package fmt.cerulean.client;

import fmt.cerulean.Cerulean;
import fmt.cerulean.block.entity.WellBlockEntity;
import fmt.cerulean.client.effects.DreamscapeEffects;
import fmt.cerulean.client.effects.SkiesEffects;
import fmt.cerulean.client.render.DreamscapeRenderer;
import fmt.cerulean.client.render.SkiesRenderer;
import fmt.cerulean.client.render.entity.MemoryFrameRenderer;
import fmt.cerulean.client.render.item.EyeOfVenderer;
import fmt.cerulean.client.tex.CeruleanAtlasSource;
import fmt.cerulean.client.tex.CeruleanModelLoadingPlugin;
import fmt.cerulean.fluid.CanisterFluidType;
import fmt.cerulean.item.StarItem;
import fmt.cerulean.net.CeruleanClientNetworking;
import fmt.cerulean.registry.CeruleanEntities;
import fmt.cerulean.registry.CeruleanFluids;
import fmt.cerulean.registry.CeruleanItemComponents;
import fmt.cerulean.registry.CeruleanItems;
import fmt.cerulean.registry.client.CeruleanBlockEntityRenderers;
import fmt.cerulean.registry.client.CeruleanParticles;
import fmt.cerulean.registry.client.CeruleanRenderLayers;
import fmt.cerulean.world.CeruleanDimensions;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.client.item.ClampedModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.texture.atlas.AtlasSourceType;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class CeruleanClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		CeruleanRenderLayers.init();
		CeruleanParticles.init();
		CeruleanBlockEntityRenderers.init();

		DimensionRenderingRegistry.registerDimensionEffects(Cerulean.id("dreamscape"), new DreamscapeEffects());
		DimensionRenderingRegistry.registerDimensionEffects(Cerulean.id("skies"), new SkiesEffects());

		DimensionRenderingRegistry.registerSkyRenderer(RegistryKey.of(RegistryKeys.WORLD, CeruleanDimensions.DREAMSCAPE), new DreamscapeRenderer());
		DimensionRenderingRegistry.registerSkyRenderer(RegistryKey.of(RegistryKeys.WORLD, Cerulean.id("skies")), new SkiesRenderer());

		CeruleanClientNetworking.init();

		ColorProviderRegistry.ITEM.register((stack, index) -> {
			return 0xFF000000 | WellBlockEntity.getRgb(((StarItem) stack.getItem()).resource);
		}, CeruleanItems.STARS.values().stream().toArray(i -> new ItemConvertible[i]));


		FluidRenderHandlerRegistry.INSTANCE.register(CeruleanFluids.POLYETHYLENE,
				new SimpleFluidRenderHandler(Cerulean.id("block/polyethylene"),
						Cerulean.id("block/polyethylene"), Cerulean.id("block/transparent")));

		ClampedModelPredicateProvider fluidType = (stack, world, entity, seed) -> {
			return stack.getOrDefault(CeruleanItemComponents.FLUID_TYPE, CanisterFluidType.NONE).ordinal() / 100.f;
		};
		ModelPredicateProviderRegistry.register(CeruleanItems.VACUUM_PUMP, Identifier.of("fluid_type"), fluidType);
		ModelPredicateProviderRegistry.register(CeruleanItems.DEPRESSURIZER, Identifier.of("fluid_type"), fluidType);

		FluidRenderHandlerRegistry.INSTANCE.register(CeruleanFluids.REALIZED_POLYETHYLENE, CeruleanFluids.REALIZED_POLYETHYLENE_FLOWING, SimpleFluidRenderHandler.coloredWater(0x999999));

		ModelLoadingPlugin.register(new CeruleanModelLoadingPlugin());
		AtlasSourceTypeRegistry.register(Identifier.of("cerulean"), CeruleanAtlasSource.TYPE);

		BuiltinItemRendererRegistry.INSTANCE.register(CeruleanItems.EYE_OF_VENDOR, new EyeOfVenderer());

		EntityRendererRegistry.register(CeruleanEntities.MEMORY_FRAME, MemoryFrameRenderer::new);
	}
}
