package fmt.cerulean.client.render;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class QuadEmitter {

	public static void buildBox(VertexConsumer buffer, MatrixStack matrices, float x1, float x2, float y1, float y2, float z1, float z2, int r, int g, int b, int a) {
		MatrixStack.Entry entry = matrices.peek();
		buildBottomFacing(buffer, entry, x1, x2, z1, z2, y1, r, g, b, a);
		buildTopFacing(buffer, entry, x1, x2, z1, z2, y2, r, g, b, a);

		buildSouthFacing(buffer, entry, x1, x2, y1, y2, z1, r, g, b, a);
		buildNorthFacing(buffer, entry, x1, x2, y1, y2, z2, r, g, b, a);

		buildWestFacing(buffer, entry, y1, y2, z1, z2, x1, r, g, b, a);
		buildEastFacing(buffer, entry, y1, y2, z1, z2, x2, r, g, b, a);
	}

	public static void buildTopFacing(VertexConsumer buffer, MatrixStack.Entry entry, float x1, float x2, float z1, float z2, float y, int r, int g, int b, int a) {
		Matrix4f model = entry.getPositionMatrix();
		Matrix3f normal = entry.getNormalMatrix();
		// -X, +Z
		buffer.vertex(model, x1, y, z2).color(r, g, b, a)
				.texture(0.0F, 1.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0.0F, 1.0F, 0.0F);
		// +X, +Z
		buffer.vertex(model, x2, y, z2).color(r, g, b, a)
				.texture(1.0F, 1.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0.0F, 1.0F, 0.0F);
		// +X, -Z
		buffer.vertex(model, x2, y, z1).color(r, g, b, a)
				.texture(1.0F, 0.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0.0F, 1.0F, 0.0F);
		// -X, -Z
		buffer.vertex(model, x1, y, z1).color(r, g, b, a)
				.texture(0.0F, 0.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0.0F, 1.0F, 0.0F);
	}

	public static void buildBottomFacing(VertexConsumer buffer, MatrixStack.Entry entry, float x1, float x2, float z1, float z2, float y, int r, int g, int b, int a) {
		Matrix4f model = entry.getPositionMatrix();
		Matrix3f normal = entry.getNormalMatrix();

		buffer.vertex(model, x1, y, z2).color(r, g, b, a)
				.texture(0.0F, 1.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0.0F, 1.0F, 0.0F);
		buffer.vertex(model, x1, y, z1).color(r, g, b, a)
				.texture(0.0F, 0.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0.0F, 1.0F, 0.0F);
		buffer.vertex(model, x2, y, z1).color(r, g, b, a)
				.texture(1.0F, 0.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0.0F, 1.0F, 0.0F);
		buffer.vertex(model, x2, y, z2).color(r, g, b, a)
				.texture(1.0F, 1.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0.0F, 1.0F, 0.0F);
	}

	public static void buildNorthFacing(VertexConsumer buffer, MatrixStack.Entry entry, float x1, float x2, float y1, float y2, float z, int r, int g, int b, int a) {
		Matrix4f model = entry.getPositionMatrix();
		Matrix3f normal = entry.getNormalMatrix();

		// -X, +Z
		buffer.vertex(model, x1, y2, z).color(r, g, b, a)
				.texture(0.0F, 1.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0.0F, 1.0F, 0.0F);
		// +X, +Z
		buffer.vertex(model, x2, y2, z).color(r, g, b, a)
				.texture(1.0F, 1.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0.0F, 1.0F, 0.0F);
		// +X, -Z
		buffer.vertex(model, x2, y1, z).color(r, g, b, a)
				.texture(1.0F, 0.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0.0F, 1.0F, 0.0F);
		// -X, -Z
		buffer.vertex(model, x1, y1, z).color(r, g, b, a)
				.texture(0.0F, 0.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0.0F, 1.0F, 0.0F);
	}

	public static void buildSouthFacing(VertexConsumer buffer, MatrixStack.Entry entry, float x1, float x2, float y1, float y2, float z, int r, int g, int b, int a) {
		Matrix4f model = entry.getPositionMatrix();
		Matrix3f normal = entry.getNormalMatrix();

		// Reverse

		buffer.vertex(model, x1, y2, z).color(r, g, b, a)
				.texture(0.0F, 1.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0.0F, 1.0F, 0.0F);
		buffer.vertex(model, x1, y1, z).color(r, g, b, a)
				.texture(0.0F, 0.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0.0F, 1.0F, 0.0F);
		buffer.vertex(model, x2, y1, z).color(r, g, b, a)
				.texture(1.0F, 0.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0.0F, 1.0F, 0.0F);
		buffer.vertex(model, x2, y2, z).color(r, g, b, a)
				.texture(1.0F, 1.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0.0F, 1.0F, 0.0F);
	}

	public static void buildEastFacing(VertexConsumer buffer, MatrixStack.Entry entry, float y1, float y2, float z1, float z2, float x, int r, int g, int b, int a) {
		Matrix4f model = entry.getPositionMatrix();
		Matrix3f normal = entry.getNormalMatrix();

		// -X, +Z
		buffer.vertex(model, x, y1, z2).color(r, g, b, a)
				.texture(0.0F, 1.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0.0F, 1.0F, 0.0F);
		// +X, +Z
		buffer.vertex(model, x, y2, z2).color(r, g, b, a)
				.texture(1.0F, 1.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0.0F, 1.0F, 0.0F);
		// +X, -Z
		buffer.vertex(model, x, y2, z1).color(r, g, b, a)
				.texture(1.0F, 0.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0.0F, 1.0F, 0.0F);
		// -X, -Z
		buffer.vertex(model, x, y1, z1).color(r, g, b, a)
				.texture(0.0F, 0.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0.0F, 1.0F, 0.0F);
	}

	public static void buildWestFacing(VertexConsumer buffer, MatrixStack.Entry entry, float y1, float y2, float z1, float z2, float x, int r, int g, int b, int a) {
		Matrix4f model = entry.getPositionMatrix();
		Matrix3f normal = entry.getNormalMatrix();

		// Reverse

		buffer.vertex(model, x, y1, z2).color(r, g, b, a)
				.texture(0.0F, 1.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0.0F, 1.0F, 0.0F);
		buffer.vertex(model, x, y1, z1).color(r, g, b, a)
				.texture(0.0F, 0.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0.0F, 1.0F, 0.0F);
		buffer.vertex(model, x, y2, z1).color(r, g, b, a)
				.texture(1.0F, 0.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0.0F, 1.0F, 0.0F);
		buffer.vertex(model, x, y2, z2).color(r, g, b, a)
				.texture(1.0F, 1.0F).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(entry, 0.0F, 1.0F, 0.0F);
	}
}