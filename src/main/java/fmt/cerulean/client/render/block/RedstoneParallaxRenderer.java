package fmt.cerulean.client.render.block;

import fmt.cerulean.block.RedstoneParallaxBlock;
import fmt.cerulean.block.entity.RedstoneParallaxBlockEntity;
import fmt.cerulean.client.ClientState;
import net.minecraft.block.BlockState;
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
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class RedstoneParallaxRenderer implements BlockEntityRenderer<RedstoneParallaxBlockEntity> {
	private final BlockRenderManager manager;

	public RedstoneParallaxRenderer(BlockEntityRendererFactory.Context ctx) {
		this.manager = ctx.getRenderManager();
	}

	@Override
	public void render(RedstoneParallaxBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		BlockModelRenderer.enableBrightnessCache();

		BlockPos pos = entity.getPos();
		BlockState state = entity.getCachedState();

		RenderLayer renderLayer = RenderLayers.getBlockLayer(state);

		if (ClientState.remember == ClientState.MemoryKind.SWEEP_K1) {
			ClientState.shouldEnact = true;
		} else if (ClientState.remember == ClientState.MemoryKind.ENACT_K2) {
			state = state.with(RedstoneParallaxBlock.POWERED, true);
			ClientState.forget = true;
		}

		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
		World world = entity.getWorld();
		this.manager
				.getModelRenderer()
				.render(world, this.manager.getModel(state), state, pos, matrices, vertexConsumer, true, Random.create(), state.getRenderingSeed(pos), overlay);

		BlockModelRenderer.disableBrightnessCache();
	}
}
