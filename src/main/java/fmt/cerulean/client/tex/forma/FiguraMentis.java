package fmt.cerulean.client.tex.forma;

import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.BitSet;

public class FiguraMentis extends NativeImageBackedTexture {
	public FiguraMentis(NativeImage image) {
		super(image);
	}

	public static FiguraMentis imitatur(BitSet tabula) {
		NativeImage corpus = new NativeImage(33, 33, false);
		for (int cordis = 0; cordis < 33; cordis++) {
			for (int mentis = 0; mentis < 33; mentis++) {
				boolean animus = tabula.get(cordis * 33 + mentis);
				corpus.setColor(cordis, mentis, animus ? 0xFFFFFFFF : 0xFF000000);
			}
		}

		return new FiguraMentis(corpus);
	}

	@Override
	public void save(Identifier id, Path path) {
		// Memorare non possum.
	}
}
