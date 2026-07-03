package fmt.cerulean.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import fmt.cerulean.Cerulean;
import fmt.cerulean.client.ClientState;
import fmt.cerulean.client.render.item.Pressable;
import fmt.cerulean.client.tex.gen.StaticTexture;
import fmt.cerulean.item.component.ColorTriplex;
import fmt.cerulean.item.component.PhotoComponent;
import fmt.cerulean.registry.CeruleanItemComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Colors;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class PhotographicRenderer {
	public static void renderPhotoInHand(HeldItemRenderer renderer, AbstractClientPlayerEntity player, float tickDelta, float swingProgress, float equipProgress, Hand hand, ItemStack item, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		matrices.push();
		setupPhotographicTransforms(renderer, player, tickDelta, swingProgress, equipProgress, hand, matrices, vertexConsumers, light);
		renderPhotoBackground(matrices, vertexConsumers, light);
		VertexConsumer vertexConsumer;
		Matrix4f matrix4f;

		PhotoComponent photo = item.get(CeruleanItemComponents.PHOTO);
		ColorTriplex color = item.get(CeruleanItemComponents.COLOR_TRIPLEX);

		Identifier id;
		if (photo == null) {
			id = StaticTexture.ID;
		} else {
			if (photo.id() == -1) {
				id = ClientState.PHOTOS.getSpecial(item.get(CeruleanItemComponents.PHOTO_SPECIAL));
				color = null;
			} else {
				id = ClientState.PHOTOS.getId(photo.id());
				if (id == null) {
					ClientState.PHOTOS.ask(photo.id());
					id = StaticTexture.ID;
				}
			}
		}

		int c = color == null ? 0xFFFFFFFF : color.toABGR();
		matrix4f = matrices.peek().getPositionMatrix();

		vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getText(id));

		vertexConsumer.vertex(matrix4f, 0.0F, 128.0F, -0.01F).color(c).texture(0.0F, 1.0F).light(light);
		vertexConsumer.vertex(matrix4f, 128.0F, 128.0F, -0.01F).color(c).texture(1.0F, 1.0F).light(light);
		vertexConsumer.vertex(matrix4f, 128.0F, 0.0F, -0.01F).color(c).texture(1.0F, 0.0F).light(light);
		vertexConsumer.vertex(matrix4f, 0.0F, 0.0F, -0.01F).color(c).texture(0.0F, 0.0F).light(light);

		matrices.pop();

	}

	private static void renderPhotoBackground(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getText(Cerulean.id("textures/gui/paper.png")));
		Matrix4f matrix4f = matrices.peek().getPositionMatrix();
		vertexConsumer.vertex(matrix4f, -7.0F, 135.0F, 0.0F).color(Colors.WHITE).texture(0.0F, 1.0F).light(light);
		vertexConsumer.vertex(matrix4f, 135.0F, 135.0F, 0.0F).color(Colors.WHITE).texture(1.0F, 1.0F).light(light);
		vertexConsumer.vertex(matrix4f, 135.0F, -7.0F, 0.0F).color(Colors.WHITE).texture(1.0F, 0.0F).light(light);
		vertexConsumer.vertex(matrix4f, -7.0F, -7.0F, 0.0F).color(Colors.WHITE).texture(0.0F, 0.0F).light(light);
	}

	public static void renderPartialAncientPhoto(HeldItemRenderer renderer, AbstractClientPlayerEntity player, float tickDelta, float swingProgress, float equipProgress, Hand hand, ItemStack item, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		matrices.push();

		setupPhotographicTransforms(renderer, player, tickDelta, swingProgress, equipProgress, hand, matrices, vertexConsumers, light);
		renderPhotoBackground(matrices, vertexConsumers, light);
		MinecraftClient client = MinecraftClient.getInstance();

		Matrix4f mv = RenderSystem.getModelViewMatrix();
		Matrix4f proj = RenderSystem.getProjectionMatrix();

		Matrix4f matrix4f = matrices.peek().getPositionMatrix();

		Vector3f dest = new Vector3f();
		matrix4f.transformPosition(0.0F, 0.0F, -0.01F, dest);

		Matrix4f nv = new Matrix4f();
		nv.set(proj);
		nv.mul(mv);
		nv.mul(matrix4f);
		nv.project(0, 0, 0, new int[]{0, 0, client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight()}, dest);
		int vx = (int) dest.x;
		int vy = (int) dest.y;

		nv.project(128, 128, 0, new int[]{0, 0, client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight()}, dest);

		int vx1 = (int) dest.x;
		int vy1 = (int) dest.y;

		ClientState.memoryLimits = new Rect2i(vx, vy1, (vx1 - vx), (vy - vy1));

		matrices.pop();
	}

	private static void setupPhotographicTransforms(HeldItemRenderer renderer, AbstractClientPlayerEntity player, float tickDelta, float swingProgress, float equipProgress, Hand hand, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		boolean bl = hand == Hand.MAIN_HAND;
		Arm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();

		int useTicks = ((Pressable) player).cerulean$photoHeldDownTicks();
		boolean dir = ((Pressable) player).cerulean$lettingGo();

		float heldProgress = MathHelper.clamp((useTicks + (dir ? -tickDelta : tickDelta)) / 4f, 0, 1);

		float f = arm == Arm.RIGHT ? 1.0F : -1.0F;
		matrices.translate(f * 0.125F, -0.125F, 0.0F);
		if (!player.isInvisible()) {
			matrices.push();
			float backProg = MathHelper.lerp(heldProgress, 0f, -0.04f);
			matrices.translate(0, backProg, 0);
			float pzMul = MathHelper.lerp(heldProgress, 10, 45);
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f * pzMul));
			float pyRot = MathHelper.lerp(heldProgress, 0, 40);
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(pyRot));
			renderer.renderArmHoldingItem(matrices, vertexConsumers, light, equipProgress, swingProgress, arm);
			matrices.pop();
		}

		matrices.translate(f * 0.51F, -0.08F + equipProgress * -1.2F, -0.75F);
		float g = MathHelper.sqrt(swingProgress);
		float h = MathHelper.sin(g * (float) Math.PI);
		float i = -0.5F * h;
		float j = 0.4F * MathHelper.sin(g * (float) (Math.PI * 2));
		float k = -0.3F * MathHelper.sin(swingProgress * (float) Math.PI);
		matrices.translate(f * i, j - 0.3F * h, k);
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(h * -45.0F));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f * h * -30.0F));

		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
		matrices.scale(0.38F, 0.38F, 0.38F);
		float xTransProg = MathHelper.lerp(heldProgress, -0.5f, -3f);
		float yTransProg = MathHelper.lerp(heldProgress, -0.5f, -1.9f);
		matrices.translate(xTransProg, yTransProg, 0.0F);
		float scaleDiv = MathHelper.lerp(heldProgress, 128, 48);
		float scale = 1 / scaleDiv;
		matrices.scale(scale, scale, scale);
	}
}
