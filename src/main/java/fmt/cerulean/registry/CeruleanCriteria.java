package fmt.cerulean.registry;

import fmt.cerulean.Cerulean;
import fmt.cerulean.advancement.CeruleanCriterion;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CeruleanCriteria {
	public static final CeruleanCriterion CRITERION = register("cerulean", new CeruleanCriterion());

	public static void init() {

	}

	public static <T extends Criterion<?>> T register(String id, T criterion) {
		return Registry.register(Registries.CRITERION, Cerulean.id(id), criterion);
	}
}
