package fmt.cerulean.mixin;

import fmt.cerulean.world.CeruleanDimensions;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public abstract class MixinWorld {

	@Shadow public abstract RegistryEntry<DimensionType> getDimensionEntry();

	@Inject(method = "tickBlockEntities", at = @At("HEAD"), cancellable = true)
	private void cerulean$dreamscapeTicking(CallbackInfo ci) {
		if (this.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
			ci.cancel();
		}
	}
}
