package fmt.cerulean.util;

import java.util.Random;

public record IntRange(int min, int max) {
	public IntRange {
		if (min > max) {
			throw new RuntimeException("Cannot make range [" + min + ", " + max + "]");
		}
	}

	public int random(Random random) {
		int range = max - min;
		return min + (range == 0 ? 0 : random.nextInt(range));
	}
}
