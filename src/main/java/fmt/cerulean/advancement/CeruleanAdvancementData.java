package fmt.cerulean.advancement;

import fmt.cerulean.Cerulean;
import fmt.cerulean.util.Vec2d;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CeruleanAdvancementData {
	private static final Map<Identifier, Data> DATA = new HashMap<>();

	public static void init() {
		register("root", new Data(new Vec2d(0, 0)));
		register("the_precipice", new Data(new Vec2d(3.9, -1.4), "advancements.cerulean.the_precipice.desc_alt"));
		register("the_presupposition", new Data(new Vec2d(-3, 2.4)));
		register("the_commutation", new Data(new Vec2d(-3.2, -1.5)));
		register("the_desire", new Data(new Vec2d(1.2, -2.1)));
		register("the_supplication", new Data(new Vec2d(0.5, 1.1), "advancements.cerulean.the_supplication.desc_alt"));
		register("the_allegory", new Data(new Vec2d(2.8, 3.3)));
		register("the_avarice", new Data(new Vec2d(-1.5, 4.3), "advancements.cerulean.the_avarice.desc_alt"));
		register("the_facsimile", new Data(new Vec2d(3.9, 1.3)));
		register("the_metempsychosis", new Data(new Vec2d(4.5, -3.7), "advancements.cerulean.the_metempsychosis.desc_alt"));
		register("the_misinterpretation", new Data(new Vec2d(-4.3, -4.4), "advancements.cerulean.the_misinterpretation.desc_alt"));
		register("the_transience", new Data(new Vec2d(-0.5, -3.3), "advancements.cerulean.the_transience.desc_alt"));
		register("the_disposition", new Data(new Vec2d(-5.1, 0.2), "advancements.cerulean.the_disposition.desc_alt"));
	}

	private static void register(String name, Data data) {
		DATA.put(Cerulean.id("cerulean/" + name), data);
	}

	public static Data get(Identifier id) {
		return DATA.get(id);
	}

	public record Data(Vec2d pos, @Nullable String altDesc) {
		private Data(Vec2d pos) {
			this(pos, null);
		}
	}
}
