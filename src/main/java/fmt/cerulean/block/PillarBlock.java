package fmt.cerulean.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class PillarBlock extends Block {
	public static final EnumProperty<PillarState> PILLAR = EnumProperty.of("pillar", PillarState.class);
	// TODO: rotation!

	public PillarBlock(Settings settings) {
		super(settings);

		setDefaultState(this.stateManager.getDefaultState()
				.with(PILLAR, PillarState.BOTH));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(PILLAR);
	}

	@Override
	protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return getState(world, pos);
	}

	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
		return getState(ctx.getWorld(), ctx.getBlockPos());
	}

	private BlockState getState(WorldAccess world, BlockPos pos) {
		boolean top = world.getBlockState(pos.up()).isOf(this);
		boolean bottom = world.getBlockState(pos.down()).isOf(this);

		return this.getDefaultState().with(PILLAR, PillarState.get(!top, !bottom));
	}

	public enum PillarState implements StringIdentifiable {
		BOTH("both"),
		TOP("top"),
		BOTTOM("bottom"),
		NEITHER("neither");

		private final String name;
		PillarState(String name) {
			this.name = name;
		}

		public static PillarState get(boolean top, boolean bottom) {
			if (top && bottom) {
				return BOTH;
			} else if (top) {
				return TOP;
			} else if (bottom) {
				return BOTTOM;
			} else {
				return NEITHER;
			}
		}

		@Override
		public String asString() {
			return name;
		}
	}
}
