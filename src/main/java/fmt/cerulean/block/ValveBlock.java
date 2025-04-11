package fmt.cerulean.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class ValveBlock extends PipeBlock {
	public static final BooleanProperty POWERED = Properties.POWERED;

	public ValveBlock(Settings settings) {
		super(settings);
		this.setDefaultState(stateManager.getDefaultState()
				.with(UP, false)
				.with(DOWN, false)
				.with(NORTH, false)
				.with(EAST, false)
				.with(SOUTH, false)
				.with(WEST, false)
				.with(WATERLOGGED, false)
				.with(PLASTICLOGGED, false)
				.with(POWERED, false)
		);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(POWERED);
	}

	@Override
	protected BlockState getDefaultStateFromWorld(WorldAccess world, BlockPos pos) {
		BlockState state = super.getDefaultStateFromWorld(world, pos);
		if (world.isReceivingRedstonePower(pos)) {
			state = state.with(ValveBlock.POWERED, true);
		}

		return state;
	}

	@Override
	protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
		if (!world.isClient()) {
			if (world.isReceivingRedstonePower(pos)) {
				world.setBlockState(pos, state.with(POWERED, true), Block.NOTIFY_LISTENERS);
				world.playSound(
						null,
						pos,
						BlockSetType.IRON.trapdoorClose(),
						SoundCategory.BLOCKS,
						1.0F,
						world.getRandom().nextFloat() * 0.1F + 0.9F
				);
			} else {
				world.scheduleBlockTick(pos, this, 4);
			}
		}
	}

	@Override
	protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (state.get(POWERED) && !world.isReceivingRedstonePower(pos)) {
			world.setBlockState(pos, state.with(POWERED, false), Block.NOTIFY_LISTENERS);
			world.playSound(
					null,
					pos,
					BlockSetType.IRON.trapdoorOpen(),
					SoundCategory.BLOCKS,
					1.0F,
					world.getRandom().nextFloat() * 0.1F + 0.9F
			);
		}
	}
}
