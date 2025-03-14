package fmt.cerulean.client.render;

import fmt.cerulean.compat.Soul;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Random;

public final class EmergencyStack {
	private static final String PATTERN = "$$POETRY$$";
	public static final String T_1 = "fmt.cerulean.reality.RealityViolation: Incompatible object change detected, automatic mitigation activated";
	private static String XT_2 = "  at fmt.cerulean.reality.Camera.checkAnomalies(" + PATTERN + ":" + Formatting.OBFUSCATED + "941" + Formatting.RESET + ")";
	private static String XT_3 = "  at fmt.cerulean.reality.Love.analyze(" + PATTERN + ":" + Formatting.OBFUSCATED + "4714" + Formatting.RESET + ")";
	private static String XT_4 = "  at fmt.cerulean.reality.Camera.captureFrame(" + PATTERN + ":" + Formatting.OBFUSCATED + "834" + Formatting.RESET + ")";
	private static String XT_5 = "  at fmt.cerulean.reality.Camera.captureAndSend(" + PATTERN + ":" + Formatting.OBFUSCATED + "4193" + Formatting.RESET + ")";
	private static String XT_6 = "  at fmt.cerulean.reality.Camera.breathe(" + PATTERN + ":" + Formatting.OBFUSCATED + "5148" + Formatting.RESET + ")";
	private static String XT_7 = "  at fmt.cerulean.task.ActionResponseTask.runTask(" + PATTERN + ":" + Formatting.OBFUSCATED + "531" + Formatting.RESET + ")";
	private static String XT_8 = "  at fmt.cerulean.task.ActionResponseTask.runAllTasks(" + PATTERN + ":" + Formatting.OBFUSCATED + "957" + Formatting.RESET + ")";
	private static String XT_9 = "  at fmt.cerulean.event.WorldRenderEvents.onRenderLast(" + PATTERN + ":" + Formatting.OBFUSCATED + "694" + Formatting.RESET + ")";
	private static String XT_10 = "  at fmt.cerulean.event.WorldRenderEvents.onRender(" + PATTERN + ":" + Formatting.OBFUSCATED + "5914" + Formatting.RESET + ")";
	private static String XT_11 = "  at fmt.cerulean.event.RenderHookInvoker.render(" + PATTERN + ":" + Formatting.OBFUSCATED + "5931" + Formatting.RESET + ")";

	public static String T_2 = XT_2;
	public static String T_3 = XT_3;
	public static String T_4 = XT_4;
	public static String T_5 = XT_5;
	public static String T_6 = XT_6;
	public static String T_7 = XT_7;
	public static String T_8 = XT_8;
	public static String T_9 = XT_9;
	public static String T_10 = XT_10;
	public static String T_11 = XT_11;
	public static String T_E = "  ... 20 more";

	public static void shuffle() {
		List<String> poetry = Soul.SCRIPTURE.get(0);
		Random random = new Random();
		T_2 = XT_2.replace(PATTERN, poetry.get(random.nextInt(poetry.size())));
		T_3 = XT_3.replace(PATTERN, poetry.get(random.nextInt(poetry.size())));
		T_4 = XT_4.replace(PATTERN, poetry.get(random.nextInt(poetry.size())));
		T_5 = XT_5.replace(PATTERN, poetry.get(random.nextInt(poetry.size())));
		T_6 = XT_6.replace(PATTERN, poetry.get(random.nextInt(poetry.size())));
		T_7 = XT_7.replace(PATTERN, poetry.get(random.nextInt(poetry.size())));
		T_8 = XT_8.replace(PATTERN, poetry.get(random.nextInt(poetry.size())));
		T_9 = XT_9.replace(PATTERN, poetry.get(random.nextInt(poetry.size())));
		T_10 = XT_10.replace(PATTERN, poetry.get(random.nextInt(poetry.size())));
		T_11 = XT_11.replace(PATTERN, poetry.get(random.nextInt(poetry.size())));
	}
}
