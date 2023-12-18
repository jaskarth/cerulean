package fmt.cerulean.mixin;

import fmt.cerulean.util.Seedy;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NoiseConfig.class)
public class MixinNoiseConfig implements Seedy {
	private long cerulean$seed;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void cerulean$capSeed(ChunkGeneratorSettings chunkGeneratorSettings, RegistryEntryLookup noiseParametersLookup, long seed, CallbackInfo ci) {
		this.cerulean$seed = seed;
	}

	@Override
	public long getSeed() {
		return this.cerulean$seed;
	}
}
