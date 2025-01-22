package fmt.cerulean.mixin.client;

import fmt.cerulean.client.render.CameraFluid;
import fmt.cerulean.registry.CeruleanFluids;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class MixinCamera implements CameraFluid {
	@Shadow private BlockView area;

	@Shadow @Final private BlockPos.Mutable blockPos;

	@Shadow private boolean ready;

	@Inject(method = "getSubmersionType", at = @At("RETURN"), cancellable = true)
	public void cerulean$noPolyethyleneFog(CallbackInfoReturnable<CameraSubmersionType> cir) {
		if (cir.getReturnValue() == CameraSubmersionType.WATER) {
			FluidState fluidState = this.area.getFluidState(this.blockPos);
			if (fluidState.getFluid() == CeruleanFluids.POLYETHYLENE) {
				cir.setReturnValue(CameraSubmersionType.NONE);
			}
		}
	}

	@Override
	public Fluid cerulean$getFluid() {
		return this.ready ? this.area.getFluidState(this.blockPos).getFluid() : null;
	}
}
