package fmt.cerulean.client.render.entity;

import fmt.cerulean.client.tex.gen.ArgutiaeMentisRecordari;
import fmt.cerulean.client.tex.gen.TabulaeMentisRecordari;
import fmt.cerulean.entity.MemoryFrameEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.PaintingManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class MemoryFrameRenderer extends EntityRenderer<MemoryFrameEntity> {
	public MemoryFrameRenderer(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	@Override
	public Identifier getTexture(MemoryFrameEntity entity) {
		return MinecraftClient.getInstance().getPaintingManager().getBackSprite().getAtlasId();
	}

	@Override
	public void render(MemoryFrameEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		matrices.push();
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - yaw));

		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(this.getTexture(entity)));

		PaintingManager paintingManager = MinecraftClient.getInstance().getPaintingManager();

		MemoryFrameEntity.Kind kind = entity.getKind();

		this.renderPaintingBody(
				matrices,
				vertexConsumer,
				entity,
				kind.width,
				kind.height,
				paintingManager.getBackSprite()
		);

		vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(entity.renderMemories() ? TabulaeMentisRecordari.ID : ArgutiaeMentisRecordari.ID));

		this.renderPaintingSoul(
				matrices,
				vertexConsumer,
				entity,
				kind.width,
				kind.height
		);

		matrices.pop();
	}

	private void renderPaintingBody(
			MatrixStack matrices, VertexConsumer vertexConsumer, MemoryFrameEntity entity, int width, int height,Sprite backSprite
	) {
		MatrixStack.Entry entry = matrices.peek();
		float startXZ = (float)(-width) / 2.0F;
		float startY = (float)(-height) / 2.0F;
		float backMinU = backSprite.getMinU();
		float backMaxU = backSprite.getMaxU();
		float k = backSprite.getMinV();
		float l = backSprite.getMaxV();
		float m = backSprite.getMinU();
		float n = backSprite.getMaxU();
		float o = backSprite.getMinV();
		float p = backSprite.getFrameV(0.0625F);
		float q = backSprite.getMinU();
		float r = backSprite.getFrameU(0.0625F);
		float s = backSprite.getMinV();
		float t = backSprite.getMaxV();

		for (int u = 0; u < width; u++) {
			for (int v = 0; v < height; v++) {
				float xzNext = startXZ + (float)(u + 1);
				float xzHere = startXZ + (float)u;
				float yNext = startY + (float)(v + 1);
				float yHere = startY + (float)v;
				int blockX = entity.getBlockX();
				int blockY = MathHelper.floor(entity.getY() + (double)((yNext + yHere) / 2.0F));
				int blockZ = entity.getBlockZ();
				Direction direction = entity.getHorizontalFacing();
				if (direction == Direction.NORTH) {
					blockX = MathHelper.floor(entity.getX() + (double)((xzNext + xzHere) / 2.0F));
				}

				if (direction == Direction.WEST) {
					blockZ = MathHelper.floor(entity.getZ() - (double)((xzNext + xzHere) / 2.0F));
				}

				if (direction == Direction.SOUTH) {
					blockX = MathHelper.floor(entity.getX() - (double)((xzNext + xzHere) / 2.0F));
				}

				if (direction == Direction.EAST) {
					blockZ = MathHelper.floor(entity.getZ() + (double)((xzNext + xzHere) / 2.0F));
				}

				int light = WorldRenderer.getLightmapCoordinates(entity.getWorld(), new BlockPos(blockX, blockY, blockZ));

				this.vertex(entry, vertexConsumer, xzNext, yNext, backMaxU, k, 0.03125F, 0, 0, 1, light);
				this.vertex(entry, vertexConsumer, xzHere, yNext, backMinU, k, 0.03125F, 0, 0, 1, light);
				this.vertex(entry, vertexConsumer, xzHere, yHere, backMinU, l, 0.03125F, 0, 0, 1, light);
				this.vertex(entry, vertexConsumer, xzNext, yHere, backMaxU, l, 0.03125F, 0, 0, 1, light);
				this.vertex(entry, vertexConsumer, xzNext, yNext, m, o, -0.03125F, 0, 1, 0, light);
				this.vertex(entry, vertexConsumer, xzHere, yNext, n, o, -0.03125F, 0, 1, 0, light);
				this.vertex(entry, vertexConsumer, xzHere, yNext, n, p, 0.03125F, 0, 1, 0, light);
				this.vertex(entry, vertexConsumer, xzNext, yNext, m, p, 0.03125F, 0, 1, 0, light);
				this.vertex(entry, vertexConsumer, xzNext, yHere, m, o, 0.03125F, 0, -1, 0, light);
				this.vertex(entry, vertexConsumer, xzHere, yHere, n, o, 0.03125F, 0, -1, 0, light);
				this.vertex(entry, vertexConsumer, xzHere, yHere, n, p, -0.03125F, 0, -1, 0, light);
				this.vertex(entry, vertexConsumer, xzNext, yHere, m, p, -0.03125F, 0, -1, 0, light);
				this.vertex(entry, vertexConsumer, xzNext, yNext, r, s, 0.03125F, -1, 0, 0, light);
				this.vertex(entry, vertexConsumer, xzNext, yHere, r, t, 0.03125F, -1, 0, 0, light);
				this.vertex(entry, vertexConsumer, xzNext, yHere, q, t, -0.03125F, -1, 0, 0, light);
				this.vertex(entry, vertexConsumer, xzNext, yNext, q, s, -0.03125F, -1, 0, 0, light);
				this.vertex(entry, vertexConsumer, xzHere, yNext, r, s, -0.03125F, 1, 0, 0, light);
				this.vertex(entry, vertexConsumer, xzHere, yHere, r, t, -0.03125F, 1, 0, 0, light);
				this.vertex(entry, vertexConsumer, xzHere, yHere, q, t, 0.03125F, 1, 0, 0, light);
				this.vertex(entry, vertexConsumer, xzHere, yNext, q, s, 0.03125F, 1, 0, 0, light);
			}
		}
	}

	private void renderPaintingSoul(
			MatrixStack matrices, VertexConsumer vertexConsumer, MemoryFrameEntity entity, int width, int height
	) {
		MatrixStack.Entry entry = matrices.peek();
		float startXZ = (float)(-width) / 2.0F;
		float startY = (float)(-height) / 2.0F;
		double widthIncr = 1.0 / (double)width;
		double heightIncr = 1.0 / (double)height;

		for (int u = 0; u < width; u++) {
			for (int v = 0; v < height; v++) {
				float xzNext = startXZ + (float)(u + 1);
				float xzHere = startXZ + (float)u;
				float yNext = startY + (float)(v + 1);
				float yHere = startY + (float)v;
				int blockX = entity.getBlockX();
				int blockY = MathHelper.floor(entity.getY() + (double)((yNext + yHere) / 2.0F));
				int blockZ = entity.getBlockZ();
				Direction direction = entity.getHorizontalFacing();
				if (direction == Direction.NORTH) {
					blockX = MathHelper.floor(entity.getX() + (double)((xzNext + xzHere) / 2.0F));
				}

				if (direction == Direction.WEST) {
					blockZ = MathHelper.floor(entity.getZ() - (double)((xzNext + xzHere) / 2.0F));
				}

				if (direction == Direction.SOUTH) {
					blockX = MathHelper.floor(entity.getX() - (double)((xzNext + xzHere) / 2.0F));
				}

				if (direction == Direction.EAST) {
					blockZ = MathHelper.floor(entity.getZ() + (double)((xzNext + xzHere) / 2.0F));
				}

				int light = WorldRenderer.getLightmapCoordinates(entity.getWorld(), new BlockPos(blockX, blockY, blockZ));
				float minU = (float)(widthIncr * (double)(width - u));
				float maxU = (float)(widthIncr * (double)(width - (u + 1)));
				float minV = (float)(heightIncr * (double)(height - v));
				float maxV = (float)(heightIncr * (double)(height - (v + 1)));
				this.vertex(entry, vertexConsumer, xzNext, yHere, maxU, minV, -0.03125F, 0, 0, -1, light);
				this.vertex(entry, vertexConsumer, xzHere, yHere, minU, minV, -0.03125F, 0, 0, -1, light);
				this.vertex(entry, vertexConsumer, xzHere, yNext, minU, maxV, -0.03125F, 0, 0, -1, light);
				this.vertex(entry, vertexConsumer, xzNext, yNext, maxU, maxV, -0.03125F, 0, 0, -1, light);
			}
		}
	}

	private void vertex(
			MatrixStack.Entry matrix, VertexConsumer vertexConsumer, float x, float y, float u, float v, float z, int normalX, int normalY, int normalZ, int light
	) {
		vertexConsumer.vertex(matrix, x, y, z)
				.color(Colors.WHITE)
				.texture(u, v)
				.overlay(OverlayTexture.DEFAULT_UV)
				.light(light)
				.normal(matrix, (float)normalX, (float)normalY, (float)normalZ);
	}
}
