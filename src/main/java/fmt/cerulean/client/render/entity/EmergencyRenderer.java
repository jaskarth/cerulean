package fmt.cerulean.client.render.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import fmt.cerulean.client.ClientState;
import fmt.cerulean.client.render.EmergencyStack;
import fmt.cerulean.client.tex.gen.EmergencyTexture;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Colors;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Random;

import java.util.Collections;

public class EmergencyRenderer {
	public static void handle(WorldRenderContext ctx) {
		MatrixStack matrixStack = ctx.matrixStack();
		VertexConsumerProvider vertexConsumerProvider = ctx.consumers();
		matrixStack.push();

		float tickDelta = ctx.tickCounter().getTickDelta(true);

		int i;
		if (ClientState.EMERGENCIES.size() > 1) {
			Collections.shuffle(ClientState.EMERGENCIES);
			i = new Random().nextInt(32);
		} else {
			i = new Random().nextInt(128);
		}

		ClientPlayerEntity ownPlayer = MinecraftClient.getInstance().player;

		for (PlayerEntity player : ClientState.EMERGENCIES) {
			if (player == ownPlayer) {
				ClientState.emergencyRender = -1;
				EmergencyStack.shuffle();
			}

			EmergencyRenderer.renderInFront(player, ctx.matrixStack(), ctx.consumers(), tickDelta, i);
			i++;
		}

//		if (!MinecraftClient.getInstance().options.getPerspective().isFirstPerson()) {
//			EmergencyRenderer.renderInFront(player, ctx.matrixStack(), ctx.consumers(), tickDelta, 0);
//		}

//		int i = 1;
//		for (Entity entity : ctx.world().getEntities()) {
//			if (entity instanceof ZombieEntity) {
//				EmergencyRenderer.renderInFront(entity, ctx.matrixStack(), ctx.consumers(), tickDelta, i);
//				i++;
//			}
//		}

		if (vertexConsumerProvider instanceof VertexConsumerProvider.Immediate imm) {
			imm.draw();
		}

		matrixStack.pop();
	}

	public static void renderInFront(Entity entity, MatrixStack stack, VertexConsumerProvider vertexConsumerProvider, float tickDelta, int idx) {
		stack.push();
		double eX = MathHelper.lerp((double)tickDelta, entity.lastRenderX, entity.getX());
		double eY = MathHelper.lerp((double)tickDelta, entity.lastRenderY, entity.getY());
		double eZ = MathHelper.lerp((double)tickDelta, entity.lastRenderZ, entity.getZ());

		Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
		Vec3d pos = camera.getPos();
		double cameraX = pos.x;
		double cameraY = pos.y;
		double cameraZ = pos.z;
		stack.translate(eX - cameraX, eY - cameraY, eZ - cameraZ);

		EmergencyRenderer.render(entity, vertexConsumerProvider, stack, tickDelta, idx);
		stack.pop();
	}

	public static void render(Entity player, VertexConsumerProvider provider, MatrixStack matrices, float tickDelta, int idx) {
		matrices.push();

		Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();

		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - camera.getYaw()));
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-camera.getPitch()));

		double dist = camera.getPos().distanceTo(player.getPos());

		int amt = 2;
		if (dist > 60) {
			amt = 1;
		}

		int width = amt;
		int height = amt * 4;

		RenderSystem.setShaderTexture(0, EmergencyTexture.getWithSize(width, height, idx));
		RenderSystem.setShader(GameRenderer::getPositionTexProgram);
		RenderSystem.disableBlend();
		RenderSystem.disableDepthTest();

		double scaleX = width * 1.2;
		double scaleZ = height * 1.2;

		float yaw = camera.getYaw();
//		matrices.translate(Math.sin(yaw * MathHelper.RADIANS_PER_DEGREE), Math.cos(yaw * MathHelper.RADIANS_PER_DEGREE), 0);
		float pitch = camera.getPitch();
//		matrices.translate(Math.sin(pitch * MathHelper.RADIANS_PER_DEGREE) * scale, -Math.cos(pitch * MathHelper.RADIANS_PER_DEGREE) * scale, 0);

		Vec3d dir = Vec3d.fromPolar(pitch, yaw);
//		matrices.translate(dir.x * scale, dir.y * scale, 0);

		double pitchDiff = 1 - Math.max(0.6, Math.abs(Math.sin(pitch * MathHelper.RADIANS_PER_DEGREE)));

		matrices.translate(-scaleX * (0.4), -(scaleZ * 0.8) + (pitchDiff * 0.7), 0);

//		matrices.translate(-1 + 0.1, 1 - (scale / 2), 0);

		matrices.scale((float) scaleX, (float) scaleZ, 1);

		RenderSystem.depthMask(false);

		BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

		Matrix4f matrix = matrices.peek().getPositionMatrix();

		bufferBuilder.vertex(matrix, 0, 0, 0).texture(0, 1).color(Colors.WHITE);
		bufferBuilder.vertex(matrix, 1, 0, 0).texture(1, 1).color(Colors.WHITE);
		bufferBuilder.vertex(matrix, 1, 1, 0).texture(1, 0).color(Colors.WHITE);
		bufferBuilder.vertex(matrix, 0, 1, 0).texture(0, 0).color(Colors.WHITE);

		BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());

		RenderSystem.depthMask(true);

		matrices.pop();
	}
}
