package fmt.cerulean.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;

@Mixin(GameRenderer.class)
public interface GameRendererAccessor {
	@Accessor("blurPostProcessor")
	PostEffectProcessor getBlurPostProcessor();

	@Invoker("getFov")
	double invoke$getFov(Camera camera, float tickDelta, boolean changingFov);
}
