package fmt.cerulean.flow.recipe;

import java.util.List;

import fmt.cerulean.flow.FlowState;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

public class InspirationBrushRecipe implements BrushRecipe {
	public final CanvasRequirements canvas;
	public final int time;
	public final List<Ingredient> input;
	public final ItemStack output;

	public InspirationBrushRecipe(CanvasRequirements canvas, int time, Ingredient input, ItemStack output) {
		this(canvas, time, List.of(input), output);
	}

	public InspirationBrushRecipe(CanvasRequirements canvas, int time, List<Ingredient> input, ItemStack output) {
		this.canvas = canvas;
		this.time = time;
		this.input = input;
		this.output = output;
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
		return canvas.canCraft(inventory.world, inventory.pos, inventory.flow, inventory.opposing) && inventory.containsAll(input);
	}

	@Override
	public void craft(PigmentInventory inventory) {
		for (Ingredient i : input) {
			inventory.killItems(i, 1);
		}
		inventory.spawnResult(output.copy());
	}

	public static class Uninspired extends InspirationBrushRecipe {

		public Uninspired(CanvasRequirements canvas, int time, Ingredient input, ItemStack output) {
			super(canvas, time, List.of(input), output);
			if (!canvas.validOpposingBrightnesses.isEmpty() || !canvas.validOpposingColors.isEmpty()) {
				throw new IllegalStateException("Inspiration is everywhere if you see it");
			}
		}

		public Uninspired(CanvasRequirements canvas, int time, List<Ingredient> input, ItemStack output) {
			super(canvas, time, input, output);
			if (!canvas.validOpposingBrightnesses.isEmpty() || !canvas.validOpposingColors.isEmpty()) {
				throw new IllegalStateException("Inspiration is everywhere if you see it");
			}
		}


		@Override
		public int getRequiredFlowInputs() {
			return 1;
		}
	
		@Override
		public boolean canCraft(PigmentInventory inventory) {
			return canvas.canCraft(inventory.world, inventory.pos, inventory.flow) && inventory.containsAll(input);
		}
	}

	public static interface Output {

		ItemStack getOutput(FlowState a, FlowState b);
	}
}
