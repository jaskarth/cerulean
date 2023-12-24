package fmt.cerulean.client;

import fmt.cerulean.Cerulean;
import fmt.cerulean.client.effects.DreamscapeEffects;
import fmt.cerulean.client.effects.SkiesEffects;
import fmt.cerulean.client.render.DreamscapeRenderer;
import fmt.cerulean.client.render.SkiesRenderer;
import fmt.cerulean.registry.CeruleanParticles;
import fmt.cerulean.registry.CeruleanRenderLayers;
import fmt.cerulean.world.CeruleanDimensions;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class CeruleanClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		CeruleanRenderLayers.init();
		CeruleanParticles.init();

		DimensionRenderingRegistry.registerDimensionEffects(Cerulean.id("dreamscape"), new DreamscapeEffects());
		DimensionRenderingRegistry.registerDimensionEffects(Cerulean.id("skies"), new SkiesEffects());

		DimensionRenderingRegistry.registerSkyRenderer(RegistryKey.of(RegistryKeys.WORLD, CeruleanDimensions.DREAMSCAPE), new DreamscapeRenderer());
		DimensionRenderingRegistry.registerSkyRenderer(RegistryKey.of(RegistryKeys.WORLD, Cerulean.id("skies")), new SkiesRenderer());
	}
}
