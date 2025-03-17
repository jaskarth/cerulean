package fmt.cerulean.solitaire;

public enum Suit {
	COMPOSITION (Type.ARTS,  125f, 0.80f, 0.95f),
	NARRATIVE   (Type.ARTS,  20f,  0.60f, 0.85f),
	PERFORMANCE (Type.ARTS,  250f, 0.70f, 0.85f),
	CERULEAN    (Type.DREAM, 239f, 0.75f, 0.91f),
	VIRIDIAN    (Type.DREAM, 156f, 0.77f, 0.66f),
	ROSE        (Type.DREAM, 338f, 0.77f, 0.66f),
	LILAC       (Type.DREAM, 281f, 0.33f, 0.89f),
	ASH         (Type.DREAM, 0f,   0.03f, 0.27f),
	CHARTREUSE  (Type.DREAM, 76f,  0.65f, 0.72f),
	TURQUOISE   (Type.DREAM, 216f, 0.58f, 0.87f),
	;

	public final Type type;
	public final float h, s, v;

	private Suit(Type type, float h, float s, float v) {
		this.type = type;
		this.h = h;
		this.s = s;
		this.v = v;
	}
	
	public static enum Type {
		ARTS,
		DREAM
	}
}
