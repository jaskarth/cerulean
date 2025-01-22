package fmt.cerulean.mixin;

import fmt.cerulean.registry.CeruleanFluids;
import fmt.cerulean.world.CeruleanDimensions;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class MixinEntity {
	@Shadow public abstract double getX();

	@Shadow public abstract double getZ();

	@Shadow public abstract World getWorld();

	@Shadow public abstract Box getBoundingBox();

	@Inject(method = "onSwimmingStart", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;floor(D)I"), cancellable = true)
	public void cerulean(CallbackInfo ci) {
		if (!this.getWorld().getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.SKIES)) {
			return;
		}

		Box box = this.getBoundingBox().contract(0.001);
		int i = MathHelper.floor(box.minX);
		int j = MathHelper.ceil(box.maxX);
		int k = MathHelper.floor(box.minY);
		int l = MathHelper.ceil(box.maxY);
		int m = MathHelper.floor(box.minZ);
		int n = MathHelper.ceil(box.maxZ);

		BlockPos.Mutable mutable = new BlockPos.Mutable();

		for (int p = i; p < j; p++) {
			for (int q = k; q < l; q++) {
				for (int r = m; r < n; r++) {
					mutable.set(p, q, r);
					FluidState fluidState = this.getWorld().getFluidState(mutable);
					if (fluidState.getFluid() == CeruleanFluids.POLYETHYLENE) {
						ci.cancel();
					}
				}
			}
		}
	}
}
