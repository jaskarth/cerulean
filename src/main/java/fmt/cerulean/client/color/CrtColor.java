package fmt.cerulean.client.color;

public interface CrtColor {
	int get(ColorType type);

	enum ColorType {
		HAZE,
		GLOW,
		BASE,
	}
}
