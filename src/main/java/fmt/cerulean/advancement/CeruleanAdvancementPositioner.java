package fmt.cerulean.advancement;

import fmt.cerulean.Cerulean;
import fmt.cerulean.util.Vec2d;
import net.minecraft.advancement.PlacedAdvancement;
import net.minecraft.util.Identifier;

import java.util.*;

public class CeruleanAdvancementPositioner {
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
