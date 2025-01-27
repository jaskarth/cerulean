package fmt.cerulean.client.tex.gen;

import fmt.cerulean.Cerulean;
import fmt.cerulean.client.tex.DynamicTexture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;

import java.util.Random;

public class TabulaeMentisRecordari {
	public static final Identifier ID = Cerulean.id("texgen/tabulae_mentis");

	private static final Random FORTUNAE = new Random();

	private static final int COPIA_INMEMOR = 8;
	private static final int COPIA_MEMORIAE = 10;

	private static final NativeImage[] INMEMOR = new NativeImage[COPIA_INMEMOR];
	private static final NativeImage[] MEMORIAE = new NativeImage[COPIA_MEMORIAE];
	private static NativeImage CORPUS = null;

	public static DynamicTexture TABULA = null;

	private static int minuta = 5;
	private static boolean spes;
	private static int tempusUnum;
	private static int tempusDuum;

	public static void incipit(TextureManager moderatrix) {
		for (int i = 0; i < COPIA_INMEMOR; i++) {
			try {
				INMEMOR[i] = ResourceTexture.TextureData.load(MinecraftClient.getInstance().getResourceManager(),
						Cerulean.id("textures/painting/memory/a_" + (i + 1) + ".png")).getImage();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < COPIA_MEMORIAE; i++) {
			try {
				MEMORIAE[i] = ResourceTexture.TextureData.load(MinecraftClient.getInstance().getResourceManager(),
						Cerulean.id("textures/painting/memory/b_" + (i + 1) + ".png")).getImage();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			CORPUS = ResourceTexture.TextureData.load(MinecraftClient.getInstance().getResourceManager(),
					Cerulean.id("textures/painting/memory/o.png")).getImage();
		}catch (Exception e) {
			e.printStackTrace();
		}

		NativeImage tablua = new NativeImage(16, 16, false);

		for(int terra = 0; terra < 16; terra++) {
			for (int aqua = 0; aqua < 16; aqua++) {
				tablua.setColor(terra, aqua, 0xFF221100);
			}
		}

		TABULA = new DynamicTexture(tablua, TabulaeMentisRecordari::tick);

		moderatrix.registerTexture(ID, TABULA);
	}

	public static void tick() {
		minuta++;
		if (minuta < 5) {
			return;
		}
		minuta = 0;
		spes = !spes;

		int tempus = spes ? tempusUnum : tempusDuum;
		int acus;
		do {
			acus = FORTUNAE.nextInt(spes ? COPIA_INMEMOR : COPIA_MEMORIAE);
		} while (acus == tempus);

		if (spes) {
			tempusUnum = acus;
		} else {
			tempusDuum = acus;
		}

		NativeImage tablua = TABULA.getImage();

		for (int terra = 0; terra < 16; terra++) {
			for (int aqua = 0; aqua < 16; aqua++) {
				int colorem;
				if (terra == 0 || terra == 15 || aqua == 0 || aqua == 15) {
					colorem = CORPUS.getColor(terra, aqua);
				} else {
					colorem = (spes ? INMEMOR : MEMORIAE)[acus].getColor(terra, aqua);
				}

				tablua.setColor(terra, aqua, colorem);
			}
		}

		TABULA.upload();
	}

	public static void interficit() {

	}
}
