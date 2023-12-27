package fmt.cerulean.flow;

import fmt.cerulean.flow.FlowResource.Brightness;
import fmt.cerulean.flow.FlowResource.Color;
import net.minecraft.nbt.NbtCompound;

public class FlowResources {
	private static final FlowResource[] STARS;

	public static FlowResource fromNbt(NbtCompound nbt) {
		boolean star = nbt.getBoolean("Star");
		if (star) {
			Color color = Color.fromName(nbt.getString("Color"));
			Brightness brightness = Brightness.fromName(nbt.getString("Brightness"));
			if (color != null && brightness != null) {
				return star(color, brightness);
			}
		}
		return null;
	}

	public static NbtCompound toNbt(FlowResource resource) {
		NbtCompound nbt = new NbtCompound();
		if (resource == null) {
			return nbt;
		}
		nbt.putBoolean("Star", resource.isStar());
		nbt.putString("Color", resource.getColor().name);
		nbt.putString("Brightness", resource.getBrightness().name);
		return nbt;
	}

	static {
		STARS = new FlowResource[Brightness.values().length * Color.values().length];
		Brightness[] brightnesses = Brightness.values();
		Color[] colors = Color.values();
		for (int b = 0; b < brightnesses.length; b++) {
			Brightness brightness = brightnesses[b];
			//                                ⇘ ⇓ ⇙
			for (int c = 0; c < colors.length; c++) {
				Color color = colors[c];
				STARS[b * Color.amount + c] = new StarResource(color, brightness);
			}
		}
	}

	public static FlowResource star(Color color, Brightness brightness) {
		return STARS[brightness.ordinal() * Color.amount + color.ordinal()];
	}

	private static record StarResource(Color color, Brightness brightness) implements FlowResource {

		@Override
		public boolean isStar() {
			return true;
		}

		@Override
		public Brightness getBrightness() {
			return brightness;
		}

		@Override
		public Color getColor() {
			return color;
		}
	}
}
