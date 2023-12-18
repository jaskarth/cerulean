package fmt.cerulean.world.gen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;

import java.util.stream.Stream;

public class DreamscapeBiomeSource extends BiomeSource {
    public static final Codec<DreamscapeBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryCodecs.entryList(RegistryKeys.BIOME, Biome.CODEC).fieldOf("biomes").forGetter((source) -> source.entries)
    ).apply(instance, DreamscapeBiomeSource::new));

    public final RegistryEntryList<Biome> entries;

    public DreamscapeBiomeSource(RegistryEntryList<Biome> entries) {
        this.entries = entries;
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return CODEC;
    }

    @Override
    protected Stream<RegistryEntry<Biome>> biomeStream() {
        return entries.stream();
    }

    @Override
    public RegistryEntry<Biome> getBiome(int x, int y, int z, MultiNoiseUtil.MultiNoiseSampler noise) {
        return entries.get(0);
    }
}