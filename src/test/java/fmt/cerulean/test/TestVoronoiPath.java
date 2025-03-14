package fmt.cerulean.test;

import fmt.cerulean.util.Vec2d;
import fmt.cerulean.util.Voronoi;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class TestVoronoiPath {
	public static void main(String[] args) {
		Voronoi vn = new Voronoi(100);
		Random random = new Random();
//		ImageDumper.dumpImage("voronoi.png", 512, (x, z) -> {
//			long i = vn.get2(x / 50.0, z / 50.0).getLeft();
//			random.setSeed(i);
//
//			int r = random.nextInt(256);
//			int g = random.nextInt(256);
//			int b = random.nextInt(256);
//
//			return ImageDumper.getIntFromColor(r, g, b);
//		});

//		ImageDumper.dumpImage("voronoi2.png", 512, (x, z) -> {
//			long i = vn.get2(x / 50.0, z / 50.0).getRight();
//			random.setSeed(i);
//
//			int r = random.nextInt(256);
//			int g = random.nextInt(256);
//			int b = random.nextInt(256);
//
//			return ImageDumper.getIntFromColor(r, g, b);
//		});

		ImageDumper.dumpImage("paths.png", 512, (x, z) -> {
			Vec2d pos = Vec2d.of(x, z);
			double scale = 50.0;
			Pair<Vec2d, Vec2d> cells = vn.getCellPos2(x / scale, z / scale, scale);
			Pair<Long, Long> values = vn.get2(x / scale, z / scale);

			int v1 = new Random(values.getLeft()).nextInt(256);
			int v2 = new Random(values.getRight()).nextInt(256);

			Vec2d left = cells.getLeft();
			Vec2d right = cells.getRight();

			double x0 = pos.x();
			double x1 = left.x();
			double x2 = right.x();

			double z0 = pos.z();
			double z1 = left.z();
			double z2 = right.z();

			double num = Math.abs(((z2 - z1) * x0) - ((x2 - x1) * z0) + (x2 * z1) - (z2 * x1));
			double den = Math.sqrt(((z2 - z1) * (z2 - z1)) + ((x2 - x1) * (x2 - x1)));

			double v = num / den;

			if (v < 1) {
				return ImageDumper.getIntFromColor(200, 200, 200);
			}

			return ImageDumper.getIntFromColor(0, 0, 0);
		});
	}
}
