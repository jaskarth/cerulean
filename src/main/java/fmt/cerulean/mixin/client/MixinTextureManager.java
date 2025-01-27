package fmt.cerulean.mixin.client;

import fmt.cerulean.client.tex.CeruleanTextures;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(TextureManager.class)
public class MixinTextureManager {
	@Inject(method = "method_18167", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;send(Ljava/lang/Runnable;)V", shift = At.Shift.BEFORE))
	public void cerulean$registerTextures(ResourceManager resourceManager, Executor executor, CompletableFuture completableFuture, Void void_, CallbackInfo ci) {
		CeruleanTextures.init(MinecraftClient.getInstance().getTextureManager());
	}

	@Inject(method = "close", at = @At("HEAD"))
	public void cerulean$closeTextures(CallbackInfo ci) {
		CeruleanTextures.close();
	}
}
