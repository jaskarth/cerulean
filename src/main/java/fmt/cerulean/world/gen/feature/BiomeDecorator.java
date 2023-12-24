package fmt.cerulean.world.gen.feature;

import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.util.IntRange;
import fmt.cerulean.world.gen.Biomes;
import fmt.cerulean.world.gen.feature.decoration.CeruleanPlantDecoration;
import fmt.cerulean.world.gen.feature.decoration.ReedsDecoration;
import fmt.cerulean.world.gen.feature.decorator.ChanceHeightmapDecorator;
import fmt.cerulean.world.gen.feature.decorator.CountRangeDecorator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BiomeDecorator {
	public static final Map<RegistryKey<Biome>, List<ConfiguredDecoration>> DECORATIONS = new HashMap<>();

	public static void init() {
		register(Biomes.SKIES,
				new ChanceHeightmapDecorator(4),
				new ReedsDecoration()
		);

		register(Biomes.SKIES,
				new CountRangeDecorator(20, new IntRange(64, 128)),
				new CeruleanPlantDecoration(4, 4, 32, CeruleanBlocks.CORAL)
		);

		register(Biomes.SKIES,
				new CountRangeDecorator(3, new IntRange(64, 128)),
				new CeruleanPlantDecoration(6, 6, 32, CeruleanBlocks.LUNARIUM)
		);

		register(Biomes.SKIES,
				new CountRangeDecorator(6, new IntRange(64, 128)),
				new CeruleanPlantDecoration(4, 4, 16, CeruleanBlocks.SPARKBLOSSOM)
		);

		register(Biomes.SKIES,
				new CountRangeDecorator(50, new IntRange(64, 128)),
				new CeruleanPlantDecoration(8, 8, 128, CeruleanBlocks.SKYGRASS)
		);
	}

	private static void register(RegistryKey<Biome> biome, Decorator decor, Decoration deco) {
		DECORATIONS.computeIfAbsent(biome, k -> new ArrayList<>()).add(new ConfiguredDecoration(decor, deco));
	}
}
