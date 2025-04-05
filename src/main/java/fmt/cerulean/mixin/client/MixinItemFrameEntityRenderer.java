package fmt.cerulean.mixin.client;

import fmt.cerulean.client.ClientState;
import fmt.cerulean.client.tex.gen.StaticTexture;
import fmt.cerulean.item.component.ColorTriplex;
import fmt.cerulean.item.component.PhotoComponent;
import fmt.cerulean.registry.CeruleanItemComponents;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemFrameEntityRenderer.class)
public abstract class MixinItemFrameEntityRenderer {
	@Shadow protected abstract int getLight(ItemFrameEntity itemFrame, int glowLight, int regularLight);

	@Inject(method = "render(Lnet/minecraft/entity/decoration/ItemFrameEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", shift = At.Shift.BEFORE), cancellable = true)
	public void cerulean$renderPhoto(ItemFrameEntity itemFrameEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		ItemStack itemStack = itemFrameEntity.getHeldItemStack();

		if (!itemStack.isEmpty()) {
			PhotoComponent photo = itemStack.get(CeruleanItemComponents.PHOTO);
			ColorTriplex color = itemStack.get(CeruleanItemComponents.COLOR_TRIPLEX);
			if (photo != null) {
				boolean bl = itemFrameEntity.isInvisible();

				if (bl) {
					matrixStack.translate(0.0F, 0.0F, 0.5F);
				} else {
					matrixStack.translate(0.0F, 0.0F, 0.4375F);
				}

				int j = itemFrameEntity.getRotation() % 4 * 2;

				matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
				float h = 0.0078125F;
				matrixStack.scale(0.0078125F, 0.0078125F, 0.0078125F);
				matrixStack.translate(-64.0F, -64.0F, 0.0F);

				matrixStack.translate(0.0F, 0.0F, -1.0F);

				Identifier id;
				if (photo.id() == -1) {
					id = ClientState.PHOTOS.getSpecial(itemStack.get(CeruleanItemComponents.PHOTO_SPECIAL));
					color = null;
				} else {
					id = ClientState.PHOTOS.getId(photo.id());

					if (id == null) {
						id = StaticTexture.ID;
						ClientState.PHOTOS.ask(photo.id());
					}
				}

				Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
				VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getText(id));

				int light = this.getLight(itemFrameEntity, LightmapTextureManager.MAX_SKY_LIGHT_COORDINATE | 210, i);

				int c = color == null ? 0xFFFFFFFF : color.toABGR();
				vertexConsumer.vertex(matrix4f, 0.0F, 128.0F, -0.01F).color(c).texture(0.0F, 1.0F).light(light);
				vertexConsumer.vertex(matrix4f, 128.0F, 128.0F, -0.01F).color(c).texture(1.0F, 1.0F).light(light);
				vertexConsumer.vertex(matrix4f, 128.0F, 0.0F, -0.01F).color(c).texture(1.0F, 0.0F).light(light);
				vertexConsumer.vertex(matrix4f, 0.0F, 0.0F, -0.01F).color(c).texture(0.0F, 0.0F).light(light);

				matrixStack.pop();

				ci.cancel();
			}
		}
	}
}
