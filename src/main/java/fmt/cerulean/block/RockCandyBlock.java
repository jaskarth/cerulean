package fmt.cerulean.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PointedDripstoneBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import java.util.Optional;

public class RockCandyBlock extends CeruleanPlantBlock {
	public static final IntProperty STAGE = IntProperty.of("stage", 1, 3);

	public RockCandyBlock(Settings settings) {
		super(settings);

		this.setDefaultState(this.getDefaultState()
			.with(STAGE, 1));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(STAGE);
	}

	@Override
	protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		int stage = state.get(STAGE);
		if (stage == 3) {
			return;
		}

		for (int y = 1; y < 7; y++) {
			BlockState here = world.getBlockState(pos.up(y));

			if (here.isOf(Blocks.POINTED_DRIPSTONE) && PointedDripstoneBlock.canDrip(here)) {
				Optional<PointedDripstoneBlock.DrippingFluid> fluid = PointedDripstoneBlock.getFluid(world, pos.up(y), here);
				if (fluid.isPresent() && fluid.get().fluid() == Fluids.WATER) {
					world.setBlockState(pos, state.with(STAGE, stage + 1));
				}
				break;
			}

			if (!here.isAir()) {
				break;
			}
		}
	}
}
