package fmt.cerulean;

import fmt.cerulean.net.CeruleanServerNetworking;
import fmt.cerulean.registry.CeruleanBlockEntities;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.registry.CeruleanItemGroups;
import fmt.cerulean.registry.CeruleanItems;
import fmt.cerulean.registry.CeruleanParticleTypes;
import fmt.cerulean.world.CeruleanDimensions;
import fmt.cerulean.world.gen.DreamscapeBiomeSource;
import fmt.cerulean.world.gen.DreamscapeChunkGenerator;
import fmt.cerulean.world.gen.SkiesBiomeSource;
import fmt.cerulean.world.gen.SkiesChunkGenerator;
import fmt.cerulean.world.gen.feature.BiomeDecorator;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;

public class Cerulean implements ModInitializer {
	public static final String ID = "cerulean";

	@Override
	public void onInitialize() {
		CeruleanBlocks.init();
		CeruleanBlockEntities.init();
		CeruleanParticleTypes.init();

		CeruleanItems.init();

		CeruleanItemGroups.init();

		BiomeDecorator.init();

		CeruleanServerNetworking.init();
		

		Registry.register(Registries.BIOME_SOURCE, id("dreamscape"), DreamscapeBiomeSource.CODEC);
		Registry.register(Registries.BIOME_SOURCE, id("skies"), SkiesBiomeSource.CODEC);

		Registry.register(Registries.CHUNK_GENERATOR, id("dreamscape"), DreamscapeChunkGenerator.CODEC);
		Registry.register(Registries.CHUNK_GENERATOR, id("skies"), SkiesChunkGenerator.CODEC);

		Registry.register(Registries.PAINTING_VARIANT, id("dreams"), new PaintingVariant(16, 32));

		UseItemCallback.EVENT.register((player, world, hand) -> {
			if (world.getDimensionKey().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
				return TypedActionResult.fail(ItemStack.EMPTY);
			}

			return TypedActionResult.pass(ItemStack.EMPTY);
		});

		UseBlockCallback.EVENT.register((player, world, hand, res) -> {
			if (player.getAbilities().allowModifyWorld && !player.getStackInHand(hand).isEmpty() && world.getDimensionKey().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
				return ActionResult.FAIL;
			}

			return ActionResult.PASS;
		});
	}

	public static Identifier id(String path) {
		return new Identifier(ID, path);
	}
}
