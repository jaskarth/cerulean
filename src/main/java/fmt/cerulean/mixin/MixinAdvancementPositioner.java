package fmt.cerulean.mixin;

import fmt.cerulean.Cerulean;
import fmt.cerulean.advancement.CeruleanAdvancementPositioner;
import net.minecraft.advancement.AdvancementPositioner;
import net.minecraft.advancement.PlacedAdvancement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AdvancementPositioner.class)
public class MixinAdvancementPositioner {
	@Inject(method = "arrangeForTree", at = @At("HEAD"), cancellable = true)
	private static void cerulean$setupAdvancements(PlacedAdvancement root, CallbackInfo ci) {
		if (root.getAdvancementEntry().id().getNamespace().equals(Cerulean.ID)) {
			CeruleanAdvancementPositioner.position(root);
			ci.cancel();
		}
	}
}
