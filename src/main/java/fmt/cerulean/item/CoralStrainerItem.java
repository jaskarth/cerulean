package fmt.cerulean.item;

import fmt.cerulean.registry.CeruleanItemComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class CoralStrainerItem extends Item {
	public CoralStrainerItem(Settings settings) {
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		if (stack.contains(CeruleanItemComponents.ITEM_STACK)) {
			ItemStack st = stack.get(CeruleanItemComponents.ITEM_STACK);
			tooltip.add(Text.literal(Formatting.GRAY + "Dreaming of " + Formatting.RESET).append(st.getName()).append(Formatting.GRAY + "..."));
		} else {
			tooltip.add(Text.of(Formatting.GRAY + "Empty...?"));
		}
	}
}
