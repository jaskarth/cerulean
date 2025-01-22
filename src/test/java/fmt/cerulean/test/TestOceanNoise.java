package fmt.cerulean.test;

import fmt.cerulean.util.Voronoi;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.util.math.random.CheckedRandom;

import java.util.Random;
import java.util.stream.IntStream;

public class TestOceanNoise {
    public static void main(String[] args) {
        OctavePerlinNoiseSampler sampler = OctavePerlinNoiseSampler.create(new CheckedRandom(100), IntStream.of(-4, -3, -1));
        Random random = new Random();
        ImageDumper.dumpImage("ocean_noise.png", 2048, (x, z) -> {
            int v = (int) MathHelper.clampedMap(sampler.sample(x / 20., 10, z / 20.), -1, -0.2, 255, 0);

            return ImageDumper.getIntFromColor(v, v, v);
        });
    }
}
