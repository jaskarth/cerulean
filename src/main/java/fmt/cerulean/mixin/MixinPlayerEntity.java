package fmt.cerulean.mixin;

import fmt.cerulean.util.Counterful;
import fmt.cerulean.world.DimensionState;
import fmt.cerulean.world.CeruleanDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity implements Counterful {
	private final DimensionState cerulean$dimensionState = new DimensionState();

	protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "isBlockBreakingRestricted", at = @At("HEAD"), cancellable = true)
	private void cerulean$dreamscapeInteraction(World world, BlockPos pos, GameMode gameMode, CallbackInfoReturnable<Boolean> cir) {
		if (world.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
			cir.setReturnValue(true);
		}
	}

	@Override
	protected void tickInVoid() {
		World world = this.getWorld();
		if (!world.isClient() && world.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.SKIES)) {
			cerulean$dimensionState.melancholy++;
			cerulean$dimensionState.sync((ServerPlayerEntity) (Object)this);
		} else {
			super.tickInVoid();
		}
	}

	@Override
	public DimensionState getState() {
		return this.cerulean$dimensionState;
	}
}
