package fmt.cerulean;

import java.util.List;

import fmt.cerulean.advancement.CeruleanAdvancementData;
import fmt.cerulean.command.ClearPhotosCommand;
import fmt.cerulean.flow.recipe.BrushRecipes;
import fmt.cerulean.net.CeruleanNetworking;
import fmt.cerulean.net.CeruleanServerNetworking;
import fmt.cerulean.registry.*;
import fmt.cerulean.util.PaintingDuck;
import fmt.cerulean.world.CeruleanDimensions;
import fmt.cerulean.world.gen.DreamscapeBiomeSource;
import fmt.cerulean.world.gen.DreamscapeChunkGenerator;
import fmt.cerulean.world.gen.SkiesBiomeSource;
import fmt.cerulean.world.gen.SkiesChunkGenerator;
import fmt.cerulean.world.gen.feature.BiomeDecorator;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;

public class Cerulean implements ModInitializer {
	public static final String ID = "cerulean";

	@Override
	public void onInitialize() {
		CeruleanCriteria.init();
		CeruleanAdvancementData.init();

		CeruleanFluids.init();
		CeruleanBlocks.init();
		CeruleanBlockEntities.init();
		CeruleanParticleTypes.init();

		CeruleanItemComponents.init();
		CeruleanItems.init();

		CeruleanItemGroups.init();

		CeruleanEntities.init();

		BiomeDecorator.init();

		CeruleanNetworking.init();
		CeruleanServerNetworking.init();

		BrushRecipes.init();

		CommandRegistrationCallback.EVENT.register((dispatcher, access, env) -> {
			ClearPhotosCommand.register(dispatcher);
		});

		Registry.register(Registries.BIOME_SOURCE, id("dreamscape"), DreamscapeBiomeSource.CODEC);
		Registry.register(Registries.BIOME_SOURCE, id("skies"), SkiesBiomeSource.CODEC);

		Registry.register(Registries.CHUNK_GENERATOR, id("dreamscape"), DreamscapeChunkGenerator.CODEC);
		Registry.register(Registries.CHUNK_GENERATOR, id("skies"), SkiesChunkGenerator.CODEC);

		UseItemCallback.EVENT.register((player, world, hand) -> {
			if (world.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
				return TypedActionResult.fail(ItemStack.EMPTY);
			}

			return TypedActionResult.pass(ItemStack.EMPTY);
		});

		UseBlockCallback.EVENT.register((player, world, hand, res) -> {
			if (player.getAbilities().allowModifyWorld && !player.getStackInHand(hand).isEmpty() && world.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
				return ActionResult.FAIL;
			}

			return ActionResult.PASS;
		});

		EntitySleepEvents.ALLOW_SLEEP_TIME.register((player, sleepingPos, vanillaResult) -> {
			List<PaintingEntity> paintings = player.getWorld().getEntitiesByClass(PaintingEntity.class, Box.enclosing(sleepingPos, sleepingPos).expand(10), p -> true);
			boolean found = false;

			for (PaintingEntity painting : paintings) {
				if (painting instanceof PaintingDuck duck && duck.manifestsInDreams() && duck.lethargic()) {
					if (found) {
						// Found two portal paintings?
						found = false;
						break;
					}
					found = true;
				}
			}

			if (found) {
				return ActionResult.SUCCESS;
			}
			return ActionResult.PASS;
		});
	}

	public static Identifier id(String path) {
		return Identifier.of(ID, path);
	}
}
