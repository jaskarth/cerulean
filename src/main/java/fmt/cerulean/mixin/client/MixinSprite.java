package fmt.cerulean.mixin.client;

import fmt.cerulean.client.tex.gen.DynamicEyeTexture;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteContents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Sprite.class)
public class MixinSprite {
	@Shadow @Final private SpriteContents contents;

	@Inject(method = "createAnimation", at = @At("HEAD"), cancellable = true)
	public void cerulean$animation(CallbackInfoReturnable<Sprite.TickableAnimation> cir) {
		SpriteContents contents = this.contents;
		if (contents.getId() == DynamicEyeTexture.ID) {
			Sprite sprite = (Sprite) (Object) this;

			cir.setReturnValue(
					new Sprite.TickableAnimation() {
						@Override
						public void tick() {
							DynamicEyeTexture.tick();
							contents.upload(sprite.getX(), sprite.getY());
						}

						@Override
						public void close() {

						}
					}
			);
		}
	}
}
