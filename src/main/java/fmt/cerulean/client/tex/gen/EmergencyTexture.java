package fmt.cerulean.client.tex.gen;

import fmt.cerulean.Cerulean;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EmergencyTexture {
	private static final Map<EmergencyDims, TextureData> TEXTURES = new HashMap<>();

	public static Identifier getWithSize(int x, int z, int idx) {
		EmergencyDims dims = new EmergencyDims(x, z, idx);
		TextureData data = TEXTURES.get(dims);

		if (data != null) {
			return data.id;
		}

		return generateWithSize(x, z, idx);
	}

	private static Identifier generateWithSize(int x, int z, int idx) {
		int sizeX = x * 36;
		int sizeZ = z * 36;

		NativeImage image = new NativeImage(sizeX, sizeZ, false);

		// calloc
		for (int ax = 0; ax < sizeX; ax++) {
			for (int az = 0; az < sizeZ; az++) {
				image.setColor(ax, az, 0);
			}
		}

		NativeImage hexa1 = getImage(Cerulean.id("textures/gui/hexa1.png"));
		NativeImage hexa2 = getImage(Cerulean.id("textures/gui/hexa2.png"));
		NativeImage hexa3 = getImage(Cerulean.id("textures/gui/hexa3.png"));
		NativeImage hexa4 = getImage(Cerulean.id("textures/gui/hexa4.png"));
		NativeImage hexa5 = getImage(Cerulean.id("textures/gui/hexa5.png"));

		Random random = new Random();

		for (int ax = 0; ax < x; ax++) {
			for (int az = 0; az < z; az++) {

				int ox = ax * 26;
				if ((az & 1) == 0) {
					ox += 13;
				}

				int oz;
				if ((az & 1) == 0) {
					oz = (az / 2) * 18;
				} else {
					oz = az * 9;
				}

				double v = random.nextDouble();
				NativeImage img;
				if (v < 0.4) {
					img = hexa1;
				} else if (v < 0.8) {
					img = hexa2;
				} else if (v < 0.95) {
					img = hexa3;
				} else if (v < 0.975) {
					img = hexa4;
				} else {
					img = hexa5;
				}

				for (int tx = 0; tx < 19; tx++) {
					for (int tz = 0; tz < 19; tz++) {
						int color = img.getColor(tx, tz);
						int alpha = (color >> 24) & 0xFF;
						if (alpha == 0) {
							continue;
						}

						image.setColor(ox + tx, oz + tz, color);
					}
				}
			}
		}

		Identifier id = id(x, z, idx);

		NativeImageBackedTexture texture = new NativeImageBackedTexture(image);
		MinecraftClient.getInstance().getTextureManager().registerTexture(id, texture);

		TEXTURES.put(new EmergencyDims(x, z, idx), new TextureData(id, texture));

		return id;
	}

	record EmergencyDims(int x, int z, int idx) {

	}

	record TextureData(Identifier id, NativeImageBackedTexture tex) {

	}

	private static Identifier id(int x, int z, int idx) {
		return Cerulean.id("texgen/emergency_" + x + "_" + z + "_" + idx);
	}

	private static NativeImage getImage(Identifier id) {
		ResourceTexture.TextureData data = ResourceTexture.TextureData.load(MinecraftClient.getInstance().getResourceManager(), id);

		try {
			return data.getImage();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void close() {

	}
}
