package fmt.cerulean.registry.client;

import fmt.cerulean.registry.CeruleanBlockEntities;
import fmt.cerulean.client.render.block.FauxBlockEntityRenderer;
import fmt.cerulean.client.render.block.MimicRenderer;
import fmt.cerulean.client.render.block.StrongboxRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class CeruleanBlockEntityRenderers {
	public static void init() {
		BlockEntityRendererFactories.register(CeruleanBlockEntities.MIMIC, MimicRenderer::new);
		BlockEntityRendererFactories.register(CeruleanBlockEntities.FAUX, FauxBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(CeruleanBlockEntities.STRONGBOX, StrongboxRenderer::new);
	}
}
