package fmt.cerulean.client.tex;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fmt.cerulean.client.tex.gen.DynamicEyeTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.client.texture.SpriteDimensions;
import net.minecraft.client.texture.SpriteOpener;
import net.minecraft.client.texture.atlas.AtlasSource;
import net.minecraft.client.texture.atlas.AtlasSourceType;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.metadata.ResourceMetadata;
import net.minecraft.util.Identifier;

public class CeruleanAtlasSource implements AtlasSource {
	public static final MapCodec<CeruleanAtlasSource> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
							Identifier.CODEC.fieldOf("resource").forGetter(s -> s.resource)
					)
					.apply(instance, CeruleanAtlasSource::new)
	);
	public static final AtlasSourceType TYPE = new AtlasSourceType(CODEC);
	private final Identifier resource;

	public CeruleanAtlasSource(Identifier resource) {
		this.resource = resource;
	}

	@Override
	public void load(ResourceManager resourceManager, SpriteRegions regions) {
		if (resource.equals(DynamicEyeTexture.ID)) {
			regions.add(DynamicEyeTexture.ID, spriteOpener -> {
				DynamicEyeTexture.IMAGE = new NativeImage(16, 16, false);
				return new SpriteContents(DynamicEyeTexture.ID,
						new SpriteDimensions(16, 16), DynamicEyeTexture.IMAGE, ResourceMetadata.NONE);
			});
		} else {
			throw new IllegalStateException("Unknown custom atlas resource!");
		}
	}

	@Override
	public AtlasSourceType getType() {
		return TYPE;
	}
}
