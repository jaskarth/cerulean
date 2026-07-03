package fmt.cerulean.world.gen.synth;

import fmt.cerulean.util.Vec2d;

public interface VoronoiSampler {
	long get(double x, double z);

	Vec2d getCellPos(double x, double z, double scale);
}
