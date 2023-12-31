package fmt.cerulean.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class OxidizablePipeBlock extends PipeBlock implements Oxidizable {
	private final OxidationLevel level;

	public OxidizablePipeBlock(OxidationLevel level, Settings settings) {
		super(settings);
		this.level = level;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		this.tickDegradation(state, world, pos, random);
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return this.level != OxidationLevel.OXIDIZED;
	}

	@Override
	public OxidationLevel getDegradationLevel() {
		return this.level;
	}
}
