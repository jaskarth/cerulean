package fmt.cerulean.solitaire;

public record CardPos(Type type, int row, int depth) {

	public CardPos mirrored() {
		return new CardPos(switch(type) {
			case ARTS -> Type.DREAM;
			case DREAM -> Type.ARTS;
			default -> type;
		}, row, depth);
	}

	public static enum Type {
		ARTS,
		DREAM,
		HELD
	}
}
