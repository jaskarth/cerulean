package fmt.cerulean.world.gen.synth;

import fmt.cerulean.util.Vec2d;
import fmt.cerulean.util.Vec2i;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public record IslandParameters(
		Vec2i center,
		int startOff,
		double distOffset,
		boolean plasticlogged,
		boolean cavity
) {
	public static IslandParameters get(Vec2i center, long data) {
		Random random = new Random(data);

		int startOff = random.nextInt(7) - 3;
		double distOffset = random.nextDouble() * 2.5;

		boolean plasticlogged = random.nextInt(7) == 0;
		boolean cavity = random.nextInt() > 0;

		return new IslandParameters(
				center,
				startOff,
				distOffset,
				plasticlogged,
				cavity
		);
	}

	public static IslandParameters get(long seed, BlockPos pos) {
		double scale = 48.0 * 4;
		Voronoi vn = new Voronoi(seed);
		int x = pos.getX();
		int z = pos.getZ();
		long uniq = vn.get(x / scale, z / scale);
		Vec2d cp = vn.getCellPos(x / scale, z / scale, scale);
		Vec2i center = cp.floor();

		return IslandParameters.get(center, uniq);
	}
}
