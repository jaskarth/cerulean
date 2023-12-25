package fmt.cerulean.registry.client;

import fmt.cerulean.registry.CeruleanBlockEntities;
import fmt.cerulean.client.render.block.MimicBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class CeruleanBlockEntityRenderers {
	public static void init() {
		BlockEntityRendererFactories.register(CeruleanBlockEntities.MIMIC, MimicBlockEntityRenderer::new);
	}
}
