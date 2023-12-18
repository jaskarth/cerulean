package fmt.cerulean.client.effects;

import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.math.Vec3d;

public class DreamscapeEffects extends DimensionEffects {
	public DreamscapeEffects() {
		super(Float.NaN, false, DimensionEffects.SkyType.NORMAL, false, false);
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
