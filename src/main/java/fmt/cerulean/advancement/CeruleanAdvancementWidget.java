package fmt.cerulean.advancement;

import com.mojang.blaze3d.systems.RenderSystem;
import fmt.cerulean.Cerulean;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.advancement.PlacedAdvancement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.advancement.AdvancementTab;
import net.minecraft.client.gui.screen.advancement.AdvancementWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class CeruleanAdvancementWidget extends AdvancementWidget {
	private static final Identifier TITLE_BOX_TEXTURE = Identifier.ofVanilla("advancements/title_box");

	private final PlacedAdvancement advancement;

	public CeruleanAdvancementWidget(AdvancementTab tab, MinecraftClient client, PlacedAdvancement advancement, AdvancementDisplay display) {
		super(tab, client, advancement, display);
		this.advancement = advancement;
	}

	public void renderWidgets(DrawContext context, int x, int y) {
		Identifier id = advancement.getAdvancementEntry().id();
		if (!this.display.isHidden() || this.progress != null && this.progress.isDone() && !id.getPath().equals("cerulean/root")) {
			float f = this.progress == null ? 0.0F : this.progress.getProgressBarPercentage();
			if (f >= 1.0F) {
				context.drawTexture(Cerulean.id("textures/gui/star_frame_obtained.png"), x + this.x + 3, y + this.y, 30, 30, 0, 0, 26, 26, 26, 26);
				ItemStack stack = this.display.getIcon();

				context.drawItemWithoutEntity(stack, x + this.x + 10, y + this.y + 7);
			} else {
				context.drawTexture(Cerulean.id("textures/gui/star_frame_unobtained.png"), x + this.x + 3, y + this.y, 30, 30, 0, 0, 26, 26, 26, 26);
			}
		}

		for (AdvancementWidget advancementWidget : this.children) {
			advancementWidget.renderWidgets(context, x, y);
		}
	}

	@Override
	public boolean shouldRender(int originX, int originY, int mouseX, int mouseY) {
		if (advancement.getAdvancementEntry().id().getPath().equals("cerulean/root")) {
			return false;
		}
		float f = this.progress == null ? 0.0F : this.progress.getProgressBarPercentage();
		if (f < 1.0F) {
			return false;
		}

		return super.shouldRender(originX, originY, mouseX, mouseY);
	}

	public void drawTooltip(DrawContext context, int originX, int originY, float alpha, int x, int y) {
		Text text = this.progress == null ? null : this.progress.getProgressBarFraction();
		MinecraftClient client = MinecraftClient.getInstance();
		int textWidth = text == null ? 0 : client.textRenderer.getWidth(text);
		float progressPercent = this.progress == null ? 0.0F : this.progress.getProgressBarPercentage();
		int width = MathHelper.floor(progressPercent * (float)this.width);
		if (progressPercent >= 1.0F) {
			width = this.width / 2;
		} else if (width < 2) {
			width = this.width / 2;
		} else if (width > this.width - 2) {
			width = this.width / 2;
		}

		int aWidth = this.width - width;
		RenderSystem.enableBlend();
		int yOff = originY + this.y;
		int xOff = originX + this.x;

		int descriptionSize = 32 + this.description.size() * 9;
		if (!this.description.isEmpty()) {
			context.drawGuiTexture(TITLE_BOX_TEXTURE, xOff, yOff, this.width, descriptionSize + 6);
		}

		context.drawGuiTexture(Cerulean.id("gui/box_obtained_large"), 200, 25, 0, 0, xOff, yOff + 3, width, 25);
		context.drawGuiTexture(Cerulean.id("gui/box_obtained_large"), 200, 25, 200 - aWidth, 0, xOff + width, yOff + 3, aWidth, 25);
		context.drawTexture(Cerulean.id("textures/gui/star_frame_obtained.png"), originX + this.x + 3, originY + this.y, 30, 30, 0, 0, 26, 26, 26, 26);
		context.drawTextWithShadow(client.textRenderer, this.title, originX + this.x + 32, originY + this.y + 9 + 3, -1);
		if (text != null) {
			context.drawTextWithShadow(client.textRenderer, text, originX + this.x + this.width - textWidth - 5, originY + this.y + 9, Colors.WHITE);
		}

		for (int i = 0; i < this.description.size(); i++) {
			context.drawText(client.textRenderer, (OrderedText) this.description.get(i), xOff + 5, originY + this.y + 9 + 17 + i * 9 + 6, 0xffaaaaaa, false);
		}

		context.drawItemWithoutEntity(this.display.getIcon(), originX + this.x + 10, originY + this.y + 7);
	}
}
