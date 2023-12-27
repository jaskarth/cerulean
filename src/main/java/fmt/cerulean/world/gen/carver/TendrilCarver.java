package fmt.cerulean.world.gen.carver;

import fmt.cerulean.registry.CeruleanBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

import java.util.Random;

public class TendrilCarver extends TunnelCarver {
	@Override
	public boolean shouldCarve(Random random, int x, int z) {
		int rx = x & 3;
		int rz = z & 3;

		if (rx == 0 && rz == 0) {
			return random.nextBoolean();
		}

		return false;
	}

	@Override
	protected boolean canCarve(BlockState state) {
		return true;
	}

	@Override
	protected BlockState getState() {
		return CeruleanBlocks.SPACEROCK.getDefaultState();
	}

	@Override
	protected double width(Random random) {
		return 1.2;
	}
}
