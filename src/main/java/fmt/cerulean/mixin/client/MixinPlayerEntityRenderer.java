package fmt.cerulean.mixin.client;

import fmt.cerulean.client.ClientState;
import fmt.cerulean.client.render.entity.EmergencyRenderer;
import fmt.cerulean.registry.CeruleanItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class MixinPlayerEntityRenderer {
	@Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
	public void cerulean$renderEmergency(AbstractClientPlayerEntity player, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		if (ClientState.remember) {
			if (player.getStackInHand(Hand.MAIN_HAND).isOf(CeruleanItems.CAMERA) || player.getStackInHand(Hand.OFF_HAND).isOf(CeruleanItems.CAMERA)) {
				ClientState.EMERGENCIES.add(player);
				ci.cancel();
			}
		}
	}
}
