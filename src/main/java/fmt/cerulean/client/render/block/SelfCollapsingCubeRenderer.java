package fmt.cerulean.client.render.block;

import java.util.List;

import fmt.cerulean.Cerulean;
import fmt.cerulean.block.entity.SelfCollapsingCubeEntity;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class SelfCollapsingCubeRenderer implements BlockEntityRenderer<SelfCollapsingCubeEntity> {
	private final BlockRenderManager manager;
	private final ItemRenderer itemRenderer;

	public SelfCollapsingCubeRenderer(BlockEntityRendererFactory.Context ctx) {
		this.manager = ctx.getRenderManager();
		this.itemRenderer = ctx.getItemRenderer();
	}

	@Override
	public void render(SelfCollapsingCubeEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		BlockModelRenderer.enableBrightnessCache();

		BlockPos pos = entity.getPos();
		BlockState state = entity.getCachedState();

		RenderLayer renderLayer = RenderLayers.getMovingBlockLayer(state);

		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
		World world = entity.getWorld();

		List<ItemStack> stacks = entity.getFilterStacks();
		List<ItemStack> buffer = entity.getBufferStacks();

		for (int i = 0; i < 8; i++) {
			BakedModel cubie;
			try {
				if (stacks.get(i).isEmpty()) {
					cubie = MinecraftClient.getInstance().getBakedModelManager().getModel(Cerulean.id("item/cubie/gray_" + i));
				} else if (!buffer.get(i).isEmpty()) {
					cubie = MinecraftClient.getInstance().getBakedModelManager().getModel(Cerulean.id("item/cubie/green_" + i));
				} else {
					cubie = MinecraftClient.getInstance().getBakedModelManager().getModel(Cerulean.id("item/cubie/blue_" + i));
				}
				this.manager
					.getModelRenderer()
					.render(world, cubie, state, pos, matrices, vertexConsumer, false, Random.create(), state.getRenderingSeed(pos), overlay);
			} catch (Exception e) {
			}
		}

		matrices.push();
		matrices.translate(0.5f, 0.5f, 0.5f);
		matrices.push();
		matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(90));
		renderFace(stacks, IntList.of(0, 2, 4, 6), world, matrices, vertexConsumers, overlay);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
		renderFace(stacks, IntList.of(2, 3, 6, 7), world, matrices, vertexConsumers, overlay);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
		renderFace(stacks, IntList.of(3, 1, 7, 5), world, matrices, vertexConsumers, overlay);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
		renderFace(stacks, IntList.of(1, 0, 5, 4), world, matrices, vertexConsumers, overlay);
		matrices.pop();
		matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(90));
		renderFace(stacks, IntList.of(0, 1, 2, 3), world, matrices, vertexConsumers, overlay);
		matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(180));
		renderFace(stacks, IntList.of(6, 7, 4, 5), world, matrices, vertexConsumers, overlay);
		matrices.pop();

		BlockModelRenderer.disableBrightnessCache();
	}

	@Override
	public int getRenderDistance() {
		return Integer.MAX_VALUE;
	}

	private void renderFace(List<ItemStack> stacks, IntList indices, World world, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int overlay) {
		matrices.push();
		matrices.translate(0.0f, 0.0f, 0.5001f);

		for (int i = 0; i < 4; i++) {
			matrices.push();
			matrices.translate(((i & 1) != 0) ? 0.25 : -0.25, (i > 1) ? -0.25 : 0.25, 0.0f);
			matrices.scale(0.4f, 0.4f, 0.0001f);
			this.itemRenderer.renderItem(stacks.get(indices.getInt(i)), ModelTransformationMode.GUI, 15728880, overlay, matrices, vertexConsumers, world, overlay);
			matrices.pop();
		}
		matrices.pop();
	}
}
