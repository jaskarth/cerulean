package fmt.cerulean.block;

import java.util.List;

import com.mojang.serialization.MapCodec;

import fmt.cerulean.block.base.Plasticloggable;
import fmt.cerulean.block.entity.AddressPlaqueBlockEntity;
import fmt.cerulean.registry.CeruleanBlockEntities;
import fmt.cerulean.registry.CeruleanFluids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
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
import net.minecraft.world.World;

public class AddressPlaqueBlock extends BlockWithEntity implements Plasticloggable {
	protected static final List<VoxelShape> SHAPES = List.of(
		Block.createCuboidShape(0.0, 5.0, 0.0, 16.0, 11.0, 1.0),
		Block.createCuboidShape(15.0, 5.0, 0.0, 16.0, 11.0, 16.0),
		Block.createCuboidShape(0.0, 5.0, 15.0, 16.0, 11.0, 16.0),
		Block.createCuboidShape(0.0, 5.0, 0.0, 1.0, 11.0, 16.0),
		Block.createCuboidShape(0.0, 0.0, 5.0, 16.0, 1.0, 11.0),
		Block.createCuboidShape(0.0, 15.0, 5.0, 16.0, 16.0, 11.0)
	);
	public static final MapCodec<AddressPlaqueBlock> CODEC = createCodec(AddressPlaqueBlock::new);
	public static final DirectionProperty FACING = Properties.FACING;

	public AddressPlaqueBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState()
				.with(FACING, Direction.NORTH)
				.with(PLASTICLOGGED, false));
	}

	protected BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getSide().getOpposite());
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return switch(state.get(FACING)) {
			case NORTH -> SHAPES.get(0);
			case EAST -> SHAPES.get(1);
			case SOUTH -> SHAPES.get(2);
			case WEST -> SHAPES.get(3);
			case DOWN -> SHAPES.get(4);
			case UP -> SHAPES.get(5);
		};
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return state.get(PLASTICLOGGED) ? CeruleanFluids.POLYETHYLENE.getDefaultState() : super.getFluidState(state);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, PLASTICLOGGED);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new AddressPlaqueBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (!world.isClient) {
			return validateTicker(type, CeruleanBlockEntities.ADDRESS_PLAQUE, AddressPlaqueBlockEntity::tick);
		}
		return null;
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return CODEC;
	}
}
