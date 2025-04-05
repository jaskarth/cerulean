package fmt.cerulean.mixin.client;

import fmt.cerulean.Cerulean;
import fmt.cerulean.client.ClientState;
import fmt.cerulean.client.render.item.Pressable;
import fmt.cerulean.client.tex.gen.StaticTexture;
import fmt.cerulean.item.component.ColorTriplex;
import fmt.cerulean.item.component.PhotoComponent;
import fmt.cerulean.registry.CeruleanItemComponents;
import fmt.cerulean.registry.CeruleanItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Colors;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class MixinHeldItemRenderer {
	@Shadow @Final private MinecraftClient client;

	@Shadow protected abstract void renderArmHoldingItem(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float equipProgress, float swingProgress, Arm arm);

	@Shadow @Final private static RenderLayer MAP_BACKGROUND;



	@Inject(method = "renderFirstPersonItem", at = @At("HEAD"), cancellable = true)
	public void cerulean$renderPhoto(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		if (this.client.player.isUsingItem() && this.client.player.getActiveItem().isOf(CeruleanItems.CAMERA)) {
			ci.cancel();
		}

		if (item.isOf(CeruleanItems.PHOTOGRAPH)) {
			matrices.push();
			boolean bl = hand == Hand.MAIN_HAND;
			Arm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();

			int useTicks = ((Pressable)this.client.player).cerulean$photoHeldDownTicks();
			boolean dir = ((Pressable)this.client.player).cerulean$lettingGo();

			float heldProgress = MathHelper.clamp((useTicks + (dir ? -tickDelta : tickDelta)) / 4f, 0, 1);

			float f = arm == Arm.RIGHT ? 1.0F : -1.0F;
			matrices.translate(f * 0.125F, -0.125F, 0.0F);
			if (!this.client.player.isInvisible()) {
				matrices.push();
				float backProg = MathHelper.lerp(heldProgress, 0f, -0.04f);
				matrices.translate(0, backProg, 0);
				float pzMul = MathHelper.lerp(heldProgress, 10, 45);
				matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f * pzMul));
				float pyRot = MathHelper.lerp(heldProgress, 0, 40);
				matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(pyRot));
				this.renderArmHoldingItem(matrices, vertexConsumers, light, equipProgress, swingProgress, arm);
				matrices.pop();
			}

			matrices.push();
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

			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getText(Cerulean.id("textures/gui/paper.png")));
			Matrix4f matrix4f = matrices.peek().getPositionMatrix();
			vertexConsumer.vertex(matrix4f, -7.0F, 135.0F, 0.0F).color(Colors.WHITE).texture(0.0F, 1.0F).light(light);
			vertexConsumer.vertex(matrix4f, 135.0F, 135.0F, 0.0F).color(Colors.WHITE).texture(1.0F, 1.0F).light(light);
			vertexConsumer.vertex(matrix4f, 135.0F, -7.0F, 0.0F).color(Colors.WHITE).texture(1.0F, 0.0F).light(light);
			vertexConsumer.vertex(matrix4f, -7.0F, -7.0F, 0.0F).color(Colors.WHITE).texture(0.0F, 0.0F).light(light);

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

			matrices.pop();

			ci.cancel();
		}
	}
}
