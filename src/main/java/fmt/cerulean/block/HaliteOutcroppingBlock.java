package fmt.cerulean.block;

import fmt.cerulean.block.base.Plasticloggable;
import fmt.cerulean.registry.CeruleanFluids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class HaliteOutcroppingBlock extends Block implements Plasticloggable {
	public static final DirectionProperty FACING = Properties.FACING;
	private final float height;
	private final float xzOffset;
	protected final VoxelShape northShape;
	protected final VoxelShape southShape;
	protected final VoxelShape eastShape;
	protected final VoxelShape westShape;
	protected final VoxelShape upShape;
	protected final VoxelShape downShape;

	public HaliteOutcroppingBlock(float height, float xzOffset, Settings settings) {
		super(settings);
		this.setDefaultState(this.getDefaultState().with(PLASTICLOGGED, false).with(FACING, Direction.UP));
		this.upShape = Block.createCuboidShape(xzOffset, 0.0, xzOffset, (16.0F - xzOffset), height, (16.0F - xzOffset));
		this.downShape = Block.createCuboidShape(
				xzOffset, (16.0F - height), xzOffset, (16.0F - xzOffset), 16.0, (16.0F - xzOffset)
		);
		this.northShape = Block.createCuboidShape(
				xzOffset, xzOffset, (16.0F - height), (16.0F - xzOffset), (16.0F - xzOffset), 16.0
		);
		this.southShape = Block.createCuboidShape(xzOffset, xzOffset, 0.0, (16.0F - xzOffset), (16.0F - xzOffset), height);
		this.eastShape = Block.createCuboidShape(0.0, xzOffset, xzOffset, height, (16.0F - xzOffset), (16.0F - xzOffset));
		this.westShape = Block.createCuboidShape(
				(16.0F - height), xzOffset, xzOffset, 16.0, (16.0F - xzOffset), (16.0F - xzOffset)
		);
		this.height = height;
		this.xzOffset = xzOffset;
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		Direction direction = state.get(FACING);
		return switch (direction) {
			case NORTH -> this.northShape;
			case SOUTH -> this.southShape;
			case EAST -> this.eastShape;
			case WEST -> this.westShape;
			case DOWN -> this.downShape;
			default -> this.upShape;
		};
	}

	@Override
	protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		Direction direction = state.get(FACING);
		BlockPos blockPos = pos.offset(direction.getOpposite());
		return world.getBlockState(blockPos).isSideSolidFullSquare(world, blockPos, direction);
	}

	@Override
	protected BlockState getStateForNeighborUpdate(
			BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos
	) {
		return direction == state.get(FACING).getOpposite() && !state.canPlaceAt(world, pos)
				? Blocks.AIR.getDefaultState()
				: super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		WorldAccess worldAccess = ctx.getWorld();
		BlockPos blockPos = ctx.getBlockPos();
		return this.getDefaultState().with(PLASTICLOGGED, worldAccess.getFluidState(blockPos).getFluid() == CeruleanFluids.POLYETHYLENE)
				.with(FACING, ctx.getSide());
	}

	@Override
	protected BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	@Override
	protected BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return state.get(PLASTICLOGGED) ? CeruleanFluids.POLYETHYLENE.getDefaultState() : super.getFluidState(state);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(PLASTICLOGGED, FACING);
	}
}
