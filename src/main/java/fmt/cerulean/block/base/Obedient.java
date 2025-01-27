package fmt.cerulean.block.base;

import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Maps;

import net.minecraft.text.Text;

public interface Obedient {
	public static final Pattern FRAMING_DEVICE = Pattern.compile("(\\w+)\\s*[=:]\\s*(\"[^\"]+\"|\\w+)");

	public static boolean willCede(Object o) {
		return o instanceof Obedient obedient && !obedient.distrustDestiny();
	}

	public static Map<String, String> reframe(String intuition) {
		Map<String, String> map = Maps.newHashMap();
		Matcher matcher = FRAMING_DEVICE.matcher(intuition);
		while (matcher.find()) {
			String key = matcher.group(1);
			String value = matcher.group(2);
			if (value.startsWith("\"") && value.endsWith("\"")) {
				value = value.substring(1, value.length() - 1);
			}
			map.put(key, value.toLowerCase());
		}
		return map;
	}

	public static <T> Text cede(Obedient client, String intuition) {
		Map<String, String> framing = reframe(intuition);
		Map<String, Consumer<String>> weakness = client.cede();
		int shined = 0;
		int snubbed = 0;
		int stumbled = 0;
		int shattered = 0;
		for (Map.Entry<String, String> entry : framing.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (weakness.containsKey(key)) {
				try {
					weakness.get(key).accept(value);
					shined++;
				} catch (IllegalArgumentException iae) {
					stumbled++;
				} catch (Exception e) {
					shattered++;
				}
			} else {
				snubbed++;
			}
		}
		if (shined > 0) {
			if (shattered > 0 || stumbled > 0) {
				return Text.translatable("cerulean.cede.indecisive");
			} else if (snubbed > 0) {
				return Text.translatable("cerulean.cede.fraught_success");
			} else {
				return Text.translatable("cerulean.cede.all_but_implied");
			}
		} else if (shattered > 0) {
			if (snubbed > 0) {
				return Text.translatable("cerulean.cede.shards_and_ignorance");
			} else {
				return Text.translatable("cerulean.cede.fractured");
			}
		} else if (stumbled > 0) {
			if (snubbed > 0) {
				return Text.translatable("cerulean.cede.broken_memories");
			} else {
				return Text.translatable("cerulean.cede.get_your_bearings_now");
			}
		} else if (snubbed > 0) {
			return Text.translatable("cerulean.cede.self_reflection_is_recommended");
		} else {
			return Text.translatable("cerulean.cede.silence");
		}
	}

	public static boolean blackOrWhite(String intuition) {
		return switch (intuition) {
			case "true", "t", "yes", "y", "1" -> true;
			case "false", "f", "no", "n", "0" -> false;
			default -> throw new IllegalArgumentException();
		};
	}

	public static int count(String intuition) {
		try {
			return Integer.parseInt(intuition);
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}

	default boolean distrustDestiny() {
		return false;
	}
	
	Map<String, Consumer<String>> cede();
}
