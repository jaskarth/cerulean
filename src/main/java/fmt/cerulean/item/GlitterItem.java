package fmt.cerulean.item;

import fmt.cerulean.flow.FlowResource;
import fmt.cerulean.item.component.ColorTriplex;
import fmt.cerulean.registry.CeruleanItemComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Locale;

public class GlitterItem extends Item {
	public GlitterItem(Settings settings) {
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		ColorTriplex color = stack.get(CeruleanItemComponents.COLOR_TRIPLEX);

		if (color != null) {
			if (!color.a().isEmpty()) {
				tooltip.add(Text.literal("- " + name(color.a().get())).withColor(color.a().get().toRGB()));
			}
			if (!color.b().isEmpty()) {
				tooltip.add(Text.literal("- " + name(color.b().get())).withColor(color.b().get().toRGB()));
			}
			if (!color.c().isEmpty()) {
				tooltip.add(Text.literal("- " + name(color.c().get())).withColor(color.c().get().toRGB()));
			}
		}

		super.appendTooltip(stack, context, tooltip, type);
	}

	private static String name(FlowResource.Color c) {
		return c.name.substring(0, 1).toUpperCase(Locale.ROOT) + c.name.substring(1);
	}
}
