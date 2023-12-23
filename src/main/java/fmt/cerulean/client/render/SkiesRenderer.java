package fmt.cerulean.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

public class SkiesRenderer implements DimensionRenderingRegistry.SkyRenderer {
	private VertexBuffer vbo = null;

	@Override
	public void render(WorldRenderContext context) {
		if (vbo == null) {
			vbo = RenderVFX.renderStars(Tessellator.getInstance().getBuffer());
		}

		MatrixStack matrices = context.matrixStack();
		Matrix4f proj = context.projectionMatrix();

		BackgroundRenderer.clearFog();

		matrices.push();
		float normGametime = MinecraftClient.getInstance().world.getTime() / 24000f;
		normGametime *= 0.3;
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(normGametime * 360.0F));
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((normGametime + 1.2f) * 360.0F));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((normGametime + 3.7f) * 360.0F));

		RenderSystem.setShaderColor(1, 1, 1, 1);
		vbo.bind();
		vbo.draw(matrices.peek().getPositionMatrix(), proj, GameRenderer.getPositionColorProgram());
		VertexBuffer.unbind();
		matrices.pop();

//		fogCallback.run();
	}
}
