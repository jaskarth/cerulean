package fmt.cerulean.entity;

import fmt.cerulean.client.ClientState;
import fmt.cerulean.client.tex.gen.eloquia.Litteratura;
import fmt.cerulean.registry.CeruleanEntities;
import fmt.cerulean.registry.CeruleanItems;
import fmt.cerulean.util.Counterful;
import fmt.cerulean.world.data.CeruleanWorldState;
import fmt.cerulean.world.data.DimensionState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class MemoryFrameEntity extends AbstractDecorationEntity implements DreamscapeEntity {
	private static final TrackedData<Integer> KIND = DataTracker.registerData(MemoryFrameEntity.class, TrackedDataHandlerRegistry.INTEGER);

	private int clientState = -1;
	private int ticker = 0;

	private String a = "";
	private String b = "";
	private String c = "";
	private String d = "";
	private String e = "";
	private String f = "";
	private String g = "";
	private String text = "";

	public MemoryFrameEntity(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
		super(entityType, world);
		initStrings();
	}

	protected MemoryFrameEntity(World world, BlockPos pos, Direction direction) {
		super(CeruleanEntities.MEMORY_FRAME, world, pos);
		this.setFacing(direction);
		initStrings();
	}

	private void initStrings() {
		a = lie(5);
		b = lie(2);
		c = lie(2);
		d = lie(2);
		e = lie(3);
		f = lie(3);
		g = lie(5);
	}

	public static MemoryFrameEntity place(World world, BlockPos pos, Direction direction) {
		return new MemoryFrameEntity(world, pos, direction);
	}

	@Override
	protected Box calculateBoundingBox(BlockPos pos, Direction side) {
		float f = 0.46875F;
		Vec3d vec3d = Vec3d.ofCenter(pos).offset(side, -0.46875);
		Kind kind = getKind();
		double d = this.getOffset(kind.width);
		double e = this.getOffset(kind.height);
		Direction direction = side.rotateYCounterclockwise();
		Vec3d vec3d2 = vec3d.offset(direction, d).offset(Direction.UP, e);
		Direction.Axis axis = side.getAxis();
		double g = axis == Direction.Axis.X ? 0.0625 : (double)kind.width;
		double h = (double)kind.height;
		double i = axis == Direction.Axis.Z ? 0.0625 : (double)kind.width;
		return Box.of(vec3d2, g, h, i);
	}

	@Override
	public void refreshPositionAndAngles(double x, double y, double z, float yaw, float pitch) {
		this.setPosition(x, y, z);
	}

	@Override
	public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps) {
		this.setPosition(x, y, z);
	}

	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);

		if (stack.isOf(CeruleanItems.EYE_OF_VENDOR)) {
			if (player.getWorld().isClient) {
				if (clientState <= 3) {
					char[] chars = new String(Litteratura.LITTERATURA_TEMPORALIS).toCharArray();
					StringBuilder text = new StringBuilder();
					Random random = new Random();
					for (char c : chars) {
						if (random.nextInt(6) == 0) {
							text.append("§r").append(c);
						} else {
							text.append("§k").append(c);
						}
					}
					ClientState.truthful = true;
					clientState = 4;
					this.text = text.toString();
				}
			} else {
				CeruleanWorldState state = CeruleanWorldState.get((ServerWorld) player.getWorld());
				state.getFor((ServerPlayerEntity)player).truthful = true;
				state.markDirty();

				player.sendMessage(Text.literal("Seeing beyond " + Formatting.LIGHT_PURPLE + getClass().getName() + Formatting.RESET + ":"));
				player.sendMessage(Text.literal(Formatting.GOLD + "\"Plumb the heart and excavate the core just to find out we weren't wanted anymore\""));
			}

			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
	}

	@Override
	public boolean handleAttack(Entity attacker) {
		return false;
	}

	public boolean renderMemories() {
		return clientState > 3;
	}

	@Override
	public void onPlace() {

	}

	@Override
	public void onBreak(@Nullable Entity breaker) {

	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		builder.add(KIND, 0);
	}

	@Override
	public void onTrackedDataSet(TrackedData<?> data) {
		if (KIND.equals(data)) {
			this.updateAttachmentPosition();
		}
	}

	public Kind getKind() {
		return Kind.VALUES[this.dataTracker.get(KIND)];
	}

	private double getOffset(int length) {
		return length % 2 == 0 ? 0.5 : 0.0;
	}

	// all's right in the world.
	private static class LoadGuard {
		private PlayerEntity player = MinecraftClient.getInstance().player;
	}

	@Override
	public void tick() {
		if (this.getWorld().isClient) {
			PlayerEntity player = new LoadGuard().player;
			if (player != null && clientState == -1 && player.getInventory().contains(i -> i.isOf(CeruleanItems.EYE_OF_VENDOR))) {
				clientState = 0;
			}

			if (clientState == 0) {
				ticker++;
				if (ticker == 60) {
					clientState = 1;
					ticker = 0;
				}
			} else if (clientState == 1) {
				ticker++;
			} else if (clientState == 2) {
				ticker++;
				if (ticker == 20) {
					clientState = 3;
					ticker = 0;
				}
			} else if (clientState == 3) {
				ticker++;
			}

			if (player != null && clientState >= 0) {
				int love = 0;
				int hate = 0;
				if (clientState == 1) {
					love = Math.clamp(ticker / 6, 0, 4);
					hate = Math.clamp((ticker / 6) - 4, 0, 6);
				} else if (clientState == 2) {
					love = Math.clamp(4 - (ticker / 4), 0, 4);
					hate = Math.clamp(6 - (ticker / 4), 0, 6);
				} else if (clientState == 3) {
					love = Math.clamp(ticker / 12, 0, 3);
					hate = Math.clamp(ticker / 12, 0, 3);
				}

				if (love == 4 && hate == 6 && player.distanceTo(this) < 3.5 && clientState == 1) {
					clientState = 2;
					ticker = 0;
				}

				String come = "come".substring(0, clientState <= 2 ? love : 0) + "§k" + "come".substring(clientState <= 2 ? love : 0, 4);
				String closer = "closer".substring(0, clientState <= 2 ? hate : 0) + "§k" + "closer".substring(clientState <= 2 ? hate : 0, 6);

				String the = "§6" + "the".substring(0, clientState == 3 ? love : 0) + "§r§k" + "the".substring(clientState == 3 ? love : 0, 3);
				String eye = "§6" + "eye".substring(0, clientState == 3 ? hate : 0) + "§r§k" + "eye".substring(clientState == 3 ? hate : 0, 3);

				DimensionState st = Counterful.get(player);
				if (st.indifference == 0 && st.ennui == 0) {
					if (clientState <= 3) {
						player.sendMessage(Text.literal("§k" + a + "§r" + come + " §k" + b + the + c + " " + d + "§r" + closer + "§k" + e + " " + f + eye + g), true);
					} else {
						player.sendMessage(Text.literal(this.text), true);
					}
				}
			}
		}

		super.tick();
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		nbt.putByte("facing", (byte)this.facing.getHorizontal());
		super.writeCustomDataToNbt(nbt);
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		this.facing = Direction.fromHorizontal(nbt.getByte("facing"));
		super.readCustomDataFromNbt(nbt);
		this.setFacing(this.facing);
	}

	private static String lie(int length) {
		StringBuilder b = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			b.append((char)('a' + random.nextInt(26)));
		}

		return b.toString();
	}

	@Override
	public Vec3d getSyncedPos() {
		return Vec3d.of(this.attachedBlockPos);
	}

	@Override
	public Packet<ClientPlayPacketListener> createSpawnPacket(EntityTrackerEntry entityTrackerEntry) {
		return new EntitySpawnS2CPacket(this, this.facing.getId(), this.getAttachedBlockPos());
	}

	@Override
	public void onSpawnPacket(EntitySpawnS2CPacket packet) {
		super.onSpawnPacket(packet);
		this.setFacing(Direction.byId(packet.getEntityData()));
	}

	public enum Kind {
		K1x1(1, 1);

		public static final Kind[] VALUES = Kind.values();

		public final int width;
		public final int height;

		Kind(int width, int height) {
			this.width = width;
			this.height = height;
		}
	}
}
