package fmt.cerulean.client.render;

import fmt.cerulean.item.BlueprintItem;
import fmt.cerulean.item.component.PosPair;
import fmt.cerulean.registry.CeruleanItemComponents;
import fmt.cerulean.registry.client.CeruleanRenderLayers;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class RenderVFX {
	public enum StarsMode {
		REGULAR,
		SPECIAL
	}

	public static VertexBuffer renderStars(Tessellator tessellator, StarsMode mode) {
		VertexBuffer vbo = new VertexBuffer(VertexBuffer.Usage.STATIC);
		Random random = Random.create(10842L);
		BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

		if (mode == StarsMode.REGULAR) {
			for (int i = 0; i < 15000; ++i) {
				double x = (double) (random.nextFloat() * 2.0F - 1.0F);
				double y = (double) (random.nextFloat() * 2.0F - 1.0F);
				double z = (double) (random.nextFloat() * 2.0F - 1.0F);
				double size = (0.05F + (random.nextFloat() * random.nextFloat() * random.nextFloat() * 0.55F));
				int color = MathHelper.hsvToRgb(random.nextFloat(), random.nextFloat() * 0.25f + 0.125f, 0.8f + random.nextFloat() * 0.225f + 0.02f);

				renderStar(x, y, z, random, size, buffer, color);
			}
		} else if (mode == StarsMode.SPECIAL) {
			renderStar(0, 0.99, 0, random, 0.75, buffer, 0xFF0000);
		}

		vbo.bind();
		vbo.upload(buffer.end());
		VertexBuffer.unbind();

		return vbo;
	}

	private static void renderStar(double x, double y, double z, Random random, double size, BufferBuilder buffer, int color) {
		double dist = 100.0;

		double pDist = x * x + y * y + z * z;
		if (pDist < 1.0 && pDist > 0.01) {
			pDist = 1.0 / Math.sqrt(pDist);
			x *= pDist;
			y *= pDist;
			z *= pDist;
			double startX = x * dist;
			double startY = y * dist;
			double startZ = z * dist;
			double m = Math.atan2(x, z);
			double n = Math.sin(m);
			double o = Math.cos(m);
			double p = Math.atan2(Math.sqrt(x * x + z * z), y);
			double q = Math.sin(p);
			double r = Math.cos(p);
			double s = random.nextDouble() * Math.PI * 2.0;
			double t = Math.sin(s);
			double u = Math.cos(s);

			for(int v = 0; v < 4; ++v) {
				double w = 0.0;
				double ax = (double)((v & 2) - 1) * size;
				double ay = (double)((v + 1 & 2) - 1) * size;
				double aa = ax * u - ay * t;
				double ab = ay * u + ax * t;
				double addY = aa * q + 0.0 * r;
				double ae = 0.0 * q - aa * r;
				double addX = ae * n - ab * o;
				double addZ = ab * n + ae * o;
				buffer.vertex((float) (startX + addX), (float) (startY + addY), (float) (startZ + addZ))
						.color((color >> 16) & 0xFF, (color >> 8) & 0xFF, (color >> 0) & 0xFF, 255);
			}
		}
	}

	public static void renderHeldBlueprint(WorldRenderContext ctx, ItemStack stack) {
		ClientWorld world = MinecraftClient.getInstance().world;

		PosPair pair = stack.getOrDefault(CeruleanItemComponents.POS_PAIR, PosPair.empty());

		Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
		VertexConsumer buffer = ctx.consumers().getBuffer(CeruleanRenderTypes.LINES_WIDER);

		double cameraX = camera.getPos().x;
		double cameraY = camera.getPos().y;
		double cameraZ = camera.getPos().z;

		if (pair.isEmpty()) {
			HitResult tgt = MinecraftClient.getInstance().crosshairTarget;

			if (tgt instanceof BlockHitResult bhit && !world.getBlockState(bhit.getBlockPos()).isAir()) {
				BlockPos pos = bhit.getBlockPos().offset(bhit.getSide());
				drawCuboidShapeOutline(
						ctx.matrixStack(), buffer,
						VoxelShapes.fullCube(),
						(double)pos.getX() - cameraX,
						(double)pos.getY() - cameraY,
						(double)pos.getZ() - cameraZ,
						0.0F,
						0.0F,
						1.0F,
						1F);
			}
		} else if (pair.first().isPresent() && pair.second().isEmpty()) {
			HitResult tgt = MinecraftClient.getInstance().crosshairTarget;

			if (tgt instanceof BlockHitResult bhit && !world.getBlockState(bhit.getBlockPos()).isAir()) {
				BlockPos pos = bhit.getBlockPos().offset(bhit.getSide());

				float flux = (float)Math.sin(world.getTimeOfDay() / 2f) * 0.2f;

				float red = 0;
				float blue = 0.8f + flux;
				if (!BlueprintItem.isOk(pair.first().get(), pos)) {
					red = 0.8f + flux;
					blue = 0;
				}

				drawBlockBoxOutline(
						ctx.matrixStack(), buffer,
						BlockBox.create(pair.first().get(), pos),
						cameraX,
						cameraY,
						cameraZ,
						red,
						0.0F,
						blue,
						1F);
			} else {
				BlockPos pos = pair.first().get();

				drawCuboidShapeOutline(
						ctx.matrixStack(), buffer,
						VoxelShapes.fullCube(),
						(double)pos.getX() - cameraX,
						(double)pos.getY() - cameraY,
						(double)pos.getZ() - cameraZ,
						0.0F,
						0.0F,
						1.0F,
						1F);
			}
		} else if (pair.first().isPresent() && pair.second().isPresent()) {
			BlockBox box = BlockBox.create(pair.first().get(), pair.second().get());

			drawBlockBoxOutline(
					ctx.matrixStack(), buffer,
					box,
					cameraX,
					cameraY,
					cameraZ,
					0.0F,
					0.0F,
					1.0F,
					1F);
		}
	}

	public static void drawCuboidShapeOutline(
			MatrixStack matrices,
			VertexConsumer vertexConsumer,
			VoxelShape shape,
			double offsetX,
			double offsetY,
			double offsetZ,
			float red,
			float green,
			float blue,
			float alpha
	) {
		MatrixStack.Entry entry = matrices.peek();
		shape.forEachEdge(
				(minX, minY, minZ, maxX, maxY, maxZ) -> {
					float k = (float)(maxX - minX);
					float l = (float)(maxY - minY);
					float m = (float)(maxZ - minZ);
					float n = MathHelper.sqrt(k * k + l * l + m * m);
					k /= n;
					l /= n;
					m /= n;
					vertexConsumer.vertex(entry, (float)(minX + offsetX), (float)(minY + offsetY), (float)(minZ + offsetZ))
							.color(red, green, blue, alpha)
							.normal(entry, k, l, m);
					vertexConsumer.vertex(entry, (float)(maxX + offsetX), (float)(maxY + offsetY), (float)(maxZ + offsetZ))
							.color(red, green, blue, alpha)
							.normal(entry, k, l, m);
				}
		);
	}

	public static void drawBlockBoxOutline(
			MatrixStack matrices,
			VertexConsumer vertexConsumer,
			BlockBox box,
			double offsetX,
			double offsetY,
			double offsetZ,
			float red,
			float green,
			float blue,
			float alpha
	) {

		float minX = (float) (box.getMinX() - offsetX);
		float minY = (float) (box.getMinY() - offsetY);
		float minZ = (float) (box.getMinZ() - offsetZ);

		float maxX = (float) (box.getMaxX() + 1 - offsetX);
		float maxY = (float) (box.getMaxY() + 1 - offsetY);
		float maxZ = (float) (box.getMaxZ() + 1 - offsetZ);

		WorldRenderer.drawBox(matrices, vertexConsumer, minX, minY, minZ, maxX, maxY, maxZ, red, green, blue, alpha);
	}
}
