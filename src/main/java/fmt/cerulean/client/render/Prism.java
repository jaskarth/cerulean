package fmt.cerulean.client.render;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import org.joml.Matrix4f;

public interface Prism {
	int cerulean$drawTransparentText(
			String text,
			float x,
			float y,
			int color,
			boolean shadow,
			Matrix4f matrix,
			VertexConsumerProvider vertexConsumers,
			TextRenderer.TextLayerType layerType,
			int backgroundColor,
			int light
	);
}
