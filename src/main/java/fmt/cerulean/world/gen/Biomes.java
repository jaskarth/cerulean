package fmt.cerulean.world.gen;

import fmt.cerulean.Cerulean;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.Biome;

public class Biomes {
	public static final RegistryKey<Biome> SKIES = RegistryKey.of(RegistryKeys.BIOME, Cerulean.id("skies"));
}
