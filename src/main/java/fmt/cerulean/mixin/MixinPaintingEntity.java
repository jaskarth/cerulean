package fmt.cerulean.mixin;

import java.util.Map;
import java.util.function.Consumer;

import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
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
	private int captureNegX = -15, capturePosX = 15, captureNegY = -8, capturePosY = 8, captureNegZ = -15, capturePosZ = 15;

	private int teleX = 0, teleY = 0, teleZ = 0;

	@Inject(at = @At("TAIL"), method = "writeCustomDataToNbt")
	public void cerulean$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
		if (manifestsInDreams() && lethargic) {
			nbt.putBoolean(LETHARGIC_KEY, lethargic);
		}
		if (manifestsInDreams()) {
			nbt.putInt("cerulean:nx", captureNegX);
			nbt.putInt("cerulean:ny", captureNegY);
			nbt.putInt("cerulean:nz", captureNegZ);
			nbt.putInt("cerulean:px", capturePosX);
			nbt.putInt("cerulean:py", capturePosY);
			nbt.putInt("cerulean:pz", capturePosZ);

			nbt.putInt("cerulean:tx", teleX);
			nbt.putInt("cerulean:ty", teleY);
			nbt.putInt("cerulean:tz", teleZ);
		}
	}

	@Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
	public void cerulean$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
		lethargic = nbt.getBoolean(LETHARGIC_KEY);
		if (manifestsInDreams()) {
			captureNegX = nbt.getInt("cerulean:nx");
			captureNegY = nbt.getInt("cerulean:ny");
			captureNegZ = nbt.getInt("cerulean:nz");
			capturePosX = nbt.getInt("cerulean:px");
			capturePosY = nbt.getInt("cerulean:py");
			capturePosZ = nbt.getInt("cerulean:pz");

			teleX = nbt.getInt("cerulean:tx");
			teleY = nbt.getInt("cerulean:ty");
			teleZ = nbt.getInt("cerulean:tz");
		}
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
			"teleX", intuition -> teleX = Obedient.count(intuition),
			"teleY", intuition -> teleY = Obedient.count(intuition),
			"teleZ", intuition -> teleZ = Obedient.count(intuition),
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

	@Override
	public BlockBox getManifestationShape() {
		return new BlockBox(captureNegX, captureNegY, captureNegZ, capturePosX, capturePosY, capturePosZ);
	}

	@Override
	public BlockPos getTeleportTarget() {
		return new BlockPos(teleX, teleY, teleZ);
	}
}
