package fmt.cerulean.registry;

import fmt.cerulean.Cerulean;
import fmt.cerulean.screen.RealityAnchorScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class CeruleanScreenHandlers {
	public static final ScreenHandlerType<RealityAnchorScreenHandler> REALITY_ANCHOR = register("reality_anchor", RealityAnchorScreenHandler::new);

	public static void init() {

	}

	private static <T extends ScreenHandler> ScreenHandlerType<T> register(String id, ScreenHandlerType.Factory<T> factory) {
		return Registry.register(Registries.SCREEN_HANDLER, Cerulean.id(id), new ScreenHandlerType<>(factory, FeatureFlags.VANILLA_FEATURES));
	}
}
