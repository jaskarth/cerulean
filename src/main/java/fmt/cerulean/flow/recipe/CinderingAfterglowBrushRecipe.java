package fmt.cerulean.flow.recipe;

import fmt.cerulean.flow.FlowState;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CinderingAfterglowBrushRecipe implements BrushRecipe {
	public final CanvasRequirements canvas;
	public final boolean twicetwice;

	public CinderingAfterglowBrushRecipe(CanvasRequirements canvas, boolean twice) {
		this.canvas = canvas;
		this.twicetwice = twice;
	}

	@Override
	public int getCraftTime() {
		return 100;
	}

	@Override
	public int getRequiredFlowInputs() {
		return 1;
	}

	@Override
	public FlowState getProcessedFlow(FlowState flow, int process) {
		if (twicetwice && twicetwice) {
			return flow.brightened(1).brightened(0.4f);
		}
		return flow.brightened(0.5f);
	}

	@Override
	public boolean canCraft(PigmentInventory inventory) {
		return canvas.canCraft(inventory.world, inventory.pos, inventory.flow) && inventory.world.getBlockState(inventory.pos).get(CampfireBlock.LIT);
	}

	@Override
	public void craft(PigmentInventory inventory) {
		World world = inventory.world;
		BlockPos pos = inventory.pos;
		if (world.getBlockEntity(pos) instanceof CampfireBlockEntity cbe && !cbe.getItemsBeingCooked().stream().allMatch(ItemStack::isEmpty) && world.random.nextInt(2) == 0) {
			BlockState state = world.getBlockState(pos).with(CampfireBlock.LIT, false);
			world.setBlockState(pos, state);
			CampfireBlock.extinguish(null, world, pos, state);
		}
	}
}
