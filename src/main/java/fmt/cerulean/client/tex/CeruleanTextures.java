package fmt.cerulean.client.tex;

import fmt.cerulean.client.tex.gen.ArgutiaeMentisRecordari;
import fmt.cerulean.client.tex.gen.DynamicEyeTexture;
import fmt.cerulean.client.tex.gen.TabulaeMentisRecordari;
import net.minecraft.client.texture.TextureManager;

public class CeruleanTextures {
	public static void init(TextureManager manager) {
		DynamicEyeTexture.init(manager);
		ArgutiaeMentisRecordari.incipit(manager);
		TabulaeMentisRecordari.incipit(manager);
	}

	public static void close() {
		DynamicEyeTexture.close();
		ArgutiaeMentisRecordari.interficit();
		TabulaeMentisRecordari.interficit();
	}
}
