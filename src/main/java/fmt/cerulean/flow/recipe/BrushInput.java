package fmt.cerulean.flow.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

public class BrushInput implements RecipeInput {
	public final PigmentInventory inventory;

	public BrushInput(PigmentInventory inventory) {
		this.inventory = inventory;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return null;
	}

	@Override
	public int getSize() {
		return 0;
	}
}
