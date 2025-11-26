package fmt.cerulean.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import fmt.cerulean.Cerulean;
import fmt.cerulean.registry.CeruleanItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class PhotographicTransformationRecipe extends BasicEmiRecipe {
	private static final EmiTexture QUESTION = new EmiTexture(Cerulean.id("textures/gui/question.png"), 0, 0, 16, 16, 16, 16, 16, 16);
	public final List<OrderedText> info;

	public PhotographicTransformationRecipe(Identifier id, @Nullable EmiStack input, Text info) {
		super(CeruleanEmiPlugin.PHOTOGRAPHIC_TRANSFORMATION, id, 100, 39 + (EmiBrushRecipe.split(info).size() * 12));
		catalysts.add(EmiStack.of(CeruleanItems.PHOTOGRAPH));
		if (input != null) {
			inputs.add(input);
		}
		this.info = EmiBrushRecipe.split(info);
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		int cx = getDisplayWidth() / 2 - 9;
		widgets.addSlot(EmiStack.of(CeruleanItems.PHOTOGRAPH), cx, 0);
		widgets.addTexture(EmiTexture.EMPTY_ARROW, cx - 4, 18 + 1);
		if (inputs.size() == 0) {
			widgets.addTexture(QUESTION, cx - 30, 18);
		} else {
			widgets.addSlot(inputs.get(0), cx - 30, 18);
		}

		widgets.addTexture(QUESTION, cx + 28, 18);

		MinecraftClient client = MinecraftClient.getInstance();
		int y = 39;
		for (OrderedText i : info) {
			int tw = client.textRenderer.getWidth(i);
			widgets.addText(i, (getDisplayWidth() - tw) / 2, y, 0xFFFFFFFF, true);
			y += 12;
		}
	}

	@Override
	public boolean supportsRecipeTree() {
		return false;
	}
}
