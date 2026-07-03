package fmt.cerulean.world.gen.synth;

import net.minecraft.util.math.noise.PerlinNoiseSampler;

public record IslandGenerationData(VoronoiSampler vn, PerlinNoiseSampler s1, PerlinNoiseSampler s2) {
}
