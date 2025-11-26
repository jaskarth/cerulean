package fmt.cerulean.client.color;

import net.minecraft.util.math.ColorHelper;

public record LerpCrtColor(float amt, CrtColor a, CrtColor b) implements CrtColor {

	@Override
	public int get(ColorType type) {
		int ac = a.get(type);
		int bc = b.get(type);
		return ColorHelper.Argb.lerp(amt, ac, bc);
	}
}
