package fmt.cerulean.mixin;

import fmt.cerulean.registry.CeruleanItems;
import fmt.cerulean.world.data.CeruleanWorldState;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryChangedCriterion.class)
public class MixinInventoryChangedCriterion {
	@Inject(method = "trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"))
	private void cerulean$detect(ServerPlayerEntity player, PlayerInventory inventory, ItemStack stack, CallbackInfo ci) {
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack st = inventory.getStack(i);
			if (st.isOf(CeruleanItems.FUCHSIA_INGOT)) {
				CeruleanWorldState state = CeruleanWorldState.get(player.getServerWorld());
				state.getFor(player).worldworn = true;
				state.markDirty();
			}
		}
	}
}
