package fmt.cerulean.client.tex.gen;

import fmt.cerulean.Cerulean;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.*;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.Random;

public class DynamicEyeTexture {
	public static final Identifier ID = Cerulean.id("texgen/dynamic_eye");

	private static final Random RANDOM = new Random();

	public static SpriteContents SPRITE;
	public static NativeImage IMAGE;

	private static NativeImage INNER;

	private static int lastIdx = 0;

	public static void init(TextureManager manager) {
		ResourceTexture.TextureData data = ResourceTexture.TextureData.load(MinecraftClient.getInstance().getResourceManager(),
				Cerulean.id("textures/item/eye_of_vendor_core.png"));
		try {
			INNER = data.getImage();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for(int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int color = INNER.getColor(x, z);
				IMAGE.setColor(x, z, color);
			}
		}
	}

	public static void tick() {
		NativeImage image = IMAGE;

		// Reset texture
		for(int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int color = INNER.getColor(x, z);
				image.setColor(x, z, color);
			}
		}

		int idx;
		do {
			idx = RANDOM.nextInt(5);
		} while (idx == lastIdx);
		lastIdx = idx;

		int x = switch (idx) {
			case 0, 1, 2 -> 8;
			case 3 -> 7;
			case 4 -> 9;
			default -> throw new IllegalStateException("Impossible state!");
		};

		int z = switch (idx) {
			case 0 -> 7;
			case 2 -> 9;
			case 1, 3, 4 -> 8;
			default -> throw new IllegalStateException("Impossible state!");
		};

		image.setColor(x, z, 0xFF_bbd14d); // BGR
	}

	public static void close() {
		INNER.close();
	}
}
