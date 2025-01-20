package fmt.cerulean.mixin.client;

import fmt.cerulean.world.CeruleanDimensions;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
	@Shadow private @Nullable ClientWorld world;

	@Redirect(method = "processWorldEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundManager;play(Lnet/minecraft/client/sound/SoundInstance;)V"))
	private void cerulean$noPortalSounds(SoundManager instance, SoundInstance sound) {
		Identifier id = this.world.getDimensionEntry().getKey().get().getValue();
		if (id.equals(CeruleanDimensions.DREAMSCAPE)) {
			return;
		}

		if (id.equals(CeruleanDimensions.SKIES)) {
			return;
		}

		instance.play(sound);
	}
}
