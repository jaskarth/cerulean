package fmt.cerulean.mixin;

import java.util.Map;
import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fmt.cerulean.Cerulean;
import fmt.cerulean.block.base.Obedient;
import fmt.cerulean.util.PaintingDuck;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.nbt.NbtCompound;

@Mixin(PaintingEntity.class)
public class MixinPaintingEntity implements Obedient, PaintingDuck {
	@Unique
	private static final String LETHARGIC_KEY = "cerulean:lethargic";
	@Unique
	private boolean lethargic = false;
	@Unique
	private int captureNegX, capturePosX, captureNegY, capturePosY, captureNegZ, capturePosZ;

	@Inject(at = @At("HEAD"), method = "writeCustomDataToNbt")
	public void cerulean$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
		if (manifestsInDreams() && lethargic) {
			nbt.putBoolean(LETHARGIC_KEY, lethargic);
		}
	}

	@Inject(at = @At("HEAD"), method = "readCustomDataFromNbt")
	public void cerulean$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
		lethargic = nbt.getBoolean(LETHARGIC_KEY);
	}

	@Override
	public boolean distrustDestiny() {
		return !manifestsInDreams();
	}

	@Override
	public Map<String, Consumer<String>> cede() {
		return Map.of(
			"captureNegX", intuition -> captureNegX = Obedient.count(intuition),
			"capturePosX", intuition -> capturePosX = Obedient.count(intuition),
			"captureNegY", intuition -> captureNegY = Obedient.count(intuition),
			"capturePosY", intuition -> capturePosY = Obedient.count(intuition),
			"captureNegZ", intuition -> captureNegZ = Obedient.count(intuition),
			"capturePosZ", intuition -> capturePosZ = Obedient.count(intuition),
			"lethargic", intuition -> lethargic = Obedient.blackOrWhite(intuition)
		);
	}

	@Override
	public boolean lethargic() {
		return lethargic;
	}

	public boolean manifestsInDreams() {
		return ((PaintingEntity) (Object) this).getVariant().matchesId(Cerulean.id("dreams"));
	}
}
