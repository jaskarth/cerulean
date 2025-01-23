package fmt.cerulean.mixin.client;

import fmt.cerulean.advancement.CeruleanAdvancementTab;
import fmt.cerulean.advancement.CeruleanAdvancementWidget;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.advancement.PlacedAdvancement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.advancement.AdvancementTab;
import net.minecraft.client.gui.screen.advancement.AdvancementTabType;
import net.minecraft.client.gui.screen.advancement.AdvancementWidget;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(AdvancementTab.class)
public class MixinAdvancementTab {
	@Mutable
	@Shadow @Final public AdvancementWidget rootWidget;

	@Inject(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screen/advancement/AdvancementTab;rootWidget:Lnet/minecraft/client/gui/screen/advancement/AdvancementWidget;", opcode = Opcodes.GETFIELD))
	private void cerulean$widget(MinecraftClient client, AdvancementsScreen screen, AdvancementTabType type, int index, PlacedAdvancement root, AdvancementDisplay display, CallbackInfo ci) {
		if ((Object)this instanceof CeruleanAdvancementTab) {
			this.rootWidget = new CeruleanAdvancementWidget((AdvancementTab) (Object)this, client, root, display);
		}
	}

	@Inject(method = "create", at = @At("HEAD"), cancellable = true)
	private static void cerulean$createTab(MinecraftClient client, AdvancementsScreen screen, int index, PlacedAdvancement root, CallbackInfoReturnable<AdvancementTab> cir) {
		if (root.getAdvancementEntry().id().getNamespace().equals("cerulean")) {
			Optional<AdvancementDisplay> optional = root.getAdvancement().display();
			if (optional.isEmpty()) {
				cir.setReturnValue(null);
			} else {
				for (AdvancementTabType advancementTabType : AdvancementTabType.values()) {
					if (index < advancementTabType.getTabCount()) {
						cir.setReturnValue(new CeruleanAdvancementTab(client, screen, advancementTabType, index, root, optional.get()));
						return;
					}

					index -= advancementTabType.getTabCount();
				}

				cir.setReturnValue(null);
			}
		}
	}
}
