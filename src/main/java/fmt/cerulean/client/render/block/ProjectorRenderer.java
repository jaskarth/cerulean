package fmt.cerulean.client.render.block;

import fmt.cerulean.block.entity.ProjectorBlockEntity;
import fmt.cerulean.client.ClientState;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

public class ProjectorRenderer implements BlockEntityRenderer<ProjectorBlockEntity> {
	public ProjectorRenderer(BlockEntityRendererFactory.Context ctx) {

	}

	@Override
	public void render(ProjectorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();

		matrices.translate(entity.transX, entity.transY, entity.transZ);

		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(entity.xp));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.yp));
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(entity.zp));

		matrices.scale(entity.scaleX, entity.scaleY, entity.scaleZ);

		if (!entity.name.isEmpty()) {
			Matrix4f matrix4f = matrices.peek().getPositionMatrix();
			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getText(ClientState.PHOTOS.getClient(entity.name)));

			int c = (entity.alpha << 24) | 0xFFFFFF;

			vertexConsumer.vertex(matrix4f, 0.0F, 1.0F, -0.01F).color(c).texture(0.0F, 0.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE);
			vertexConsumer.vertex(matrix4f, 1.0F, 1.0F, -0.01F).color(c).texture(1.0F, 0.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE);
			vertexConsumer.vertex(matrix4f, 1.0F, 0.0F, -0.01F).color(c).texture(1.0F, 1.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE);
			vertexConsumer.vertex(matrix4f, 0.0F, 0.0F, -0.01F).color(c).texture(0.0F, 1.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE);

			vertexConsumer.vertex(matrix4f, 0.0F, 1.0F, -0.01F).color(c).texture(0.0F, 0.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE);
			vertexConsumer.vertex(matrix4f, 0.0F, 0.0F, -0.01F).color(c).texture(0.0F, 1.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE);
			vertexConsumer.vertex(matrix4f, 1.0F, 0.0F, -0.01F).color(c).texture(1.0F, 1.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE);
			vertexConsumer.vertex(matrix4f, 1.0F, 1.0F, -0.01F).color(c).texture(1.0F, 0.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE);
		}

		matrices.pop();
	}
}
