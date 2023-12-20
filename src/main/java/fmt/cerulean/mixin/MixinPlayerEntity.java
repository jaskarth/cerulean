package fmt.cerulean.mixin;

import fmt.cerulean.world.CeruleanDimensions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {
	@Inject(method = "isBlockBreakingRestricted", at = @At("HEAD"), cancellable = true)
	private void cerulean$dreamscapeInteraction(World world, BlockPos pos, GameMode gameMode, CallbackInfoReturnable<Boolean> cir) {
		if (world.getDimensionKey().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
			cir.setReturnValue(true);
		}
	}
}
