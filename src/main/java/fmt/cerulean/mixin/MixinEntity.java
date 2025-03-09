package fmt.cerulean.mixin;

import fmt.cerulean.entity.PlasticSwimming;
import fmt.cerulean.registry.CeruleanFluids;
import fmt.cerulean.world.CeruleanDimensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Entity.class)
public abstract class MixinEntity implements PlasticSwimming {
	private boolean cerulean$inPlastic = false;
	private boolean cerulean$inFakePlastic = false;

	@Shadow public abstract double getX();

	@Shadow public abstract double getZ();

	@Shadow public abstract World getWorld();

	@Shadow public abstract Box getBoundingBox();

	@Inject(method = "onSwimmingStart", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;floor(D)I"), cancellable = true)
	public void cerulean(CallbackInfo ci) {
		if (!this.getWorld().getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.SKIES)) {
			return;
		}

		if (cerulean$inPlastic || cerulean$inFakePlastic) {
			ci.cancel();
		}
	}

	@Inject(method = "updateMovementInFluid", at = @At("HEAD"))
	public void cerulean$resetPlasticState(TagKey<Fluid> tag, double speed, CallbackInfoReturnable<Boolean> cir) {
		if (FluidTags.WATER.equals(tag)) {
			cerulean$inPlastic = false;
			cerulean$inFakePlastic = false;
		}
	}

	@Inject(method = "updateMovementInFluid", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(DD)D"), locals = LocalCapture.CAPTURE_FAILHARD)
	public void cerulean$setPlasticState(TagKey<Fluid> tag, double speed, CallbackInfoReturnable<Boolean> cir, Box box, int i, int j, int k, int l, int m, int n, double d, boolean bl, boolean bl2, Vec3d vec3d, int o, BlockPos.Mutable mutable, int p, int q, int r, FluidState fluidState, double e) {
		if (FluidTags.WATER.equals(tag)) {
			Fluid fluid = fluidState.getFluid();
			if (fluid == CeruleanFluids.POLYETHYLENE) {
				cerulean$inPlastic = true;
			}

			if (fluid == CeruleanFluids.REALIZED_POLYETHYLENE || fluid == CeruleanFluids.REALIZED_POLYETHYLENE_FLOWING) {
				cerulean$inFakePlastic = true;
			}
		}
	}

	@Inject(method = "isOnGround", at = @At("HEAD"), cancellable = true)
	public void cerulean$itemMovement(CallbackInfoReturnable<Boolean> cir) {
		if ((Object)this instanceof ItemEntity && cerulean$inPlastic) {
			cir.setReturnValue(false);
		}
	}

	@Override
	public boolean cerulean$isInPlastic() {
		return cerulean$inPlastic;
	}
}
