package fmt.cerulean.client.render.block;

import fmt.cerulean.block.entity.ProjectorBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class ProjectorRenderer implements BlockEntityRenderer<ProjectorBlockEntity> {
	@Override
	public void render(ProjectorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();

		matrices.translate(entity.transX, entity.transY, entity.transZ);

		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(entity.xp));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.yp));
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(entity.zp));

		matrices.scale(entity.scaleX, entity.scaleY, entity.scaleZ);

		matrices.pop();
	}
}
