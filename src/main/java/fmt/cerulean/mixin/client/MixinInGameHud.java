package fmt.cerulean.mixin.client;

import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(InGameHud.class)
public class MixinInGameHud {
	@ModifyConstant(method = "render", constant = @Constant(floatValue = 220.f))
	private float cerulean$fullFadeout(float constant) {
		return 255.0f;
	}
}
