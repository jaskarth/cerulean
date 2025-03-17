package fmt.cerulean.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fmt.cerulean.client.render.EmergencyOverlay;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

	@Inject(at = @At("HEAD"), method = "shouldRender", cancellable = true)
	public void shouldRender(Entity entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> info) {
		if (EmergencyOverlay.shouldCensor(entity)) {
			info.setReturnValue(false);
		}
	}
}
