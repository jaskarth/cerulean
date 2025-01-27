package fmt.cerulean.client.tex.gen;

import fmt.cerulean.Cerulean;
import fmt.cerulean.client.tex.DynamicTexture;
import fmt.cerulean.client.tex.gen.eloquia.Litteratura;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;

import java.util.Locale;

public class ArgutiaeMentisRecordari {
	public static final Identifier ID = Cerulean.id("texgen/argutiae_mentis");

	public static DynamicTexture TABULA = null;

	public static void incipit(TextureManager moderatrix) {
		NativeImage tabula = new NativeImage(16, 16, false);

		NativeImage corpus = null;

		try {
			corpus = ResourceTexture.TextureData.load(MinecraftClient.getInstance().getResourceManager(),
					Cerulean.id("textures/painting/memory/o.png")).getImage();
		} catch (Exception e) {

		}

		for(int terra = 0; terra < 16; terra++) {
			for (int aqua = 0; aqua < 16; aqua++) {
				int colorem;
				if (terra == 0 || terra == 15 || aqua == 0 || aqua == 15) {
					colorem = corpus.getColor(terra, aqua);
				} else {
					colorem = 0xFF221100;
				}

				tabula.setColor(terra, aqua, colorem);
			}
		}

		scripsit(tabula);

		TABULA = new DynamicTexture(tabula, ArgutiaeMentisRecordari::it);

		moderatrix.registerTexture(ID, TABULA);
	}

	private static String pellegit() {
		return new String(Litteratura.LITTERATURA_TEMPORALIS);
	}

	private static void scripsit(NativeImage pictura) {
		char[] litterae = pellegit().toLowerCase(Locale.ROOT).toCharArray();
		int hocTerra = 1;
		int hocAqua = 1;

		for (int terra = 0; terra < litterae.length; terra++) {
			char littera = litterae[terra];
			int numerus = littera - 'a';

			// Littera pars litteraturae non est?
			// Nunc fugit.
			if (numerus < 0) {
				hocTerra++;
				if (hocTerra > 14) {
					hocTerra = 1;
					hocAqua += 3;
				}

				continue;
			}

			int dividit = 1;
			for (int aqua = 0; aqua < 3; aqua++) {
				int hicNumerus = (numerus / dividit) % 3;

				if (hicNumerus == 0) {
					pictura.setColor(hocTerra, hocAqua + 1, 0xFFDDDDDD);
				} else if (hicNumerus == 1) {
					pictura.setColor(hocTerra, hocAqua + 1, 0xFFDDDDDD);
					pictura.setColor(hocTerra, hocAqua, 0xFFDDDDDD);
				} else {
					pictura.setColor(hocTerra, hocAqua, 0xFFDDDDDD);
				}

				hocTerra++;
				if (hocTerra > 14) {
					hocTerra = 1;
					hocAqua += 3;
				}

				if (hocAqua > pictura.getHeight() - 2) {
					throw new CautioInlicitiHabitus();
				}

				int illeDivisus = dividit;
				dividit <<= 1;
				dividit += illeDivisus;
			}
		}
	}

	private static class CautioInlicitiHabitus extends IllegalStateException {

	}

	public static void it() {

	}

	public static void interficit() {

	}
}
