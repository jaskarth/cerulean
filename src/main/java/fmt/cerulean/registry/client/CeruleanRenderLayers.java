package fmt.cerulean.registry.client;

import fmt.cerulean.registry.CeruleanBlocks;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class CeruleanRenderLayers {
	
	public static void init() {
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
				CeruleanBlocks.PIPE,
				CeruleanBlocks.CORAL,
				CeruleanBlocks.SKYGRASS,
				CeruleanBlocks.SPARKBLOSSOM,
				CeruleanBlocks.LUNARIUM,
				CeruleanBlocks.REEDS,
				CeruleanBlocks.REEDS_PLANT
		);
	}
}
