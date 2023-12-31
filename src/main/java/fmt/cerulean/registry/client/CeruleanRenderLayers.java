package fmt.cerulean.registry.client;

import fmt.cerulean.registry.CeruleanBlocks;
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
				CeruleanBlocks.CORAL,
				CeruleanBlocks.SKYGRASS,
				CeruleanBlocks.SPARKBLOSSOM,
				CeruleanBlocks.SPARKLESSBLOSSOM,
				CeruleanBlocks.LUNARIUM,
				CeruleanBlocks.REEDS,
				CeruleanBlocks.REEDS_PLANT
		);
	}
}
