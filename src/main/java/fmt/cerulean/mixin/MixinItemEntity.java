package fmt.cerulean.mixin;

import fmt.cerulean.entity.PlasticSwimming;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class MixinItemEntity extends Entity {
	public MixinItemEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "applyWaterBuoyancy", at = @At("HEAD"), cancellable = true)
	public void cerulean$plasticBuoyancy(CallbackInfo ci) {
		if (((PlasticSwimming)this).cerulean$isInPlastic()) {
			Vec3d vec3d = this.getVelocity();
			this.setVelocity(vec3d.x * 0.99F, vec3d.y * 0.99F, vec3d.z * 0.99F);
			ci.cancel();
		}
	}
}
