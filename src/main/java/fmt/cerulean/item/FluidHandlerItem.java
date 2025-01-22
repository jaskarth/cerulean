package fmt.cerulean.item;

import fmt.cerulean.registry.CeruleanItemComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class FluidHandlerItem extends Item {
	public FluidHandlerItem(Settings settings) {
		super(settings);
	}

	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return stack.getOrDefault(CeruleanItemComponents.FLUID_TYPE, null) != null;
	}

	public int getAmount(ItemStack stack) {
		return MathHelper.clamp(stack.getOrDefault(CeruleanItemComponents.FLUID_AMOUNT, 0), 0, 10_000);
	}

	@Override
	public int getItemBarStep(ItemStack stack) {
		return MathHelper.clamp(Math.round((((float) getAmount(stack) * 13.0F) / 10_000F)), 0, 13);
	}

	@Override
	public int getItemBarColor(ItemStack stack) {
		return MathHelper.packRgb(0.9F, 0.9F, 0.9F);
	}
}
