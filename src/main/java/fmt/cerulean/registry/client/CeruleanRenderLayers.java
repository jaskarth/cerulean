package fmt.cerulean.registry.client;

import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.registry.CeruleanFluids;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class CeruleanRenderLayers {
	
	public static void init() {
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
				CeruleanBlocks.PIPE,
				CeruleanBlocks.EXPOSED_PIPE,
				CeruleanBlocks.WEATHERED_PIPE,
				CeruleanBlocks.OXIDIZED_PIPE,
				CeruleanBlocks.WAXED_PIPE,
				CeruleanBlocks.WAXED_EXPOSED_PIPE,
				CeruleanBlocks.WAXED_WEATHERED_PIPE,
				CeruleanBlocks.WAXED_OXIDIZED_PIPE,
				CeruleanBlocks.FUCHSIA_PIPE,
				CeruleanBlocks.LUSTROUS_PIPE,
				CeruleanBlocks.DUCTILE_PIPE,
				CeruleanBlocks.CHIMERIC_PIPE,
				CeruleanBlocks.CORAL,
				CeruleanBlocks.SKYGRASS,
				CeruleanBlocks.SPARKBLOSSOM,
				CeruleanBlocks.GLITTERING_SPARKBLOSSOM,
				CeruleanBlocks.SPARKLESSBLOSSOM,
				CeruleanBlocks.STICKBLOSSOM,
				CeruleanBlocks.GLITTERING_STICKBLOSSOM,
				CeruleanBlocks.STICKBLOSSOMLESS,
				CeruleanBlocks.LUNARIUM,
				CeruleanBlocks.REEDS,
				CeruleanBlocks.REEDS_PLANT,
				CeruleanBlocks.STRONGBOX,
				CeruleanBlocks.KALE,
				CeruleanBlocks.KALE_PLANT,
				CeruleanBlocks.OAK_GAPDOOR,
				CeruleanBlocks.HALITE_OUTCROPPING,
				CeruleanBlocks.HALITE_OUTCROPPING_LARGE,
				CeruleanBlocks.HALITE_OUTCROPPING_MEDIUM,
				CeruleanBlocks.HALITE_OUTCROPPING_SMALL
		);

		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(),
				CeruleanBlocks.POLYETHYLENE,
				CeruleanBlocks.REALIZED_POLYETHYLENE
		);

		BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(),
				CeruleanFluids.POLYETHYLENE,
				CeruleanFluids.REALIZED_POLYETHYLENE,
				CeruleanFluids.REALIZED_POLYETHYLENE_FLOWING
		);
	}
}
