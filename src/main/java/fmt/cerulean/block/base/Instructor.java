package fmt.cerulean.block.base;

public interface Instructor {
	
	void recall(String intuition);

	public static record Fragment(String intuition, long time) {
	}
}
