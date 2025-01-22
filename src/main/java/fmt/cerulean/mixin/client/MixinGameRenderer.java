package fmt.cerulean.mixin.client;

import fmt.cerulean.client.render.CameraFluid;
import fmt.cerulean.registry.CeruleanFluids;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
	@Shadow @Final
	MinecraftClient client;

	@Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
	public void cerulean$modifyFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
		Fluid f = ((CameraFluid)camera).cerulean$getFluid();
		if (f == CeruleanFluids.POLYETHYLENE) {
			cir.setReturnValue(cir.getReturnValueD() * MathHelper.lerp(this.client.options.getFovEffectScale().getValue(), 1.0, 0.85714287F));
		}
	}
}
