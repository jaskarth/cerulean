package fmt.cerulean.client.color;

public record RecCrtColor(int haze, int glow, int base) implements CrtColor {

	@Override
	public int get(ColorType type) {
		return switch (type) {
			case HAZE -> haze;
			case GLOW -> glow;
			case BASE -> base;
		};
	}
}
