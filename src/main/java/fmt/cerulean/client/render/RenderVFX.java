package fmt.cerulean.client.render;

import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

public class RenderVFX {
	public enum StarsMode {
		REGULAR,
		SPECIAL
	}

	public static VertexBuffer renderStars(Tessellator tessellator, StarsMode mode) {
		VertexBuffer vbo = new VertexBuffer(VertexBuffer.Usage.STATIC);
		Random random = Random.create(10842L);
		BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

		if (mode == StarsMode.REGULAR) {
			for (int i = 0; i < 15000; ++i) {
				double x = (double) (random.nextFloat() * 2.0F - 1.0F);
				double y = (double) (random.nextFloat() * 2.0F - 1.0F);
				double z = (double) (random.nextFloat() * 2.0F - 1.0F);
				double size = (0.05F + (random.nextFloat() * random.nextFloat() * random.nextFloat() * 0.55F));
				int color = MathHelper.hsvToRgb(random.nextFloat(), random.nextFloat() * 0.25f + 0.125f, 0.8f + random.nextFloat() * 0.225f + 0.02f);

				renderStar(x, y, z, random, size, buffer, color);
			}
		} else if (mode == StarsMode.SPECIAL) {
			renderStar(0, 0.99, 0, random, 0.75, buffer, 0xFF0000);
		}

		vbo.bind();
		vbo.upload(buffer.end());
		VertexBuffer.unbind();

		return vbo;
	}

	private static void renderStar(double x, double y, double z, Random random, double size, BufferBuilder buffer, int color) {
		double dist = 100.0;

		double pDist = x * x + y * y + z * z;
		if (pDist < 1.0 && pDist > 0.01) {
			pDist = 1.0 / Math.sqrt(pDist);
			x *= pDist;
			y *= pDist;
			z *= pDist;
			double startX = x * dist;
			double startY = y * dist;
			double startZ = z * dist;
			double m = Math.atan2(x, z);
			double n = Math.sin(m);
			double o = Math.cos(m);
			double p = Math.atan2(Math.sqrt(x * x + z * z), y);
			double q = Math.sin(p);
			double r = Math.cos(p);
			double s = random.nextDouble() * Math.PI * 2.0;
			double t = Math.sin(s);
			double u = Math.cos(s);

			for(int v = 0; v < 4; ++v) {
				double w = 0.0;
				double ax = (double)((v & 2) - 1) * size;
				double ay = (double)((v + 1 & 2) - 1) * size;
				double aa = ax * u - ay * t;
				double ab = ay * u + ax * t;
				double addY = aa * q + 0.0 * r;
				double ae = 0.0 * q - aa * r;
				double addX = ae * n - ab * o;
				double addZ = ab * n + ae * o;
				buffer.vertex((float) (startX + addX), (float) (startY + addY), (float) (startZ + addZ))
						.color((color >> 16) & 0xFF, (color >> 8) & 0xFF, (color >> 0) & 0xFF, 255);
			}
		}
	}
}
