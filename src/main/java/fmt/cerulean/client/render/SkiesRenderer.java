package fmt.cerulean.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import fmt.cerulean.util.Counterful;
import fmt.cerulean.world.data.DimensionState;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

public class SkiesRenderer implements DimensionRenderingRegistry.SkyRenderer {
	private VertexBuffer vbo = null;

	@Override
	public void render(WorldRenderContext context) {
		if (vbo == null) {
			vbo = RenderVFX.renderStars(Tessellator.getInstance());
		}

		MatrixStack matrices = new MatrixStack();
		Matrix4f proj = context.projectionMatrix();
		matrices.multiplyPositionMatrix(context.positionMatrix());

		BackgroundRenderer.clearFog();

		matrices.push();
		float normGametime = MinecraftClient.getInstance().world.getTime() / 24000f;
		normGametime *= 0.3;
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(normGametime * 360.0F));
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((normGametime + 1.2f) * 360.0F));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((normGametime + 3.7f) * 360.0F));

		DimensionState state = Counterful.get(MinecraftClient.getInstance().player);
		float amt = 1;
		if (state.melancholy > 0) {
			amt = MathHelper.clamp(1 - ((state.melancholy - 20) / 120.f), 0, 1);
		}

//		RenderSystem.blendFuncSeparate(
//				GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO
//		);

		RenderSystem.disableBlend();
		RenderSystem.setShaderColor(amt, amt, amt, amt);
		RenderSystem.disableDepthTest();
		vbo.bind();
		vbo.draw(matrices.peek().getPositionMatrix(), proj, GameRenderer.getPositionColorProgram());
		VertexBuffer.unbind();
		matrices.pop();
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableDepthTest();

		RenderSystem.setShaderColor(1, 1, 1, 1);

//		fogCallback.run();
	}
}
