package fmt.cerulean.flow;

import net.minecraft.text.Text;

public interface FlowResource {

	boolean isStar();

	Brightness getBrightness();

	Color getColor();

	public static enum Brightness {
		DIM("dim"),
		WANING("waning"),
		INNOCUOUS("innocuous"),
		CANDESCENT("candescent"),
		BRILLIANT("brilliant"),
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

		public Text text() {
			return Text.translatable("cerulean.brightness." + name);
		}

		public static final int amount = Brightness.values().length;
	}

	public static enum Color {
		CERULEAN("cerulean", 0x3a3de8),
		VIRIDIAN("viridian", 0x26a875),
		ROSE("rose", 0xa82656),
		LILAC("lilac", 0xcb98e2),
		ASH("ash", 0x444242),
		CHARTREUSE("chartreuse", 0x98b840),
		TURQUOISE("turquoise", 0x5d92df),
		;

		public final String name;
		public final int color;

		private Color(String name, int color) {
			this.name = name;
			this.color = color;
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
