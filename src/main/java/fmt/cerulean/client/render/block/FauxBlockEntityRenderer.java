package fmt.cerulean.client.render.block;

import fmt.cerulean.block.entity.FauxBlockEntity;
import fmt.cerulean.client.render.CeruleanRenderTypes;
import fmt.cerulean.client.render.QuadEmitter;
import fmt.cerulean.registry.CeruleanBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class FauxBlockEntityRenderer implements BlockEntityRenderer<FauxBlockEntity> {
	private final BlockRenderManager manager;

	public FauxBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		this.manager = ctx.getRenderManager();
	}

	@Override
	public void render(FauxBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if (entity.canReckon()) {
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
		
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player.isHolding(CeruleanBlocks.FAUX.asItem())) {
			int alpha = 80 + (int) (40f * Math.sin((System.currentTimeMillis() & 0xFFFF) * Math.PI / 511f));
			int r = 255;
			int g = 200;
			int b = 255;
			if (!entity.carefulNow.isEmpty()) {
				r = 200;
			}
			QuadEmitter.buildBox(vertexConsumers.getBuffer(CeruleanRenderTypes.TRANSLUCENT_BOX), matrices, 0, 1, 0, 1, 0, 1, r, g, b, alpha);
		}

		BlockModelRenderer.disableBrightnessCache();
	}

	@Override
	public int getRenderDistance() {
		return Integer.MAX_VALUE;
	}

	public static int intuitDetachment(BlockPos pos) {
		return (int) MinecraftClient.getInstance().player.getPos().distanceTo(pos.toCenterPos());
	}

	public static boolean adrenaline(BlockPos blockPos) {
		Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
		Vec3d camPos = camera.getPos();
		Vec3d pos = blockPos.toCenterPos();
		if (pos.distanceTo(camPos) < 2) {
			return true;
		}
		Vec3d dir = camPos.subtract(pos).normalize();
		float dot = camera.getHorizontalPlane().dot(dir.toVector3f());
		if (dot < 0.2f) {
			return true;
		}
		return false;
	}
}
