package fmt.cerulean.client.tex.gen;

import fmt.cerulean.Cerulean;
import fmt.cerulean.client.tex.DynamicTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;

import java.util.Random;

public class StaticTexture {
	public static final Identifier ID = Cerulean.id("texgen/static");

	private static final Random RANDOM = new Random();

	public static NativeImage IMAGE;
	public static DynamicTexture TEXTURE = null;

	public static void init(TextureManager manager) {
		NativeImage image = new NativeImage(16, 16, false);

		for(int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				image.setColor(x, z, 0xFF_FFFFFF);
			}
		}

		TEXTURE = new DynamicTexture(image, StaticTexture::tick);

		manager.registerTexture(ID, TEXTURE);
	}

	public static void tick() {
		NativeImage image = TEXTURE.getImage();

		for(int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int v = RANDOM.nextInt(256);
				int color = 0xFF000000 | (v << 16) | (v << 8) | v;
				image.setColor(x, z, color);
			}
		}

		TEXTURE.upload();
	}

	public static void close() {

	}
}
