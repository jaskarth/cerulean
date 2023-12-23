package fmt.cerulean.registry;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class CeruleanRenderLayers {
	
	public static void init() {
		BlockRenderLayerMap.INSTANCE.putBlock(CeruleanBlocks.PIPE, RenderLayer.getCutout());
	}
}
