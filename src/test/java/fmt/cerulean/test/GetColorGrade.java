package fmt.cerulean.test;

import net.minecraft.util.math.MathHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.util.*;

public class GetColorGrade {
	public static void main(String[] args) throws Throwable {
		BufferedImage read = ImageIO.read(Paths.get(args[0]).toFile());
		Set<Integer> set = new HashSet<>();

		for (int x = 0; x < read.getWidth(); x++) {
			for (int z = 0; z < read.getHeight(); z++) {
				set.add(read.getRGB(x, z));
			}
		}

		NavigableMap<Double, Integer> map = new TreeMap<>();
		for (int i : set) {
			int r = (i >> 16) & 0xFF;
			int g = (i >> 8) & 0xFF;
			int b = (i >> 0) & 0xFF;

			map.put(Math.max(Math.max(r, g), b) / 255.0, i);
		}

		for (Map.Entry<Double, Integer> e : map.entrySet()) {
			System.out.println(e.getKey() + ", 0x" + Integer.toHexString(e.getValue()).toUpperCase(Locale.ROOT));
		}

		System.out.println("----");

		for (Map.Entry<Double, Integer> e : map.entrySet()) {
			System.out.println(MathHelper.clampedMap(e.getKey(), 0.28, 0.8, 0.05, 0.85) + ", 0x" + Integer.toHexString(e.getValue()).toUpperCase(Locale.ROOT));
		}
	}
}
