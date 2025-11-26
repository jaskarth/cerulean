package fmt.cerulean.flow.recipe;

import fmt.cerulean.registry.CeruleanItemComponents;
import fmt.cerulean.registry.CeruleanItems;
import net.minecraft.item.ItemStack;

public class StrainingBrushRecipe implements BrushRecipe {
	public final CanvasRequirements canvas;

	public StrainingBrushRecipe(CanvasRequirements canvas) {
		this.canvas = canvas;
	}

	@Override
	public int getCraftTime() {
		return 15;
	}

	@Override
	public int getRequiredFlowInputs() {
		return 1;
	}

	@Override
	public boolean canCraft(PigmentInventory inventory) {
		if (!canvas.canCraft(inventory.world, inventory.pos, inventory.flow)) {
			return false;
		}

		if (inventory.size() < 2) {
			return false;
		}

		for (int i = 0; i < inventory.size(); i++) {
			if (inventory.getStack(i).isOf(CeruleanItems.CORAL_STRAINER)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void craft(PigmentInventory inventory) {
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = inventory.getStack(i);
			ItemStack copy = stack.copy();
			if (stack.isOf(CeruleanItems.CORAL_STRAINER)) {
				inventory.killItems(st -> st == stack, 1);

				// Probably fine?
				copy.set(CeruleanItemComponents.ITEM_STACK, inventory.getStack(i == 0 ? 1 : 0));

				inventory.spawnResult(copy);
			}
		}
	}
}
