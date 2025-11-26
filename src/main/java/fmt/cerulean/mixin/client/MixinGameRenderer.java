package fmt.cerulean.mixin.client;

import fmt.cerulean.client.render.CameraFluid;
import fmt.cerulean.client.render.EmergencyOverlay;
import fmt.cerulean.client.render.frame.CeruleanFramebuffers;
import fmt.cerulean.registry.CeruleanFluids;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.Window;
import net.minecraft.fluid.Fluid;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
	@Shadow @Final
	MinecraftClient client;

	@Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
	public void cerulean$modifyFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
		Fluid f = ((CameraFluid)camera).cerulean$getFluid();
		if (f == CeruleanFluids.POLYETHYLENE || f == CeruleanFluids.REALIZED_POLYETHYLENE || f == CeruleanFluids.REALIZED_POLYETHYLENE_FLOWING) {
			cir.setReturnValue(cir.getReturnValueD() * MathHelper.lerp(this.client.options.getFovEffectScale().getValue(), 1.0, 0.85714287F));
		}
	}

	@Inject(method = "loadBlurPostProcessor", at = @At("TAIL"))
	private void cerulean$loadFramebuffer(ResourceFactory resourceFactory, CallbackInfo ci) {
		CeruleanFramebuffers.init(resourceFactory);
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;draw()V"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void cerulean$beforeDraw(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci, boolean bl, int i, int j, Window window, Matrix4f matrix4f, Matrix4fStack matrix4fStack, DrawContext context) {
		EmergencyOverlay.render(context);
	}
}
