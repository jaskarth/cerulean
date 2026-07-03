package fmt.cerulean.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import fmt.cerulean.Cerulean;
import fmt.cerulean.client.ClientState;
import fmt.cerulean.client.render.PhotographicRenderer;
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
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class MixinHeldItemRenderer {
	@Shadow @Final private MinecraftClient client;

	@Inject(method = "renderFirstPersonItem", at = @At("HEAD"), cancellable = true)
	public void cerulean$renderPhoto(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		if (this.client.player.isUsingItem() && this.client.player.getActiveItem().isOf(CeruleanItems.CAMERA)) {
			ci.cancel();
		}

		if (item.isOf(CeruleanItems.PHOTOGRAPH)) {
			PhotographicRenderer.renderPhotoInHand((HeldItemRenderer) (Object) this, player, tickDelta, swingProgress, equipProgress, hand, item, matrices, vertexConsumers, light);

			ci.cancel();
		}

		if (item.isOf(CeruleanItems.ANCIENT_PHOTOGRAPH)) {
			PhotographicRenderer.renderPartialAncientPhoto((HeldItemRenderer) (Object) this, player, tickDelta, swingProgress, equipProgress, hand, item, matrices, vertexConsumers, light);

			ci.cancel();
		}
	}
}
