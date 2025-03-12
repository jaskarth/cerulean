package fmt.cerulean.flow.recipe;

import fmt.cerulean.registry.CeruleanItemComponents;
import fmt.cerulean.registry.CeruleanItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;

import java.util.List;
import java.util.Set;

public class ImprintingRecipe implements BrushRecipe {
	private final CanvasRequirements canvas;

	public ImprintingRecipe(CanvasRequirements canvas) {
		this.canvas = canvas;
	}

	@Override
	public int getRequiredFlowInputs() {
		return 1;
	}

	@Override
	public int getCraftTime() {
		return 15;
	}

	@Override
	public boolean canCraft(PigmentInventory inventory) {
		if (!canvas.canCraft(inventory.world, inventory.pos, inventory.flow)) {
			return false;
		}
		return inventory.containsAll(List.of(Ingredient.ofItems(CeruleanItems.PHOTONEGATIVE), Ingredient.ofItems(Items.PAPER)));
	}

	@Override
	public void craft(PigmentInventory inventory) {
		inventory.killItems(i -> i.isOf(Items.PAPER), 1);
		ItemStack stack = inventory.find(i -> i.isOf(CeruleanItems.PHOTONEGATIVE));
		ItemStack res = new ItemStack(CeruleanItems.PHOTOGRAPH);

		res.set(CeruleanItemComponents.COLOR_TRIPLEX, stack.get(CeruleanItemComponents.COLOR_TRIPLEX));
		res.set(CeruleanItemComponents.PHOTO, stack.get(CeruleanItemComponents.PHOTO));

		inventory.spawnResult(res);
	}
}
