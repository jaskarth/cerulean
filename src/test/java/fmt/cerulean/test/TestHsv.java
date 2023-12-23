package fmt.cerulean.test;

import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class TestHsv {
	public static void main(String[] args) {
		Random random = new Random();
		ImageDumper.dumpImage("hsv.png", 256, (x, z) -> {
			return MathHelper.hsvToRgb(random.nextFloat(), random.nextFloat() * 0.25f + 0.125f, 0.8f + random.nextFloat() * 0.225f + 0.02f);
		});
	}
}
