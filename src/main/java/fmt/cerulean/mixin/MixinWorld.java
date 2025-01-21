package fmt.cerulean.mixin;

import fmt.cerulean.block.entity.base.DreamscapeTickable;
import fmt.cerulean.world.CeruleanDimensions;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.chunk.BlockEntityTickInvoker;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.tick.TickManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.List;

@Mixin(World.class)
public abstract class MixinWorld {

	@Shadow public abstract RegistryEntry<DimensionType> getDimensionEntry();

	@Shadow public abstract Profiler getProfiler();

	@Shadow private boolean iteratingTickingBlockEntities;

	@Shadow @Final private List<BlockEntityTickInvoker> pendingBlockEntityTickers;

	@Shadow @Final protected List<BlockEntityTickInvoker> blockEntityTickers;

	@Shadow public abstract TickManager getTickManager();

	@Shadow public abstract boolean shouldTickBlockPos(BlockPos pos);

	@Shadow public abstract @Nullable BlockEntity getBlockEntity(BlockPos pos);

	@Inject(method = "shouldTickBlockPos", at = @At("HEAD"), cancellable = true)
	private void cerulean$dreamscapeTicking(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if (this.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
			if (getBlockEntity(pos) instanceof DreamscapeTickable) {
				return;
			}

			cir.setReturnValue(false);
		}
	}
}
