package fmt.cerulean.flow.recipe;

import fmt.cerulean.flow.FlowState;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public interface BrushRecipe extends Recipe<BrushInput> {

	int getCraftTime();

	int getRequiredFlowInputs();

	boolean canCraft(PigmentInventory inventory);

	void craft(PigmentInventory inventory);

	default FlowState getProcessedFlow(FlowState flow, int process) {
		return FlowState.NONE;
	}

	@Override
	default boolean matches(BrushInput input, World world) {
		if (input.inventory.opposing.empty() != (getRequiredFlowInputs() == 1)) {
			return false;
		}
		return canCraft(input.inventory);
	}

	@Override
	default boolean fits(int width, int height) {
		return true;
	}

	@Override
	default RecipeSerializer<?> getSerializer() {
		throw new UnsupportedOperationException();
	}

	@Override
	default RecipeType<?> getType() {
		throw new UnsupportedOperationException();
	}

	@Override
	default ItemStack craft(BrushInput input, RegistryWrapper.WrapperLookup lookup) {
		return ItemStack.EMPTY;
	}

	@Override
	default ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
		return ItemStack.EMPTY;
	}
}
