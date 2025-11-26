package fmt.cerulean.mixin;

import com.mojang.blaze3d.platform.TextureUtil;
import fmt.cerulean.client.tex.gen.Oxidator;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.PlayerSkinTexture;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerSkinTexture.class)
public abstract class MixinPlayerSkinTexture extends ResourceTexture implements Oxidator.TextureRetriever {
	@Unique
	private NativeImage cerulean$texture;

	public MixinPlayerSkinTexture(Identifier location) {
		super(location);
	}

	@Inject(method = "onTextureLoaded", at = @At("RETURN"))
	private void cerulean$captureTexture(NativeImage image, CallbackInfo ci) {
		if (cerulean$texture != null) {
			cerulean$texture.close();
		}

		NativeImage img = new NativeImage(image.getFormat(), image.getWidth(), image.getHeight(), false);
		img.copyFrom(image);
		cerulean$texture = img;
	}

	@Override
	public NativeImage cerulean$getTexture() {
		return cerulean$texture;
	}
}
