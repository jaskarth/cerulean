package fmt.cerulean.block;

import net.minecraft.block.*;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.function.BiConsumer;

public class FuchsiaGapDoorBlock extends TrapdoorBlock {
	public FuchsiaGapDoorBlock(BlockSetType type, Settings settings) {
		super(type, settings);
	}

	@Override
	protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
		if (!world.isClient) {
			boolean bl = world.isReceivingRedstonePower(pos);
			if (bl != state.get(POWERED)) {

				this.playToggleSound(null, world, pos, bl);

				world.setBlockState(pos, state.with(POWERED, bl), Block.NOTIFY_LISTENERS);
				if (state.get(WATERLOGGED)) {
					world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
				}
			}
		}
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState blockState = this.getDefaultState();
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		Direction direction = ctx.getSide();
		if (!ctx.canReplaceExisting() && direction.getAxis().isHorizontal()) {
			blockState = blockState.with(FACING, direction).with(HALF, ctx.getHitPos().y - (double)ctx.getBlockPos().getY() > 0.5 ? BlockHalf.TOP : BlockHalf.BOTTOM);
		} else {
			blockState = blockState.with(FACING, ctx.getHorizontalPlayerFacing().getOpposite()).with(HALF, direction == Direction.UP ? BlockHalf.BOTTOM : BlockHalf.TOP);
		}

		if (ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos())) {
			blockState = blockState.with(POWERED, true);
		}

		return blockState.with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
	}

	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		// flip
		BlockState blockState = state.cycle(OPEN);
		world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS);
		if (blockState.get(WATERLOGGED)) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}

		this.playToggleSound(player, world, pos, blockState.get(OPEN));

		return ActionResult.success(world.isClient);
	}

	@Override
	protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (state.get(TrapdoorBlock.POWERED)) {
			return VoxelShapes.empty();
		}

		return super.getCollisionShape(state, world, pos, context);
	}
}
