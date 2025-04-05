package fmt.cerulean.solitaire;

public record CardPos(Type type, int row, int depth) {

	public CardPos mirrored() {
		return new CardPos(switch(type) {
			case ARTS -> Type.DREAM;
			case DREAM -> Type.ARTS;
			default -> type;
		}, row, depth);
	}

	public int encode() {
		int t = switch(type) {
			case HELD -> 0;
			case ARTS -> 1;
			case DREAM -> 2;
		};
		return (t << 14) | (row << 7) | (depth << 0);
	}

	public static CardPos decode(int pos) {
		int t = (pos >> 14) & 0b11;
		int row = (pos >> 7) & 0b0111_1111;
		int depth = (pos >> 0) & 0b0111_1111;
		Type type = switch (t) {
			case 1 -> Type.ARTS;
			case 2 -> Type.DREAM;
			default -> Type.HELD;
		};
		return new CardPos(type, row, depth);
	}

	public static enum Type {
		ARTS,
		DREAM,
		HELD
	}
}
