package fmt.cerulean.client.tex.gen;

import fmt.cerulean.Cerulean;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.util.*;

public class Oxidator {
	private static final NavigableMap<Double, Integer> COLORS = new TreeMap<>();
	private static final Map<Identifier, Identifier> REMAPPED = new HashMap<>();

	public static Identifier oxidate(Identifier texture) {
		Identifier res = REMAPPED.get(texture);
		if (res != null) {
			return res;
		}

		if (MinecraftClient.getInstance().getTextureManager().getTexture(texture) instanceof TextureRetriever tex) {
			NativeImage img = tex.cerulean$getTexture();
			if (img != null) {
				NativeImage newImage = new NativeImage(img.getFormat(), img.getWidth(), img.getHeight(), false);
				newImage.copyFrom(img);

				int[] pixels = img.copyPixelsRgba();

				for (int x = 0; x < img.getWidth(); x++) {
					for (int z = 0; z < img.getHeight(); z++) {
						int rgba = pixels[x + z * 64];

						int r = (rgba >> 0) & 0xFF;
						int g = (rgba >> 8) & 0xFF;
						int b = (rgba >> 16) & 0xFF;

						double brightness = Math.max(Math.max(r, g), b) / 255.0;

						int v = COLORS.floorEntry(brightness).getValue();
						int ba = (v >> 0) & 0xFF;
						int ga = (v >> 8) & 0xFF;
						int ra = (v >> 16) & 0xFF;

						if (isMagic(x, z)) {
							ra = r;
							ga = g;
							ba = b;
						}

						newImage.setColor(x, z, (rgba >> 24) << 24 | ba << 16 | ga << 8 | ra);
					}
				}

				Identifier newId = Cerulean.id(texture.getPath() + "_oxidized");

				MinecraftClient.getInstance().getTextureManager().registerTexture(newId, new NativeImageBackedTexture(newImage));
				REMAPPED.put(texture, newId);
				return newId;
			}
		}

		return texture;
	}

	private static boolean isMagic(int x, int z) {
		return x >= 0 && x <= 3 && z >= 32 && z <= 35;
	}

	public interface TextureRetriever {
		NativeImage cerulean$getTexture();
	}

	static {
		COLORS.put(0., 0xFF2A4C3E);
		COLORS.put(0.07775263951734535, 0xFF2A4C3E);
		COLORS.put(0.15015082956259423, 0xFF325849);
		COLORS.put(0.28288084464555047, 0xFF396E59);
		COLORS.put(0.3251131221719456, 0xFF43755F);
		COLORS.put(0.39751131221719443, 0xFF3E816B);
		COLORS.put(0.45180995475113106, 0xFF3D8A69);
		COLORS.put(0.5121417797888387, 0xFF4C9484);
		COLORS.put(0.5543740573152337, 0xFF4B9B7F);
		COLORS.put(0.5905731523378581, 0xFF53A178);
		COLORS.put(0.6931372549019607, 0xFF59B292);
		COLORS.put(0.7655354449472096, 0xFF64BEAC);
		COLORS.put(0.8319004524886876, 0xFF73C9BB);
	}
}
