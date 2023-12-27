package fmt.cerulean.world.gen.carver;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

import java.util.Random;

public class CaveCarver extends TunnelCarver {
	@Override
	public boolean shouldCarve(Random random, int x, int z) {
		int rx = x & 1;
		int rz = z & 1;

		if (rx == 0 && rz == 0) {
			return true;
		}

		return false;
	}

	@Override
	protected boolean canCarve(BlockState state) {
		return true;
	}

	@Override
	protected BlockState getState() {
		return Blocks.AIR.getDefaultState();
	}

	@Override
	protected double width(Random random) {
		return 2;
	}
}
