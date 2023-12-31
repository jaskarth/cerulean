package fmt.cerulean.block;

import fmt.cerulean.block.entity.PipeBlockEntity;
import fmt.cerulean.registry.CeruleanBlockEntities;
import fmt.cerulean.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ConnectingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class PipeBlock extends Block implements BlockEntityProvider, Waterloggable {
	public static final BooleanProperty UP = Properties.UP;
	public static final BooleanProperty DOWN = Properties.DOWN;
	public static final BooleanProperty NORTH = Properties.NORTH;
	public static final BooleanProperty EAST = Properties.EAST;
	public static final BooleanProperty SOUTH = Properties.SOUTH;
	public static final BooleanProperty WEST = Properties.WEST;
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	private final VoxelShape[] SHAPE_CACHE = bakeShapes();

	public PipeBlock(Settings settings) {
		super(settings);
		this.setDefaultState(stateManager.getDefaultState()
			.with(UP, false)
			.with(DOWN, false)
			.with(NORTH, false)
			.with(EAST, false)
			.with(SOUTH, false)
			.with(WEST, false)
			.with(WATERLOGGED, false)
		);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
			WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(WATERLOGGED)) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return getState(world, pos);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getState(ctx.getWorld(), ctx.getBlockPos());
	}

	private BlockState getState(WorldAccess world, BlockPos pos) {
		FluidState fluid = world.getFluidState(pos);
		BlockState state = this.getDefaultState().with(WATERLOGGED, fluid.isOf(Fluids.WATER));
		Direction lastDir = null;
		int connections = 0;
		for (Direction dir : Util.DIRECTIONS) {
			if (canConnect(world.getBlockState(pos.offset(dir)), dir.getOpposite())) {
				connections++;
				lastDir = dir;
				state = state.with(ConnectingBlock.FACING_PROPERTIES.get(dir), true);
			}
		}
		if (connections == 1) {
			state = state.with(ConnectingBlock.FACING_PROPERTIES.get(lastDir.getOpposite()), true);
		}
		return state;
	}

	public static boolean canConnect(BlockState state, Direction dir) {
		return isPipe(state) || (state.getBlock() instanceof WellBlock && dir == state.get(WellBlock.FACING));
	}

	public static boolean isPipe(BlockState state) {
		return state.getBlock() instanceof PipeBlock;
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new PipeBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (type == CeruleanBlockEntities.PIPE) {
			if (world.isClient) {
				return (w, p, s, be) -> ((PipeBlockEntity) be).clientTick(w, p, s);
			} else {
				return (w, p, s, be) -> ((PipeBlockEntity) be).tick(w, p, s);
			}
		}
		return null;
	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST, WATERLOGGED);
	}

	@Override
	@SuppressWarnings("deprecation")
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(true) : super.getFluidState(state);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE_CACHE[(state.get(UP) ? 1 : 0)
			| (state.get(DOWN) ? 2 : 0)
			| (state.get(NORTH) ? 4 : 0)
			| (state.get(EAST) ? 8 : 0)
			| (state.get(SOUTH) ? 16 : 0)
			| (state.get(WEST) ? 32 : 0)
		];
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (newState.getBlock() instanceof PipeBlock) {
			return;
		}

		super.onStateReplaced(state, world, pos, newState, moved);
	}

	private VoxelShape[] bakeShapes() {
		VoxelShape[] shapes = new VoxelShape[64];
		float px0 = 0 / 16f;
		float px2 = 2 / 16f;
		float px4 = 4 / 16f;
		float px12 = 12 / 16f;
		float px14 = 14 / 16f;
		float px16 = 16 / 16f;
		float riml = 3 / 16f;
		float rimh = 13 / 16f;
		for (int i = 0; i < shapes.length; i++) {
			BlockState state = getDefaultState()
				.with(UP, (i & 1) != 0)
				.with(DOWN, (i & 2) != 0)
				.with(NORTH, (i & 4) != 0)
				.with(EAST, (i & 8) != 0)
				.with(SOUTH, (i & 16) != 0)
				.with(WEST, (i & 32) != 0);
			VoxelShape shape = VoxelShapes.cuboid(px4, px4, px4, px12, px12, px12);
			if (state.get(UP)) {
				shape = VoxelShapes.union(shape,
					VoxelShapes.cuboid(px4, px12, px4, px12, px14, px12),
					VoxelShapes.cuboid(riml, px14, riml, rimh, px16, rimh)
				);
			}
			if (state.get(DOWN)) {
				shape = VoxelShapes.union(shape,
					VoxelShapes.cuboid(px4, px2, px4, px12, px4, px12),
					VoxelShapes.cuboid(riml, px0, riml, rimh, px2, rimh)
				);
			}
			if (state.get(NORTH)) {
				shape = VoxelShapes.union(shape,
					VoxelShapes.cuboid(px4, px4, px2, px12, px12, px4),
					VoxelShapes.cuboid(riml, riml, px0, rimh, rimh, px2)
				);
			}
			if (state.get(SOUTH)) {
				shape = VoxelShapes.union(shape,
					VoxelShapes.cuboid(px4, px4, px12, px12, px12, px14),
					VoxelShapes.cuboid(riml, riml, px14, rimh, rimh, px16)
				);
			}
			if (state.get(WEST)) {
				shape = VoxelShapes.union(shape,
					VoxelShapes.cuboid(px2, px4, px4, px4, px12, px12),
					VoxelShapes.cuboid(px0, riml, riml, px2, rimh, rimh)
				);
			}
			if (state.get(EAST)) {
				shape = VoxelShapes.union(shape,
					VoxelShapes.cuboid(px12, px4, px4, px14, px12, px12),
					VoxelShapes.cuboid(px14, riml, riml, px16, rimh, rimh)
				);
			}
			shapes[i] = shape;
		}
		return shapes;
	}
}
