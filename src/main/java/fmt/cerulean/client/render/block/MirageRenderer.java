package fmt.cerulean.client.render.block;

import fmt.cerulean.block.entity.MirageBlockEntity;
import fmt.cerulean.client.ClientState;
import fmt.cerulean.client.render.CeruleanRenderTypes;
import fmt.cerulean.client.render.QuadEmitter;
import fmt.cerulean.registry.CeruleanBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
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

public class MirageRenderer implements BlockEntityRenderer<MirageBlockEntity> {
	private final BlockRenderManager manager;

	public MirageRenderer(BlockEntityRendererFactory.Context ctx) {
		this.manager = ctx.getRenderManager();
	}

	@Override
	public void render(MirageBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		boolean isHolding = player.isHolding(CeruleanBlocks.MIRAGE.asItem());
		if (!ClientState.forget && !isHolding) {
			return;
		}

		BlockModelRenderer.enableBrightnessCache();

		BlockPos pos = entity.getPos();
		BlockState state = entity.state;
		if (state == null) {
			state = Blocks.BEDROCK.getDefaultState();
		}

		RenderLayer renderLayer = RenderLayers.getMovingBlockLayer(state);

		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
		World world = entity.getWorld();
		this.manager
				.getModelRenderer()
				.render(world, this.manager.getModel(state), state, pos, matrices, vertexConsumer, false, Random.create(), state.getRenderingSeed(pos), overlay);

		if (isHolding) {
			int alpha = 80 + (int) (40f * Math.sin((System.currentTimeMillis() & 0xFFFF) * Math.PI / 511f));
			int r = 255;
			int g = 200;
			int b = 255;
			QuadEmitter.buildBox(vertexConsumers.getBuffer(CeruleanRenderTypes.TRANSLUCENT_BOX), matrices, 0, 1, 0, 1, 0, 1, r, g, b, alpha);
		}

		BlockModelRenderer.disableBrightnessCache();
	}
}
