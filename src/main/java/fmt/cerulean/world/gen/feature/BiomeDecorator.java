package fmt.cerulean.world.gen.feature;

import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.util.IntRange;
import fmt.cerulean.world.gen.Biomes;
import fmt.cerulean.world.gen.carver.CaveCarver;
import fmt.cerulean.world.gen.carver.SkyCarver;
import fmt.cerulean.world.gen.carver.TendrilCarver;
import fmt.cerulean.world.gen.carver.TunnelCarver;
import fmt.cerulean.world.gen.feature.decoration.*;
import fmt.cerulean.world.gen.feature.decorator.*;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BiomeDecorator {
	public static final Map<RegistryKey<Biome>, List<ConfiguredDecoration>> DECORATIONS = new HashMap<>();

	public static final List<SkyCarver> CARVERS = List.of(
			new CaveCarver(),
			new TendrilCarver()
	);

	public static void init() {
		register(Biomes.SKIES,
				new NoOpDecorator(),
				new WellDecoration()
		);

		register(Biomes.SKIES,
				new ChanceRangeDecorator(36, new IntRange(80, 140)),
				new IslandDecoration()
		);

		register(Biomes.SKIES,
				new NoOpDecorator(),
				new IslandAugmentDecoration()
		);

		register(Biomes.SKIES,
				new RegionDecorator(4, 2, new ChanceRangeDecorator(1, new IntRange(0, 256)), 158139),
				new NeodeDecoration()
		);

		register(Biomes.SKIES,
				new NoOpDecorator(),
				new KaleDecoration()
		);

		register(Biomes.SKIES,
				new ChanceHeightmapDecorator(4),
				new ReedsDecoration()
		);

		register(Biomes.SKIES,
				new CountRangeDecorator(30, new IntRange(40, 150)),
				new CeruleanPlantDecoration(4, 4, 32, CeruleanBlocks.CORAL)
		);

		register(Biomes.SKIES,
				new CountRangeDecorator(6, new IntRange(40, 150)),
				new CeruleanPlantDecoration(6, 6, 32, CeruleanBlocks.LUNARIUM)
		);

		register(Biomes.SKIES,
				new CountRangeDecorator(18, new IntRange(40, 150)),
				new CeruleanPlantDecoration(4, 4, 28, CeruleanBlocks.SPARKBLOSSOM)
		);

		register(Biomes.SKIES,
				new CountRangeDecorator(75, new IntRange(40, 150)),
				new CeruleanPlantDecoration(8, 8, 128, CeruleanBlocks.SKYGRASS)
		);
	}

	private static void register(RegistryKey<Biome> biome, Decorator decor, Decoration deco) {
		DECORATIONS.computeIfAbsent(biome, k -> new ArrayList<>()).add(new ConfiguredDecoration(decor, deco));
	}
}
