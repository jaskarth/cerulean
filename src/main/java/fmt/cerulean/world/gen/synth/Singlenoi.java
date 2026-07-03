package fmt.cerulean.world.gen.synth;

import fmt.cerulean.util.Vec2d;

public record Singlenoi(long color, Vec2d sample) implements VoronoiSampler {
	@Override
	public long get(double x, double z) {
		return color;
	}

	@Override
	public Vec2d getCellPos(double x, double z, double scale) {
		return sample;
	}
}
