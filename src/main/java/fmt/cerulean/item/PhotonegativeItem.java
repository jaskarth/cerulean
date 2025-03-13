package fmt.cerulean.item;

import fmt.cerulean.flow.FlowResource;
import fmt.cerulean.item.component.ColorTriplex;
import fmt.cerulean.item.component.PhotoComponent;
import fmt.cerulean.registry.CeruleanItemComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Locale;

public class PhotonegativeItem extends GlitterItem {
	public PhotonegativeItem(Settings settings) {
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		PhotoComponent photo = stack.get(CeruleanItemComponents.PHOTO);
		if (photo != null) {
			tooltip.add(Text.literal(Formatting.DARK_GRAY + "Photo #" + photo.id()));

		}

		super.appendTooltip(stack, context, tooltip, type);
	}
}
