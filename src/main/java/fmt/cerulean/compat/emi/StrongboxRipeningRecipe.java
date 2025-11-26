package fmt.cerulean.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import fmt.cerulean.Cerulean;
import fmt.cerulean.registry.CeruleanItems;
import fmt.cerulean.world.CeruleanDimensions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;

public class StrongboxRipeningRecipe extends BasicEmiRecipe {

	public StrongboxRipeningRecipe(Identifier id, EmiStack input, EmiStack output) {
		super(CeruleanEmiPlugin.STRONGBOX_RIPENING, id, 100, 30 + (2 * 12));
		inputs.add(input);
		outputs.add(output);
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		int cx = getDisplayWidth() / 2 - 9;
		widgets.addTexture(EmiTexture.EMPTY_ARROW, cx - 4, 9 + 1);
		widgets.addSlot(inputs.get(0), cx - 30, 9);
		widgets.addSlot(outputs.get(0), cx + 28, 9);

		MinecraftClient client = MinecraftClient.getInstance();

		Text text;
		if (client.world.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.SKIES)) {
			text = Text.translatable("info.cerulean.strongbox.recipe.unknowable." + client.world.random.nextInt(4));
		} else {
			text = Text.translatable("info.cerulean.strongbox.recipe.knowable." + client.world.random.nextInt(4));
		}

		List<OrderedText> info = EmiBrushRecipe.split(text);

		int y = 30;
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
