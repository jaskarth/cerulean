package fmt.cerulean.flow;

import net.minecraft.text.Text;

public interface FlowResource {

	boolean isStar();

	Brightness getBrightness();

	Color getColor();

	public static enum Brightness {
		BRILLIANT("brilliant"),
		CANDESCENT("candescent"),
		INNOCUOUS("innocuous"),
		WANING("waning"),
		DIM("dim"),
		;

		public final String name;

		private Brightness(String name) {
			this.name = name;
		}

		public static Brightness fromName(String name) {
			for (Brightness brightness : values()) {
				if (brightness.name.equals(name)) {
					return brightness;
				}
			}
			return null;
		}

		public Brightness dimmer() {
			return switch (this) {
				case BRILLIANT -> CANDESCENT;
				case CANDESCENT -> INNOCUOUS;
				case INNOCUOUS -> WANING;
				case WANING -> DIM;
				case DIM -> DIM;
			};
		}

		public Text text() {
			return Text.translatable("cerulean.brightness." + name);
		}

		public static final int amount = Brightness.values().length;
	}

	public static enum Color {
		CERULEAN("cerulean", 239f, 0.75f, 0.91f),
		VIRIDIAN("viridian", 156f, 0.77f, 0.66f),
		ROSE("rose", 338f, 0.77f, 0.66f),
		LILAC("lilac", 281f, 0.33f, 0.89f),
		ASH("ash", 0f, 0.03f, 0.27f),
		CHARTREUSE("chartreuse", 76f, 0.65f, 0.72f),
		TURQUOISE("turquoise", 216f, 0.58f, 0.87f),
		;

		public final String name;
		public final float h, s, v;

		private Color(String name, float h, float s, float v) {
			this.name = name;
			this.h = h;
			this.s = s;
			this.v = v;
		}

		public static Color fromName(String name) {
			for (Color color : values()) {
				if (color.name.equals(name)) {
					return color;
				}
			}
			return null;
		}

		public Text text() {
			return Text.translatable("cerulean.color." + name);
		}

		public static final int amount = Color.values().length;
	}
}
