package fmt.cerulean.flow.recipe;

import java.util.List;

import com.google.common.collect.Lists;

import fmt.cerulean.flow.FlowState;
import fmt.cerulean.flow.FlowResource.Color;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UnblightBrushRecipe implements BrushRecipe {
	private static final int HORIZONTAL_SEARCH = 6;
	private static final int VERTICAL_SEARCH = 2;
	public final CanvasRequirements canvas;
	public final CropBlock block;
	public final Color color;

	public UnblightBrushRecipe(CanvasRequirements canvas, CropBlock block, Color color) {
		this.canvas = canvas;
		this.block = block;
		this.color = color;
	}

	@Override
	public int getCraftTime() {
		return 80;
	}

	@Override
	public int getRequiredFlowInputs() {
		return 1;
	}

	@Override
	public FlowState getProcessedFlow(FlowState flow, int process) {
		return flow.colored(color);
	}

	@Override
	public boolean canCraft(PigmentInventory inventory) {
		if (!canvas.canCraft(inventory.world, inventory.pos, inventory.flow)) {
			return false;
		}
		if (inventory.recipeProgress == 0) {
			World world = inventory.world;
			BlockPos origin = inventory.pos;
			BlockPos.Mutable pos = new BlockPos.Mutable();
			for (int x = origin.getX() - HORIZONTAL_SEARCH; x < origin.getX() + HORIZONTAL_SEARCH; x++) {
				for (int z = origin.getZ() - HORIZONTAL_SEARCH; z < origin.getZ() + HORIZONTAL_SEARCH; z++) {
					for (int y = origin.getY() - VERTICAL_SEARCH; y < origin.getY() + VERTICAL_SEARCH; y++) {
						pos.set(x, y, z);
						BlockState state = world.getBlockState(pos);
						if (state.isOf(block) && !block.isMature(state)) {
							return true;
						}
					}
				}
			}
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void craft(PigmentInventory inventory) {
		World world = inventory.world;
		BlockPos origin = inventory.pos;
		BlockPos.Mutable pos = new BlockPos.Mutable();
		List<BlockPos> candidates = Lists.newArrayList();
		for (int x = origin.getX() - HORIZONTAL_SEARCH; x < origin.getX() + HORIZONTAL_SEARCH; x++) {
			for (int z = origin.getZ() - HORIZONTAL_SEARCH; z < origin.getZ() + HORIZONTAL_SEARCH; z++) {
				for (int y = origin.getY() - VERTICAL_SEARCH; y < origin.getY() + VERTICAL_SEARCH; y++) {
					pos.set(x, y, z);
					BlockState state = world.getBlockState(pos);
					if (state.isOf(block) && !block.isMature(state)) {
						candidates.add(pos.toImmutable());
					}
				}
			}
		}
		if (!candidates.isEmpty()) {
			BlockPos growUp = candidates.get(world.random.nextInt(candidates.size()));
			block.applyGrowth(world, growUp, world.getBlockState(growUp));
		}
	}
}
