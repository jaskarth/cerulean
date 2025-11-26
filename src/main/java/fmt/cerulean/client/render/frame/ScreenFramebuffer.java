package fmt.cerulean.client.render.frame;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.VertexSorter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.gui.DrawContext;
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
}
