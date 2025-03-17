package fmt.cerulean.advancement;

import com.mojang.blaze3d.systems.RenderSystem;
import fmt.cerulean.Cerulean;
import fmt.cerulean.world.CeruleanDimensions;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.advancement.PlacedAdvancement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.advancement.AdvancementTab;
import net.minecraft.client.gui.screen.advancement.AdvancementWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.*;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class CeruleanAdvancementWidget extends AdvancementWidget {
	private static final int[] SPLIT_OFFSET_CANDIDATES = new int[]{0, 10, -10, 25, -25};

	private static final Identifier TITLE_BOX_TEXTURE = Identifier.ofVanilla("advancements/title_box");

	private final PlacedAdvancement advancement;
	public List<OrderedText> altDescription;
	private int width2;

	public CeruleanAdvancementWidget(AdvancementTab tab, MinecraftClient client, PlacedAdvancement advancement, AdvancementDisplay display) {
		super(tab, client, advancement, display);
		this.advancement = advancement;

		CeruleanAdvancementData.Data data = CeruleanAdvancementData.get(advancement.getAdvancementEntry().id());
		int i = this.getProgressWidth();
		int j = 29 + client.textRenderer.getWidth(this.title) + i;

		this.width2 = this.width;

		if (data.altDesc() != null) {
			this.altDescription = Language.getInstance()
					.reorder(this.wrapDescription(
							Texts.setStyleIfAbsent(Text.translatable(data.altDesc()).copy(),
									Style.EMPTY.withColor(display.getFrame().getTitleFormat())), j));

			j = this.width - 8;

			for (OrderedText orderedText : this.altDescription) {
				j = Math.max(j, client.textRenderer.getWidth(orderedText));
			}

			this.width2 = j + 5 + 3;
		}
	}

	private int getProgressWidth() {
		int i = this.advancement.getAdvancement().requirements().getLength();
		if (i <= 1) {
			return 0;
		} else {
			int j = 8;
			Text text = Text.translatable("advancements.progress", i, i);
			return MinecraftClient.getInstance().textRenderer.getWidth(text) + 8;
		}
	}

	private static float getMaxWidth(TextHandler textHandler, List<StringVisitable> lines) {
		return (float)lines.stream().mapToDouble(textHandler::getWidth).max().orElse(0.0);
	}

	private List<StringVisitable> wrapDescription(Text text, int width) {
		TextHandler textHandler = MinecraftClient.getInstance().textRenderer.getTextHandler();
		List<StringVisitable> list = null;
		float f = Float.MAX_VALUE;

		for (int i : SPLIT_OFFSET_CANDIDATES) {
			List<StringVisitable> list2 = textHandler.wrapLines(text, width - i, Style.EMPTY);
			float g = Math.abs(getMaxWidth(textHandler, list2) - (float)width);
			if (g <= 10.0F) {
				return list2;
			}

			if (g < f) {
				f = g;
				list = list2;
			}
		}

		return list;
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
		int thisWidth = this.width2;
		int width = MathHelper.floor(progressPercent * (float) thisWidth);
		if (progressPercent >= 1.0F) {
			width = thisWidth / 2;
		} else if (width < 2) {
			width = thisWidth / 2;
		} else if (width > thisWidth - 2) {
			width = thisWidth / 2;
		}

		int aWidth = thisWidth - width;
		RenderSystem.enableBlend();
		int yOff = originY + this.y;
		int xOff = originX + this.x;

		List<OrderedText> curDesc = this.description;

		if (altDescription != null && !(client.world.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.SKIES))) {
			curDesc = this.altDescription;
		}

		int descriptionSize = 32 + curDesc.size() * 9;
		if (!curDesc.isEmpty()) {
			context.drawGuiTexture(TITLE_BOX_TEXTURE, xOff, yOff, thisWidth, descriptionSize + 6);
		}

		context.drawGuiTexture(Cerulean.id("gui/box_obtained_large"), 200, 25, 0, 0, xOff, yOff + 3, width, 25);
		context.drawGuiTexture(Cerulean.id("gui/box_obtained_large"), 200, 25, 200 - aWidth, 0, xOff + width, yOff + 3, aWidth, 25);
		context.drawTexture(Cerulean.id("textures/gui/star_frame_obtained.png"), originX + this.x + 3, originY + this.y, 30, 30, 0, 0, 26, 26, 26, 26);
		context.drawTextWithShadow(client.textRenderer, this.title, originX + this.x + 32, originY + this.y + 9 + 3, -1);
		if (text != null) {
			context.drawTextWithShadow(client.textRenderer, text, originX + this.x + thisWidth - textWidth - 5, originY + this.y + 9, Colors.WHITE);
		}

		for (int i = 0; i < curDesc.size(); i++) {
			context.drawText(client.textRenderer, (OrderedText) curDesc.get(i), xOff + 5, originY + this.y + 9 + 17 + i * 9 + 6, 0xffaaaaaa, false);
		}

		context.drawItemWithoutEntity(this.display.getIcon(), originX + this.x + 10, originY + this.y + 7);
	}
}
