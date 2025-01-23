package fmt.cerulean.advancement;

import fmt.cerulean.Cerulean;
import fmt.cerulean.util.Vec2d;
import net.minecraft.advancement.PlacedAdvancement;
import net.minecraft.util.Identifier;

import java.util.*;

public class CeruleanAdvancementPositioner {
	private static final Map<Identifier, Vec2d> POSITIONS = new HashMap<>();

	static {
		initPositions();
	}

	private static void initPositions() {
		POSITIONS.put(Cerulean.id("cerulean/root"), new Vec2d(0, 0));
		POSITIONS.put(Cerulean.id("cerulean/the_precipice"), new Vec2d(2, -1.4));
		POSITIONS.put(Cerulean.id("cerulean/the_presupposition"), new Vec2d(-3, 2.4));
		POSITIONS.put(Cerulean.id("cerulean/the_commutation"), new Vec2d(-1.2, -2.1));
	}

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
			Vec2d vec = POSITIONS.get(adv.getAdvancementEntry().id());
			if (vec == null) {
				throw new IllegalStateException("No custom position set for " + adv.getAdvancementEntry().id());
			} else {
				adv.getAdvancement().display().get().setPos((float) vec.x(), (float) vec.z());
			}
		}
	}
}
