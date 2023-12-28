package fmt.cerulean.test;

import fmt.cerulean.util.Voronoi;

import java.util.Random;

public class TestVoronoi {
    public static void main(String[] args) {
        Voronoi vn = new Voronoi(100);
        Random random = new Random();
        ImageDumper.dumpImage("voronoi.png", 512, (x, z) -> {
            long i = vn.get(x / 20.0, z / 20.0);
            random.setSeed(i);

            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);

            return ImageDumper.getIntFromColor(r, g, b);
        });
    }
}
