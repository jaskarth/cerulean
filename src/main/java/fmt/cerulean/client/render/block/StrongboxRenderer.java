package fmt.cerulean.client.render.block;

import fmt.cerulean.block.StrongboxBlock;
import fmt.cerulean.block.entity.StrongboxBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;

public class StrongboxRenderer implements BlockEntityRenderer<StrongboxBlockEntity> {
	private final Random random = Random.create();

	private final ItemRenderer itemRenderer;

	public StrongboxRenderer(BlockEntityRendererFactory.Context ctx) {
		itemRenderer = ctx.getItemRenderer();
	}

	@Override
	public void render(StrongboxBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if (entity.getCachedState().get(StrongboxBlock.WEAK)) {
			if (entity.hasItem) {
				matrices.push();

				matrices.translate(0.5f, 0.15f, 0.5f);

				long time = entity.getWorld().getTime();

				ItemStack itemStack = new ItemStack(Items.ECHO_SHARD);
				long seed = MathHelper.hashCode(entity.getPos());
				this.random.setSeed(seed);
				BakedModel bakedModel = this.itemRenderer.getModel(itemStack, entity.getWorld(), null, MathHelper.idealHash((int)seed));
				boolean bl = bakedModel.hasDepth();
				float j = MathHelper.sin(((float)time + tickDelta) / 10.0F) * 0.1F + 0.1F;
				float k = bakedModel.getTransformation().getTransformation(ModelTransformationMode.GROUND).scale.y();
				matrices.translate(0.0F, j + 0.25F * k, 0.0F);
				float l = ((float)time + tickDelta) / 20.0F;
				matrices.multiply(RotationAxis.POSITIVE_Y.rotation(l));

				ItemEntityRenderer.renderStack(this.itemRenderer, matrices, vertexConsumers, LightmapTextureManager.MAX_LIGHT_COORDINATE, itemStack, bakedModel, bl, this.random);

				matrices.pop();
			}

			if (entity.breakProgress > 0) {
				int o = MathHelper.clamp(entity.breakProgress / 10, 0, 9);
				VertexConsumer vertexConsumer2 = new OverlayVertexConsumer(
						MinecraftClient.getInstance().getBufferBuilders()
								.getEffectVertexConsumers()
								.getBuffer((RenderLayer) ModelLoader.BLOCK_DESTRUCTION_RENDER_LAYERS.get(o)), matrices.peek(), 1.0F
				);

				MinecraftClient.getInstance()
						.getBlockRenderManager()
						.renderDamage(entity.getWorld().getBlockState(entity.getPos()), entity.getPos(), entity.getWorld(), matrices, vertexConsumer2);
			}
		}
	}
}
