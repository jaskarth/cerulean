package fmt.cerulean.client.render.block;

import org.joml.Matrix4f;

import fmt.cerulean.block.AddressPlaqueBlock;
import fmt.cerulean.block.base.Addressable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

public class AddressableRenderer<T extends BlockEntity & Addressable> implements BlockEntityRenderer<T> {
	private final TextRenderer textRenderer;

	public AddressableRenderer(BlockEntityRendererFactory.Context ctx) {
		this.textRenderer = ctx.getTextRenderer();
	}

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.crosshairTarget instanceof BlockHitResult bhr && bhr.getBlockPos().equals(entity.getPos())) {
			Direction direction = entity.getCachedState().get(AddressPlaqueBlock.FACING);
			Text text = Text.literal(entity.getAddress());
			matrices.push();
			matrices.translate(0.5, 1.2, 0.5);
			matrices.translate(direction.getOffsetX() * 0.5, direction.getOffsetY() * 0.5, direction.getOffsetZ() * 0.5);
			if (direction == Direction.UP) {
				matrices.translate(0, -1, 0);
			}
			matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(180 + client.getCameraEntity().getYaw()));
			//matrices.multiply(this.dispatcher.getRotation());
			matrices.scale(0.025F, -0.025F, 0.025F);
			Matrix4f matrix4f = matrices.peek().getPositionMatrix();
			float f = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
			int j = (int)(f * 255.0F) << 24;
			float g = (float)(-textRenderer.getWidth(text) / 2);
			textRenderer.draw(
				text, g, 0, 553648127, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.SEE_THROUGH, j, light
			);
			textRenderer.draw(text, g, 0, Colors.WHITE, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);

			matrices.pop();
		}
	}
}
