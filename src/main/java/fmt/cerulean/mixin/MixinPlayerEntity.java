package fmt.cerulean.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fmt.cerulean.block.base.Obedient;
import fmt.cerulean.item.StrictBrushItem;
import fmt.cerulean.registry.CeruleanItems;
import fmt.cerulean.util.Counterful;
import fmt.cerulean.world.CeruleanDimensions;
import fmt.cerulean.world.data.DimensionState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity implements Counterful {
	private final DimensionState cerulean$dimensionState = new DimensionState();

	protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(at = @At("HEAD"), method = "interact", cancellable = true)
	public void cerulean$interact(Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> info) {
		if (hand == Hand.MAIN_HAND && ((PlayerEntity) (Object) this).isHolding(CeruleanItems.BRUSH)) {
			if (Obedient.willCede(entity)) {
				StrictBrushItem.explain(entity.getWorld(), (Obedient) entity);
				info.setReturnValue(ActionResult.SUCCESS);
			}
		}
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
