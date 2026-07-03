package fmt.cerulean.client.render.block;

import fmt.cerulean.block.ProjectorBlock;
import fmt.cerulean.block.entity.ProjectorBlockEntity;
import fmt.cerulean.client.ClientState;
import fmt.cerulean.client.tex.gen.StaticTexture;
import fmt.cerulean.item.component.ColorTriplex;
import fmt.cerulean.item.component.PhotoComponent;
import fmt.cerulean.registry.CeruleanItemComponents;
import fmt.cerulean.registry.CeruleanItems;
import fmt.cerulean.util.Powerful;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class ProjectorRenderer implements BlockEntityRenderer<ProjectorBlockEntity> {
	public ProjectorRenderer(BlockEntityRendererFactory.Context ctx) {

	}

	@Override
	public void render(ProjectorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if (!entity.enabled) {
			return;
		}

		matrices.push();

		Direction direction = entity.getCachedState().get(ProjectorBlock.FACING);

		int scale = entity.dist;
		double oz = direction.getOffsetX() == -1 || direction.getOffsetY() == 1 ? 1 : 0;
		double ox = direction.getOffsetZ() == 1 ? 1 : 0;
		double oy = 0;
		double offset = (entity.size - 1) / 2.0;

		if (direction.getAxis() != Direction.Axis.Y) {
			// horizontal
			Direction ccw = direction.rotateYCounterclockwise();
			ox += (ccw.getOffsetX() * offset);
			oz += (ccw.getOffsetZ() * offset);
			oy -= offset;
		} else if (direction == Direction.UP) {
			ox -= offset;
			oz += offset;
		} else if (direction == Direction.DOWN) {
			ox -= offset;
			oz -= offset;
		}

		if (direction.getDirection() == Direction.AxisDirection.POSITIVE) {
			scale++;
		}

		matrices.translate(direction.getOffsetX() * scale + ox, direction.getOffsetY() * scale + oy, direction.getOffsetZ() * scale + oz);

		// flip the photo the right way round
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
		// horizontal rotation
		float hRot = switch (direction) {
			case EAST -> 90;
			case SOUTH -> 180;
			case WEST -> 270;
			default -> 0;
		};
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(hRot));
		// vertical rotation
		float vRot = switch (direction) {
			case UP -> 90;
			case DOWN -> 270;
			default -> 0;
		};
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(vRot));

		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(entity.rotX));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.rotY));
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(entity.rotZ));

		matrices.scale(entity.size, entity.size, 0);

		ItemStack stack = entity.slots.get(0);
		if (stack.isOf(CeruleanItems.PHOTOGRAPH)) {
			PhotoComponent photo = stack.get(CeruleanItemComponents.PHOTO);
			ColorTriplex color = stack.get(CeruleanItemComponents.COLOR_TRIPLEX);

			if (photo != null) {
				Identifier id;
				if (photo.id() == -1) {
					id = ClientState.PHOTOS.getSpecial(stack.get(CeruleanItemComponents.PHOTO_SPECIAL));
					color = null;
				} else {
					id = ClientState.PHOTOS.getId(photo.id());

					if (id == null) {
						id = StaticTexture.ID;
						ClientState.PHOTOS.ask(photo.id());
					}
				}

				matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));

				Matrix4f matrix4f = matrices.peek().getPositionMatrix();
				VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getTextPolygonOffset(id));

				int c = color == null ? (entity.alpha << 24) : (entity.alpha << 24) | color.toBGR();
				vertexConsumer.vertex(matrix4f, 0.0F, 1.0F, -0.01F).color(c).texture(0.0F, 0.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE);
				vertexConsumer.vertex(matrix4f, 1.0F, 1.0F, -0.01F).color(c).texture(1.0F, 0.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE);
				vertexConsumer.vertex(matrix4f, 1.0F, 0.0F, -0.01F).color(c).texture(1.0F, 1.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE);
				vertexConsumer.vertex(matrix4f, 0.0F, 0.0F, -0.01F).color(c).texture(0.0F, 1.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE);

				vertexConsumer.vertex(matrix4f, 0.0F, 1.0F, -0.01F).color(c).texture(0.0F, 0.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE);
				vertexConsumer.vertex(matrix4f, 0.0F, 0.0F, -0.01F).color(c).texture(0.0F, 1.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE);
				vertexConsumer.vertex(matrix4f, 1.0F, 0.0F, -0.01F).color(c).texture(1.0F, 1.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE);
				vertexConsumer.vertex(matrix4f, 1.0F, 1.0F, -0.01F).color(c).texture(1.0F, 0.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE);
			}
		}

		matrices.pop();
	}
}
