package fmt.cerulean.world.gen;

import fmt.cerulean.util.Vec2i;

import java.util.Random;

public record IslandParameters(
		Vec2i center,
		int startOff,
		double distOffset,
		boolean plasticlogged
) {
	public static IslandParameters get(Vec2i center, long data) {
		Random random = new Random(data);

		int startOff = random.nextInt(7) - 3;
		double distOffset = random.nextDouble() * 2.5;

		boolean plasticlogged = random.nextInt(15) == 0;

		return new IslandParameters(
				center,
				startOff,
				distOffset,
				plasticlogged
		);
	}
}
