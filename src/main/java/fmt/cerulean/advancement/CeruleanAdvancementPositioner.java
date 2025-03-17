package fmt.cerulean.advancement;

import fmt.cerulean.Cerulean;
import fmt.cerulean.util.Vec2d;
import net.minecraft.advancement.PlacedAdvancement;
import net.minecraft.util.Identifier;

import java.util.*;

public class CeruleanAdvancementPositioner {
	private static final Map<Identifier, Vec2d> POSITIONS = new HashMap<>();

	static {
//		initPositions();
	}

//	private static void initPositions() {
//		POSITIONS.put(Cerulean.id("cerulean/root"), new Vec2d(0, 0));
//		POSITIONS.put(Cerulean.id("cerulean/the_precipice"), new Vec2d(3.9, -1.4));
//		POSITIONS.put(Cerulean.id("cerulean/the_presupposition"), new Vec2d(-3, 2.4));
//		POSITIONS.put(Cerulean.id("cerulean/the_commutation"), new Vec2d(-3.2, -1.5));
//		POSITIONS.put(Cerulean.id("cerulean/the_desire"), new Vec2d(1.2, -2.1));
//		POSITIONS.put(Cerulean.id("cerulean/the_supplication"), new Vec2d(0.5, 1.1));
//		POSITIONS.put(Cerulean.id("cerulean/the_allegory"), new Vec2d(2.2, 3.1));
//		POSITIONS.put(Cerulean.id("cerulean/the_avarice"), new Vec2d(-1.5, 4.3));
//		POSITIONS.put(Cerulean.id("cerulean/the_facsimile"), new Vec2d(3.6, 1.1));
//		POSITIONS.put(Cerulean.id("cerulean/the_metempsychosis"), new Vec2d(4.5, -3.7));
//		POSITIONS.put(Cerulean.id("cerulean/the_misinterpretation"), new Vec2d(-4.3, -4.4));
//		POSITIONS.put(Cerulean.id("cerulean/the_transience"), new Vec2d(-0.5, -2.8));
//	}

	public static void position(PlacedAdvancement advancement) {
		List<PlacedAdvancement> advs = new ArrayList<>();
		Deque<PlacedAdvancement> stack = new ArrayDeque<>();

		stack.push(advancement);

		while (!stack.isEmpty()) {
			PlacedAdvancement adv = stack.pop();

			for (PlacedAdvancement child : adv.getChildren()) {
				stack.add(child);
			}

			if (!advs.contains(adv)) {
				advs.add(adv);
			}
		}

		for (PlacedAdvancement adv : advs) {
			Vec2d vec = CeruleanAdvancementData.get(adv.getAdvancementEntry().id()).pos();
			if (vec == null) {
				throw new IllegalStateException("No custom position set for " + adv.getAdvancementEntry().id());
			} else {
				adv.getAdvancement().display().get().setPos((float) vec.x(), (float) vec.z());
			}
		}
	}
}
