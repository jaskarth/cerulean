package fmt.cerulean.mixin;

import fmt.cerulean.world.CeruleanDimensions;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RedstoneView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RedstoneView.class)
public interface MixinRedstoneView {
	@Inject(method = {"getStrongRedstonePower", "getEmittedRedstonePower*"}, at = @At("HEAD"), cancellable = true)
	private void cerulean$powerless(CallbackInfoReturnable<Integer> cir) {
		if (((Object)this) instanceof World world) {
			if (world.getDimensionKey().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
				cir.setReturnValue(0);
			}
		}
	}
}
