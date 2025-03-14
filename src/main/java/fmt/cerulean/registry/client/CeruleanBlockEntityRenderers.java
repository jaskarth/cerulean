package fmt.cerulean.registry.client;

import fmt.cerulean.registry.CeruleanBlockEntities;
import fmt.cerulean.client.render.block.AddressableRenderer;
import fmt.cerulean.client.render.block.FauxBlockEntityRenderer;
import fmt.cerulean.client.render.block.MimicRenderer;
import fmt.cerulean.client.render.block.SelfCollapsingCubeRenderer;
import fmt.cerulean.client.render.block.StrongboxRenderer;
import fmt.cerulean.client.render.block.MirageRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class CeruleanBlockEntityRenderers {
	public static void init() {
		BlockEntityRendererFactories.register(CeruleanBlockEntities.MIMIC, MimicRenderer::new);
		BlockEntityRendererFactories.register(CeruleanBlockEntities.FAUX, FauxBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(CeruleanBlockEntities.STRONGBOX, StrongboxRenderer::new);
		BlockEntityRendererFactories.register(CeruleanBlockEntities.SELF_COLLAPSING_CUBE, SelfCollapsingCubeRenderer::new);
		BlockEntityRendererFactories.register(CeruleanBlockEntities.ADDRESS_PLAQUE, AddressableRenderer::new);
		BlockEntityRendererFactories.register(CeruleanBlockEntities.FLAG, AddressableRenderer::new);
		BlockEntityRendererFactories.register(CeruleanBlockEntities.MIRAGE, MirageRenderer::new);
	}
}
