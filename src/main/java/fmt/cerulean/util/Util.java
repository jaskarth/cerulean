package fmt.cerulean.util;

import net.minecraft.util.math.Direction;

import java.util.List;
import java.util.Random;

public class Util {
	public static final Direction[] DIRECTIONS = Direction.values();

	public static <T> T pick(List<T> list, Random random) {
		return list.get(random.nextInt(list.size()));
	}

	public static int remuxColor(int c) {
		int r = c & 0xFF;
		int g = (c >> 8) & 0xFF;
		int b = (c >> 16) & 0xFF;

		return (r << 16) | (g << 8) | (b << 0);
	}
}
