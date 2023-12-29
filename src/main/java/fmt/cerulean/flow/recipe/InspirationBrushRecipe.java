package fmt.cerulean.flow.recipe;

import java.util.function.Predicate;

import fmt.cerulean.flow.FlowState;
import net.minecraft.item.ItemStack;

public class InspirationBrushRecipe implements BrushRecipe {
	protected final CanvasRequirements canvas;
	protected final int time;
	protected final Predicate<ItemStack> input;
	protected final Output output;

	public InspirationBrushRecipe(CanvasRequirements canvas, int time, Predicate<ItemStack> input, Output output) {
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
		return canvas.canCraft(inventory.world, inventory.pos, inventory.flow, inventory.opposing) && inventory.containsAny(input);
	}

	@Override
	public void craft(PigmentInventory inventory) {
		inventory.killItems(input, 1);
		inventory.spawnResult(output.getOutput(inventory.flow, inventory.opposing));
	}

	public static class Uninspired extends InspirationBrushRecipe {

		public Uninspired(CanvasRequirements canvas, int time, Predicate<ItemStack> input, Output output) {
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
