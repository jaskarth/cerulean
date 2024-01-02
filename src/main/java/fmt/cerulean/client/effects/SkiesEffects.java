package fmt.cerulean.client.effects;

import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

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

	@Nullable
	@Override
	public float[] getFogColorOverride(float skyAngle, float tickDelta) {
		return null;
	}
}
