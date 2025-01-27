package fmt.cerulean.mixin.client;

import fmt.cerulean.client.render.Prism;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TextRenderer.class)
public abstract class MixinTextRenderer implements Prism {

	@Shadow public abstract String mirror(String text);

	@Shadow @Final private static Vector3f FORWARD_SHIFT;

	@Shadow protected abstract float drawLayer(String text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider, TextRenderer.TextLayerType layerType, int underlineColor, int light);

	@Shadow public abstract boolean isRightToLeft();

	@Override
	public int cerulean$drawTransparentText(String text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, TextRenderer.TextLayerType layerType, int backgroundColor, int light) {
		if (isRightToLeft()) {
			text = this.mirror(text);
		}

		Matrix4f matrix4f = new Matrix4f(matrix);
		if (shadow) {
			this.drawLayer(text, x, y, color, true, matrix, vertexConsumers, layerType, backgroundColor, light);
			matrix4f.translate(FORWARD_SHIFT);
		}

		x = this.drawLayer(text, x, y, color, false, matrix4f, vertexConsumers, layerType, backgroundColor, light);
		return (int)x + (shadow ? 1 : 0);
	}
}
