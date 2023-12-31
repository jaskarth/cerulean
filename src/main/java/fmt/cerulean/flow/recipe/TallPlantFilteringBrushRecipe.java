package fmt.cerulean.flow.recipe;

import fmt.cerulean.flow.FlowResource.Color;
import fmt.cerulean.flow.FlowState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TallPlantFilteringBrushRecipe implements BrushRecipe {
	public final CanvasRequirements canvas;
	public final Color color;
	public final float wiltChance;

	public TallPlantFilteringBrushRecipe(CanvasRequirements canvas, Color color, float wiltChance) {
		this.canvas = canvas;
		this.color = color;
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
		if (wiltChance > 0 && world.getRandom().nextFloat() <= wiltChance) {
			BlockPos top = inventory.pos;
			while (canvas.canCraft(inventory.world, top.up(), inventory.flow)) {
				top = top.up();
			}
			world.breakBlock(top, false);
		}
	}

	@Override
	public FlowState getProcessedFlow(FlowState flow, int process) {
		if (wiltChance <= 0) {
			return flow.coloredDimmer(color).scaled(0.95f);
		} else {
			return flow.colored(color);
		}
	}
}
