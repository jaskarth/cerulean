package fmt.cerulean.mixin.client;

import fmt.cerulean.Cerulean;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.AdvancementToast;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(AdvancementToast.class)
public abstract class MixinAdvancementToast implements Toast {
	@Shadow @Final private AdvancementEntry advancement;

	@Shadow @Final private static Identifier TEXTURE;

	@Inject(method = "draw", at = @At("HEAD"), cancellable = true)
	private void cerulean$draw(DrawContext context, ToastManager manager, long startTime, CallbackInfoReturnable<Toast.Visibility> cir) {
		AdvancementDisplay advancementDisplay = this.advancement.value().display().orElse(null);

		if (advancementDisplay != null && this.advancement.id().getNamespace().equals(Cerulean.ID)) {
			context.drawGuiTexture(TEXTURE, 0, 0, this.getWidth(), this.getHeight());
			List<OrderedText> list = manager.getClient().textRenderer.wrapLines(advancementDisplay.getTitle(), 125);
			int color = advancementDisplay.getFrame() == AdvancementFrame.CHALLENGE ? 0xff88ff : 0xffff00;

			if (list.size() == 1) {
				context.drawText(manager.getClient().textRenderer, Text.translatable("advancements.toast.cerulean"), 30, 7, color | Colors.BLACK, false);
				context.drawText(manager.getClient().textRenderer, list.get(0), 30, 18, -1, false);
			} else {
				if (startTime < 1500L) {
					int alpha = MathHelper.floor(MathHelper.clamp((float) (1500L - startTime) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 0x4000000;
					context.drawText(manager.getClient().textRenderer, Text.translatable("advancements.toast.cerulean"), 30, 11, color | alpha, false);
				} else {
					int alpha = MathHelper.floor(MathHelper.clamp((float) (startTime - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 0x4000000;
					int y = this.getHeight() / 2 - list.size() * 9 / 2;

					for (OrderedText orderedText : list) {
						context.drawText(manager.getClient().textRenderer, orderedText, 30, y, 0xffffff | alpha, false);
						y += 9;
					}
				}
			}

			context.drawItemWithoutEntity(advancementDisplay.getIcon(), 8, 8);
			cir.setReturnValue((double)startTime >= 5000.0 * manager.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.HIDE : Toast.Visibility.SHOW);
		}
	}
}
