package fmt.cerulean;

import fmt.cerulean.world.gen.DreamscapeBiomeSource;
import fmt.cerulean.world.gen.DreamscapeChunkGenerator;
import fmt.cerulean.world.gen.SkiesBiomeSource;
import fmt.cerulean.world.gen.SkiesChunkGenerator;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Cerulean implements ModInitializer {
	public static final String ID = "cerulean";

	@Override
	public void onInitialize() {

		Registry.register(Registries.BIOME_SOURCE, id("dreamscape"), DreamscapeBiomeSource.CODEC);
		Registry.register(Registries.BIOME_SOURCE, id("skies"), SkiesBiomeSource.CODEC);

		Registry.register(Registries.CHUNK_GENERATOR, id("dreamscape"), DreamscapeChunkGenerator.CODEC);
		Registry.register(Registries.CHUNK_GENERATOR, id("skies"), SkiesChunkGenerator.CODEC);

		Registry.register(Registries.PAINTING_VARIANT, id("dreams"), new PaintingVariant(16, 32));
	}

	public static Identifier id(String path) {
		return new Identifier(ID, path);
	}
}
