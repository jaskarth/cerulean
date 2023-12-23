package fmt.cerulean.client.render;

import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

public class RenderVFX {
	public static VertexBuffer renderStars(BufferBuilder buffer) {
		VertexBuffer vbo = new VertexBuffer(VertexBuffer.Usage.STATIC);
		Random random = Random.create(10842L);
		buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

		for(int i = 0; i < 7500; ++i) {
			double d = (double)(random.nextFloat() * 2.0F - 1.0F);
			double e = (double)(random.nextFloat() * 2.0F - 1.0F);
			double f = (double)(random.nextFloat() * 2.0F - 1.0F);
			double g = (0.05F + (random.nextFloat() * random.nextFloat() * 0.30F));
			double h = d * d + e * e + f * f;
			if (h < 1.0 && h > 0.01) {
				h = 1.0 / Math.sqrt(h);
				d *= h;
				e *= h;
				f *= h;
				double j = d * 100.0;
				double k = e * 100.0;
				double l = f * 100.0;
				double m = Math.atan2(d, f);
				double n = Math.sin(m);
				double o = Math.cos(m);
				double p = Math.atan2(Math.sqrt(d * d + f * f), e);
				double q = Math.sin(p);
				double r = Math.cos(p);
				double s = random.nextDouble() * Math.PI * 2.0;
				double t = Math.sin(s);
				double u = Math.cos(s);

				int color = MathHelper.hsvToRgb(random.nextFloat(), random.nextFloat() * 0.25f + 0.125f, 0.8f + random.nextFloat() * 0.225f + 0.02f);

				for(int v = 0; v < 4; ++v) {
					double w = 0.0;
					double x = (double)((v & 2) - 1) * g;
					double y = (double)((v + 1 & 2) - 1) * g;
					double z = 0.0;
					double aa = x * u - y * t;
					double ab = y * u + x * t;
					double ad = aa * q + 0.0 * r;
					double ae = 0.0 * q - aa * r;
					double af = ae * n - ab * o;
					double ah = ab * n + ae * o;
					buffer.vertex(j + af, k + ad, l + ah).color((color >> 16) & 0xFF, (color >> 8) & 0xFF, (color >> 0) & 0xFF, 255).next();
				}
			}
		}

		vbo.bind();
		vbo.upload(buffer.end());
		VertexBuffer.unbind();

		return vbo;
	}
}
