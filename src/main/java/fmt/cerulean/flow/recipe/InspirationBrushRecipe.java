package fmt.cerulean.flow.recipe;

import java.util.function.Predicate;

import fmt.cerulean.flow.FlowState;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

public class InspirationBrushRecipe implements BrushRecipe {
	public final CanvasRequirements canvas;
	public final int time;
	public final Predicate<ItemStack> input;
	public final Output output;
	public final Ingredient misleadingInput;
	public final ItemStack misleadingOutput;

	public InspirationBrushRecipe(CanvasRequirements canvas, int time, Ingredient input, ItemStack output) {
		this.canvas = canvas;
		this.time = time;
		this.input = input;
		this.output = (a, b) -> output;
		this.misleadingInput = input;
		this.misleadingOutput = output;
	}

	@Override
	public int getCraftTime() {
		return time;
	}

	@Override
	public int getRequiredFlowInputs() {
		return 2;
	}

	@Override
	public boolean canCraft(PigmentInventory inventory) {
		return canvas.canCraft(inventory.world, inventory.pos, inventory.flow, inventory.opposing) && inventory.containsAny(input);
	}

	@Override
	public void craft(PigmentInventory inventory) {
		inventory.killItems(input, 1);
		inventory.spawnResult(output.getOutput(inventory.flow, inventory.opposing));
	}

	public static class Uninspired extends InspirationBrushRecipe {

		public Uninspired(CanvasRequirements canvas, int time, Ingredient input, ItemStack output) {
			super(canvas, time, input, output);
		}


		@Override
		public int getRequiredFlowInputs() {
			return 1;
		}
	
		@Override
		public boolean canCraft(PigmentInventory inventory) {
			return canvas.canCraft(inventory.world, inventory.pos, inventory.flow) && inventory.containsAny(input);
		}
	}

	public static interface Output {

		ItemStack getOutput(FlowState a, FlowState b);
	}
}
