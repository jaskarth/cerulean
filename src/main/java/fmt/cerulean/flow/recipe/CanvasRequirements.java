package fmt.cerulean.flow.recipe;

import java.util.Set;

import fmt.cerulean.flow.FlowResource.Brightness;
import fmt.cerulean.flow.FlowResource.Color;
import fmt.cerulean.flow.FlowState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CanvasRequirements {
	public Set<Block> validBlocks;
	public Set<BlockState> validBlockStates;
	public Set<Color> validColors;
	public Set<Brightness> validBrightnesses;
	public Set<Color> validOpposingColors;
	public Set<Brightness> validOpposingBrightnesses;

	private CanvasRequirements(Set<Block> validBlocks, Set<BlockState> validBlockStates,
			Set<Color> validColors, Set<Brightness> validBrightnesses,
			Set<Color> validOpposingColors, Set<Brightness> validOpposingBrightnesses) {
		this.validBlocks = validBlocks;
		this.validBlockStates = validBlockStates;
		this.validColors = validColors;
		this.validBrightnesses = validBrightnesses;
		this.validOpposingColors = validOpposingColors;
		this.validOpposingBrightnesses = validOpposingBrightnesses;
	}

	public static CanvasRequirements of(Set<Block> blocks, Set<Color> validColors, Set<Brightness> validBrightnesses) {
		return new CanvasRequirements(blocks, Set.of(), validColors, validBrightnesses, Set.of(), Set.of());
	}

	public static CanvasRequirements of(Block block, Set<Color> validColors, Set<Brightness> validBrightnesses) {
		return new CanvasRequirements(Set.of(block), Set.of(), validColors, validBrightnesses, Set.of(), Set.of());
	}

	public static CanvasRequirements of(BlockState state, Set<Color> validColors, Set<Brightness> validBrightnesses) {
		return new CanvasRequirements(Set.of(), Set.of(state), validColors, validBrightnesses, Set.of(), Set.of());
	}

	public static CanvasRequirements of(Block block, Set<Color> validColors, Set<Brightness> validBrightnesses, Set<Color> validOpposingColors, Set<Brightness> validOpposingBrightnesses) {
		return new CanvasRequirements(Set.of(block), Set.of(), validColors, validBrightnesses, validOpposingColors, validOpposingBrightnesses);
	}

	public static CanvasRequirements of(Set<Color> validColors, Set<Brightness> validBrightnesses) {
		return new CanvasRequirements(Set.of(), Set.of(), validColors, validBrightnesses, Set.of(), Set.of());
	}

	public static CanvasRequirements of(Set<Color> validColors, Set<Brightness> validBrightnesses, Set<Color> validOpposingColors, Set<Brightness> validOpposingBrightnesses) {
		return new CanvasRequirements(Set.of(), Set.of(), validColors, validBrightnesses, validOpposingColors, validOpposingBrightnesses);
	}

	public boolean canCraft(World world, BlockPos pos, FlowState first, FlowState second) {
		if (validOpposingBrightnesses.contains(first.resource().getBrightness()) && validOpposingColors.contains(first.resource().getColor())) {
			if (canCraft(world, pos, second)) {
				return true;
			}
		}
		if (validOpposingBrightnesses.contains(second.resource().getBrightness()) && validOpposingColors.contains(second.resource().getColor())) {
			if (canCraft(world, pos, first)) {
				return true;
			}
		}
		return false;
	}

	public boolean canCraft(World world, BlockPos pos, FlowState flow) {
		if (validBrightnesses.contains(flow.resource().getBrightness()) && validColors.contains(flow.resource().getColor())) {
			BlockState state = world.getBlockState(pos);
			if (validBlockStates.isEmpty() && validBlocks.isEmpty()) {
				return true;
			}
			if (validBlockStates.contains(state) || validBlocks.contains(state.getBlock())) {
				return true;
			}
		}
		return false;
	}
}
