package fmt.cerulean.mixin.client;

import fmt.cerulean.world.CeruleanDimensions;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerInteractionManager.class)
public class MixinClientPlayerNetworkManager {
	@Inject(method = "clickSlot", at = @At("HEAD"), cancellable = true)
	private void cerulean$dontThrowSlot(int syncId, int slotId, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
		if (actionType == SlotActionType.THROW || (actionType == SlotActionType.PICKUP && slotId == -999)) {
			if (player.getWorld().getDimensionKey().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
				ci.cancel();
			}
		}
	}
}
