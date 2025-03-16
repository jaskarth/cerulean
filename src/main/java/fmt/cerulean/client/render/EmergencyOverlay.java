package fmt.cerulean.client.render;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import fmt.cerulean.Cerulean;
import fmt.cerulean.client.ClientState;
import fmt.cerulean.registry.CeruleanItems;
import fmt.cerulean.util.Vec2d;
import fmt.cerulean.util.Vec2i;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Camera.Projection;
import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class EmergencyOverlay {
	public static final List<HexState> HEX_STATES = Lists.newArrayList();

	public static void render(DrawContext context) {
		MinecraftClient client = MinecraftClient.getInstance();
		List<Censorship> censorships = Lists.newArrayList();

		Camera camera = client.gameRenderer.getCamera();
		for (Entity entity : client.world.getEntities()) {
			if (entity instanceof ClientPlayerEntity) {
				int emergency = ClientState.emergencyRender;
				if (emergency >= 0 && !client.options.getPerspective().isFirstPerson()) {
					censorships.add(Censorship.of(entity, 0.2f + emergency / 20f));
				}
			}
			/*
			if (!(entity instanceof ClientPlayerEntity) && !entity.isInvisibleTo(client.player) && entity.shouldRender(camera.getPos().getX(), camera.getPos().getY(), camera.getPos().getZ())) {
				censorships.add(Censorship.of(entity, 1));
			}
			*/
		}

		censorships.removeIf(c -> c.shape == null);
		
		if (client.player.getStackInHand(Hand.MAIN_HAND).getItem() == CeruleanItems.BRUSH) {
			return;
		}
		int width = client.getWindow().getFramebufferWidth();
		int height = client.getWindow().getFramebufferHeight();
		int WIDTH = 19;
		int HEIGHT = 19;
		Random random = new Random();
		boolean off = false;
		int i = 0;
		long time = System.currentTimeMillis();
		for (int ry = -HEIGHT / 2; ry < height; ry += HEIGHT / 2) {
			int offset = off ? 26 / -2 : 0;
			off = !off;
			for (int rx = 0 + offset; rx < width + WIDTH; rx += 26) {
				if (i >= HEX_STATES.size()) {
					HEX_STATES.add(HexState.generate(random, time));
				}
				HexState state = HEX_STATES.get(i);
				boolean active = false;
				for (Censorship censorship : censorships) {
					if (censorship.shape.containsNearby(rx + WIDTH, ry + HEIGHT)) {
						if (censorship.heat < 1 && random.nextFloat() > censorship.heat) {
							continue;
						}
						active = true;
						break;
					}
				}
				if (active) {
					state.active = true;
				} else if (state.active) {
					state.deactivate(random, time);
				}
				int variant = state.getVariant(random, time);
				if (variant != -1) {
					context.drawTexture(Cerulean.id("textures/gui/hexa" + (1 + state.getVariant(random, time)) + ".png"), rx, ry, 0, 0, 0, WIDTH, HEIGHT, WIDTH, HEIGHT);
				}
				i++;
			}
		}
	}

	public static Vec2d getProjection(Vec3d vector) {
		MinecraftClient client = MinecraftClient.getInstance();
		Camera camera = client.gameRenderer.getCamera();
		Projection proj = camera.getProjection();
		Vec3d ray = vector.subtract(camera.getPos()).normalize();
		Vec3d center = camera.getProjection().getPosition(0, 0).add(camera.getPos());
		Vec3d CA = proj.getTopRight();
		Vec3d CB = proj.getTopLeft();
		Vec3d CC = proj.getBottomLeft();
		Vec3d CD = proj.getBottomRight();
		Vec3d normal = (CB.subtract(CA)).crossProduct(CC.subtract(CA)).normalize();

		Vec3d diff = center.subtract(camera.getPos());
		double p1 = diff.dotProduct(normal);
		double p2 = ray.dotProduct(normal);
		if (p2 < 0) {
			return null;
		}
		double distance = p1 / p2;
		
		Vec3d point = camera.getPos().add(ray.multiply(distance));

		CA = CA.add(camera.getPos());
		CB = CB.add(camera.getPos());
		CC = CC.add(camera.getPos());
		CD = CD.add(camera.getPos());

		double y = (point.subtract(CB).dotProduct(CA.subtract(CB))) / (CA.subtract(CB).dotProduct(CA.subtract(CB)));
		double x = -(point.subtract(CB).dotProduct(CC.subtract(CB))) / (CC.subtract(CB).dotProduct(CC.subtract(CB)));
		x = 1 - (-x);
		y = 1 - y;
		double fov = client.options.getFov().getValue() * client.player.getFovMultiplier();
		double scalar = 0.90 - 0.1 * (fov - 30) / 150;
		//scalar = 0.80;
		x = (x - 0.5) * scalar + 0.5;
		y = (y - 0.5) * scalar + 0.5;
		return new Vec2d(x, y);
	}

	public static int convexHullOrientation(Vec2i current, Vec2i candidate, Vec2i temp) {
		int val = (candidate.z() - current.z()) * (temp.x() - candidate.x()) - (candidate.x() - current.x()) * (temp.z() - candidate.z());
		return val == 0 ? 0 : val > 0 ? 1 : 2;
	}

	public static List<Vec2i> convexHull(List<Vec2i> points) {
		List<Vec2i> hull = Lists.newArrayList();
		int leftmost = 0;
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).x() < points.get(leftmost).x()) {
				leftmost = i;
			}
		}
		IntSet used = new IntOpenHashSet();
		int current = leftmost;
		do {
			hull.add(points.get(current));
			used.add(current);
			int next = (current + 1) % points.size();
			for (int i = 0; i < points.size(); i++) {
				if (convexHullOrientation(points.get(current), points.get(i), points.get(next)) == 2) {
					next = i;
				}
			}
			current = next;
		} while (!used.contains(current));
		return hull;
	}

	private static class Censorship {
		public float heat;
		public ConvexShape shape;

		public Censorship(List<Vec3d> worldPoints, float heat) {
			this.heat = heat;

			MinecraftClient client = MinecraftClient.getInstance();
			if (client.player.getStackInHand(Hand.MAIN_HAND).getItem() == CeruleanItems.BRUSH) {
				return;
			}
			int width = client.getWindow().getFramebufferWidth();
			int height = client.getWindow().getFramebufferHeight();
			List<Vec2i> points = Lists.newArrayList();
			for (Vec3d worldPoint : worldPoints) {
				Vec2d pos = EmergencyOverlay.getProjection(worldPoint);
				if (pos == null) {
					continue;
				}
				int x = (int) (pos.x() * (width)) - 2;
				int y = (int) (pos.z() * (height)) - 2;
				x /= client.getWindow().getScaleFactor();
				y /= client.getWindow().getScaleFactor();
				points.add(new Vec2i(x, y));
			}
			if (points.size() < 3) {
				return;
			}
			List<Vec2i> convex = convexHull(points);
			this.shape = new ConvexShape(convex);
		}

		public static Censorship of(BlockPos pos, float heat) {
			List<Vec3d> points = Lists.newArrayList();
			Vec3d centerPos = pos.toCenterPos();
			for (int i = 0; i < 8; i++) {
				double xo = (i & 1) != 0 ? -0.6 : 0.6;
				double yo = (i & 2) != 0 ? -0.6 : 0.6;
				double zo = (i & 4) != 0 ? -0.6 : 0.6;
				points.add(centerPos.add(xo, yo, zo));
			}
			return new Censorship(points, heat);
		}

		public static Censorship of(Entity entity, float heat) {
			Box box = entity.getBoundingBox();
			Vec3d min = box.getMinPos();
			Vec3d max = box.getMaxPos();
			List<Vec3d> points = List.of(
				min,
				new Vec3d(min.x, min.y, max.z),
				new Vec3d(min.x, max.y, min.z),
				new Vec3d(min.x, max.y, max.z),
				new Vec3d(max.x, min.y, min.z),
				new Vec3d(max.x, min.y, max.z),
				new Vec3d(max.x, max.y, min.z),
				max
			);
			return new Censorship(points, heat);
		}
	}

	private static class ConvexShape {
		private List<Vec2i> shape;
		private int minX, minY, maxX, maxY;
		private int nudgeX, nudgeY;

		public ConvexShape(List<Vec2i> shape) {
			this.shape = shape;
			this.minX = shape.get(0).x();
			this.minY = shape.get(0).z();
			this.maxX = shape.get(0).x();
			this.maxY = shape.get(0).z();
			for (int i = 1; i < shape.size(); i++) {
				Vec2i point = shape.get(i);
				this.minX = Math.min(this.minX, point.x());
				this.minY = Math.min(this.minY, point.z());
				this.maxX = Math.max(this.maxX, point.x());
				this.maxY = Math.max(this.maxY, point.z());
			}
			int discretionX = maxX - minX;
			int discretionY = maxY - minY;
			nudgeX = 30;
			nudgeY = 30;
			while (discretionX > 0 && nudgeX > discretionX) {
				nudgeX /= 2;
			}
			while (discretionY > 0 && nudgeY > discretionY) {
				nudgeY /= 2;
			}
		}

		public boolean containsNearby(int x, int y) {
			for (int xo = 0; xo < 60; xo += nudgeX) {
				for (int yo = 0; yo < 60; yo += nudgeY) {
					if (contains(x - 30 + xo, y - 30 + yo)) {
						return true;
					}
				}
			}
			return false;
		}

		public boolean contains(int x, int y) {
			if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
				int p = 0;
				int n = 0;
				for (int i = 0; i < shape.size(); i++) {
					Vec2i a = shape.get(i);
					Vec2i b = shape.get((i + 1) % shape.size());
					int cross = (x - a.x()) * (b.z() - a.z()) - (y - a.z()) * (b.x() - a.x());
					if (cross < 0) {
						n++;
					} else if (cross > 0) {
						p++;
					}
					if (p > 0 && n > 0) {
						return false;
					}
				}
				return true;
			}
			return false;
		}
	}

	private static class HexState {
		private static final int STABLE = 0;
		private static final int ERRATIC = 1;
		private static final int RANDOM = 2;
		private static final int FLICKERING = 3;
		private int personality;
		private int current;
		private long nextUpdate;
		private long persist;
		private boolean active = false;

		private static HexState generate(Random random, long time) {
			HexState state = new HexState();
			state.renew(random, time);
			return state;
		}

		private void renew(Random random, long time) {
			int[] weights = new int[] {
				30, 10, 30, 60
			};
			int sum = 0;
			for (int i = 0; i < weights.length; i++) {
				sum += weights[i];
			}
			int p = random.nextInt(sum);
			for (int i = 0; i < weights.length; i++) {
				if (p < weights[i]) {
					p = i;
					break;
				} else {
					p -= weights[i];
				}
			}
			this.personality = p;
			this.current = switch(personality) {
				case STABLE, ERRATIC, RANDOM -> random.nextInt(5);
				case FLICKERING -> (random.nextInt(6) == 0 ? 3 : 1) + random.nextInt(2);
				default -> 0;
			};
			queueNextUpdate(random, time);
		}

		public void deactivate(Random random, long time) {
			this.active = false;
			this.persist = time + random.nextInt(300);
		}

		private void queueNextUpdate(Random random, long time) {
			this.nextUpdate = time + switch(personality) {
				case ERRATIC -> 50 + random.nextInt(200);
				case RANDOM -> 300 + random.nextInt(6000);
				case FLICKERING -> 200 + random.nextInt(1000);
				case STABLE -> -1;
				default -> -1;
			};
		}

		public int getVariant(Random random, long time) {
			if (!active) {
				if (time > persist) {
					return -1;
				} else {
					return current;
				}
			}
			boolean shouldUpdate = nextUpdate != -1 && time > nextUpdate;
			if (shouldUpdate) {
				queueNextUpdate(random, time);
				this.current = switch(this.personality) {
					case ERRATIC, RANDOM -> random.nextInt(5);
					case FLICKERING -> switch(current) {
						case 0 -> 1;
						case 1 -> 0;
						case 3 -> 4;
						case 4 -> 3;
						default -> 0;
					};
					default -> current;
				};
			}
			if (random.nextInt(1000) == 0) {
				renew(random, time);
			}
			return this.current;
		}
	}
}
