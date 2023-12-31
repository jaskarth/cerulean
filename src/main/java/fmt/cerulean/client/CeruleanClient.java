package fmt.cerulean.client;

import fmt.cerulean.Cerulean;
import fmt.cerulean.block.entity.WellBlockEntity;
import fmt.cerulean.client.effects.DreamscapeEffects;
import fmt.cerulean.client.effects.SkiesEffects;
import fmt.cerulean.client.render.DreamscapeRenderer;
import fmt.cerulean.client.render.SkiesRenderer;
import fmt.cerulean.item.StarItem;
import fmt.cerulean.net.CeruleanClientNetworking;
import fmt.cerulean.registry.CeruleanItems;
import fmt.cerulean.registry.client.CeruleanBlockEntityRenderers;
import fmt.cerulean.registry.client.CeruleanParticles;
import fmt.cerulean.registry.client.CeruleanRenderLayers;
import fmt.cerulean.world.CeruleanDimensions;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

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
			return WellBlockEntity.getRgb(((StarItem) stack.getItem()).resource);
		}, CeruleanItems.STARS.values().stream().toArray(i -> new ItemConvertible[i]));
	}
}
