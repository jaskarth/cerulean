package fmt.cerulean.client.render.frame;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.VertexSorter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

public class ScreenFramebuffer extends SimpleFramebuffer {
	private final int pixelWidth;
	private final int pixelHeight;
	private PostEffectProcessor blurPostProcessor;

	// Innate gui scale 3

	public ScreenFramebuffer(int width, int height, ResourceFactory resourceFactory) {
		super(width * 3, height * 3, true, MinecraftClient.IS_SYSTEM_MAC);
		this.pixelWidth = width;
		this.pixelHeight = height;

		try {
			this.blurPostProcessor = new PostEffectProcessor(MinecraftClient.getInstance().getTextureManager(), resourceFactory, this, Identifier.ofVanilla("shaders/post/blur.json"));
			this.blurPostProcessor.setupDimensions(textureWidth, textureHeight);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void clear() {
		setClearColor(0f, 0f, 0f, 0f);
		clear(MinecraftClient.IS_SYSTEM_MAC);
		beginWrite(true);
	}

	public void end() {
		endWrite();
		MinecraftClient.getInstance().getFramebuffer().beginWrite(true);
	}

	public ProjectedDrawContext setupFrameContext() {
		Matrix4f backupProj = RenderSystem.getProjectionMatrix();

		Matrix4f matrix4f = new Matrix4f().setOrtho(
			0.0F,
			(float)((double)textureWidth / 3.),
			(float)((double)textureHeight / 3.),
			0.0F,
			1000.0F,
			21000.0F);

		RenderSystem.setProjectionMatrix(matrix4f, VertexSorter.BY_Z);
		MinecraftClient client = MinecraftClient.getInstance();

		DrawContext context = new DrawContext(client, client.getBufferBuilders().getEntityVertexConsumers());

		return new ProjectedDrawContext(context, backupProj);
	}

	public void blur(float radius, float tickDelta) {
		MinecraftClient client = MinecraftClient.getInstance();

		blurPostProcessor.setUniforms("Radius", radius);
		blurPostProcessor.render(tickDelta);

		beginWrite(true);
	}

	public static class ProjectedDrawContext {
		private final DrawContext context;
		private final Matrix4f original;


		public ProjectedDrawContext(DrawContext context, Matrix4f original) {
			this.context = context;
			this.original = original;
		}

		public DrawContext context() {
			return context;
		}

		public void reset() {
			RenderSystem.setProjectionMatrix(original, VertexSorter.BY_DISTANCE);
		}
	}

	public int getPixelWidth() {
		return pixelWidth;
	}

	public int getPixelHeight() {
		return pixelHeight;
	}

	public static void drawFramebuffer(DrawContext context, ScreenFramebuffer framebuffer, int x, int y) {
		int x2 = x + framebuffer.getPixelWidth();
		int y2 = y + framebuffer.getPixelHeight();
		int z = 0;

		// Flip vertically
		int yy = y;
		y = y2;
		y2 = yy;

		float u1 = 0;
		float u2 = 1;
		float v1 = 0;
		float v2 = 1;

		RenderSystem.enableDepthTest();

		RenderSystem.setShaderTexture(0, framebuffer.getColorAttachment());
		RenderSystem.setShader(GameRenderer::getPositionTexProgram);
		Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
		BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

		RenderSystem.disableCull();

		bufferBuilder.vertex(matrix4f, (float)x, (float)y, (float)z).texture(u1, v1);
		bufferBuilder.vertex(matrix4f, (float)x, (float)y2, (float)z).texture(u1, v2);
		bufferBuilder.vertex(matrix4f, (float)x2, (float)y2, (float)z).texture(u2, v2);
		bufferBuilder.vertex(matrix4f, (float)x2, (float)y, (float)z).texture(u2, v1);
		BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());

		RenderSystem.enableCull();
	}
}
