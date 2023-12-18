package fmt.cerulean.client.effects;

import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.math.Vec3d;

public class SkiesEffects extends DimensionEffects {
	public SkiesEffects() {
		super(Float.NaN, false, SkyType.NORMAL, true, false);
	}

	@Override
	public Vec3d adjustFogColor(Vec3d color, float sunHeight) {
		return color;
	}

	@Override
	public boolean useThickFog(int camX, int camY) {
		return false;
	}
}
