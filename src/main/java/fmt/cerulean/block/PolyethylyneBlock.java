package fmt.cerulean.block;

import fmt.cerulean.block.base.Plasticloggable;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class PolyethylyneBlock extends Block implements Plasticloggable {
	private final Fluid fluid;

	public PolyethylyneBlock(Fluid fluid, Settings settings) {
		super(settings);
		this.fluid = fluid;
	}

	@Override
	protected BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return fluid.getDefaultState();
	}

	@Override
	protected boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
		return stateFrom.getFluidState().getFluid().matchesType(this.fluid);
	}

	@Override
	protected List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
		return Collections.emptyList();
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	public boolean tryDrainPlastic(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos, BlockState state) {
		world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL_AND_REDRAW);

		return true;
	}

	@Override
	public boolean tryFillWithPlastic(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
		return false;
	}
}
