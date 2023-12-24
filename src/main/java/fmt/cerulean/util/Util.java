package fmt.cerulean.util;

import net.minecraft.util.math.Direction;

import java.util.List;
import java.util.Random;

public class Util {
	public static final Direction[] DIRECTIONS = Direction.values();

	public static <T> T pick(List<T> list, Random random) {
		return list.get(random.nextInt(list.size()));
	}
}
