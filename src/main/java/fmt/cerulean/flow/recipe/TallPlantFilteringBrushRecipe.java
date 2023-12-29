package fmt.cerulean.flow.recipe;

import java.util.function.Function;

import fmt.cerulean.flow.FlowState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TallPlantFilteringBrushRecipe implements BrushRecipe {
	private final CanvasRequirements canvas;
	private final Function<FlowState, FlowState> flowTransform;
	private final float wiltChance;

	public TallPlantFilteringBrushRecipe(CanvasRequirements canvas, Function<FlowState, FlowState> flowTransform, float wiltChance) {
		this.canvas = canvas;
		this.flowTransform = flowTransform;
		this.wiltChance = wiltChance;
	}

	@Override
	public int getRequiredFlowInputs() {
		return 1;
	}

	@Override
	public int getCraftTime() {
		return 20;
	}

	@Override
	public boolean canCraft(PigmentInventory inventory) {
		return canvas.canCraft(inventory.world, inventory.pos, inventory.flow);
	}

	@Override
	public void craft(PigmentInventory inventory) {
		World world = inventory.world;
		if (world.getRandom().nextFloat() <= wiltChance) {
			BlockPos top = inventory.pos;
			while (canvas.canCraft(inventory.world, top, inventory.flow)) {
				top = top.up();
			}
			world.breakBlock(top, false);
		}
	}

	@Override
	public FlowState getProcessedFlow(FlowState flow, int process) {
		return flowTransform.apply(flow);
	}
}
