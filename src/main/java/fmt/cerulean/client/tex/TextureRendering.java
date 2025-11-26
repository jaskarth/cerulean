package fmt.cerulean.client.tex;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.joml.Matrix4f;

public class TextureRendering {
	public static void drawTexturedQuad(
			MatrixStack matrices, Identifier texture,
			int x, int y, float u, float v,
			int width, int height, int textureWidth, int textureHeight,
			float red, float green, float blue, float alpha
	) {
		drawTexture(matrices, texture, x, x + width, y, y + height, 0, width, height, u, v, textureWidth, textureHeight, red, green, blue, alpha);
	}

	public static void drawTexture(
			MatrixStack matrices, Identifier texture, int x1, int x2, int y1, int y2, int z,
			int regionWidth, int regionHeight, float u, float v, int textureWidth, int textureHeight,
			float red, float green, float blue, float alpha
	) {
		int argb = ColorHelper.Argb.fromFloats(alpha, red, green, blue);

		drawTexturedQuad(
				matrices,
				texture,
				x1,
				x2,
				y1,
				y2,
				z,
				(u + 0.0F) / (float)textureWidth,
				(u + (float)regionWidth) / (float)textureWidth,
				(v + 0.0F) / (float)textureHeight,
				(v + (float)regionHeight) / (float)textureHeight,
				argb
		);
	}

	public static void drawTexture(
			MatrixStack matrices, Identifier texture, int x1, int x2, int y1, int y2, int z,
			int regionWidth, int regionHeight, float u, float v, int textureWidth, int textureHeight,
			int color
	) {
		drawTexturedQuad(
				matrices,
				texture,
				x1,
				x2,
				y1,
				y2,
				z,
				(u + 0.0F) / (float)textureWidth,
				(u + (float)regionWidth) / (float)textureWidth,
				(v + 0.0F) / (float)textureHeight,
				(v + (float)regionHeight) / (float)textureHeight,
				color
		);
	}

	public static void drawTexture(
			MatrixStack matrices, Identifier texture,
			int x, int y, int width, int height,
			float u, float v, int color,
			int regionWidth, int regionHeight, int textureWidth, int textureHeight
	) {

		drawTexture(matrices, texture, x, x + width, y, y + height, 0, regionWidth, regionHeight, u, v, textureWidth, textureHeight, color);
	}

	public static void drawTexturedQuad(
			MatrixStack matrices, Identifier texture,
			int x1, int x2, int y1, int y2, int z,
			float u1, float u2, float v1, float v2,
			int argb
	) {
		RenderSystem.setShaderTexture(0, texture);
		RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
		RenderSystem.enableBlend();
		Matrix4f matrix4f = matrices.peek().getPositionMatrix();
		BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
		bufferBuilder.vertex(matrix4f, (float)x1, (float)y1, (float)z).texture(u1, v1).color(argb);
		bufferBuilder.vertex(matrix4f, (float)x1, (float)y2, (float)z).texture(u1, v2).color(argb);
		bufferBuilder.vertex(matrix4f, (float)x2, (float)y2, (float)z).texture(u2, v2).color(argb);
		bufferBuilder.vertex(matrix4f, (float)x2, (float)y1, (float)z).texture(u2, v1).color(argb);
		BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
		RenderSystem.disableBlend();
	}
}
