package fmt.cerulean.client.render.block;

import fmt.cerulean.block.entity.MimicBlockEntity;
import fmt.cerulean.client.render.CeruleanRenderTypes;
import fmt.cerulean.client.render.QuadEmitter;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class MimicBlockEntityRenderer implements BlockEntityRenderer<MimicBlockEntity> {
	private final BlockRenderManager manager;

	public MimicBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		this.manager = ctx.getRenderManager();
	}
	@Override
	public void render(MimicBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		BlockModelRenderer.enableBrightnessCache();

		BlockPos pos = entity.getPos();
		BlockState state = entity.state;
		if (state == null) {
			state = Blocks.BEDROCK.getDefaultState();
		}
		float v = entity.dist / 15.f;
		v = v * v * v;
		int alpha = MathHelper.clamp((int) (v * 255), 0, 255);

		RenderLayer renderLayer = RenderLayers.getMovingBlockLayer(state);

		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
		World world = entity.getWorld();
		this.manager
				.getModelRenderer()
				.render(world, this.manager.getModel(state), state, pos, matrices, vertexConsumer, false, Random.create(), state.getRenderingSeed(pos), overlay);

		QuadEmitter.buildBox(vertexConsumers.getBuffer(CeruleanRenderTypes.TRANSLUCENT_BOX), matrices, 0, 1, 0, 1, 0, 1, 255, 255, 255, alpha);

		BlockModelRenderer.disableBrightnessCache();
	}
}
