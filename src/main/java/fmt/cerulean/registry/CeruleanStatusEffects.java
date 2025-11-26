package fmt.cerulean.registry;

import fmt.cerulean.Cerulean;
import fmt.cerulean.effect.OxidizedStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class CeruleanStatusEffects {
	public static final RegistryEntry<StatusEffect> OXIDIZED = register("oxidized", new OxidizedStatusEffect());

	public static void init() {

	}

	private static RegistryEntry<StatusEffect> register(String path, StatusEffect effect) {
		return Registry.registerReference(Registries.STATUS_EFFECT, Cerulean.id(path), effect);
	}
}
