package fmt.cerulean.mixin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import fmt.cerulean.entity.DreamscapeEntity;
import fmt.cerulean.entity.MemoryFrameEntity;
import fmt.cerulean.world.CeruleanDimensions;
import fmt.cerulean.world.DreamscapeGamerules;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import fmt.cerulean.world.DreamscapeTeleporter;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public abstract class MixinServerWorld extends World implements StructureWorldAccess {

	@Shadow public abstract ServerWorld toServerWorld();

	@Shadow @Final private List<ServerPlayerEntity> players;

	protected MixinServerWorld(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
		super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void cerulean$handleSleep(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {

		for (ServerPlayerEntity player : new ArrayList<>(this.players)) {
			if (player.canResetTimeBySleeping() && DreamscapeTeleporter.isSpecialSleep(player)) {
				DreamscapeTeleporter.handleTeleport(toServerWorld());
				player.wakeUp(false, false);
			}
		}
	}

//	@Inject(method = "wakeSleepingPlayers", at = @At("HEAD"))
//	public void handleSleep(CallbackInfo ci) {
//		DreamscapeTeleporter.handleTeleport(toServerWorld());
//	}

	@Override
	public GameRules getGameRules() {
		GameRules rules = super.getGameRules();
		if (this.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
			return new DreamscapeGamerules(rules);
		}
		return rules;
	}

	@Inject(method = "spawnEntity", at = @At("HEAD"), cancellable = true)
	private void cerulean$noEntitySpawn(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		if (this.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
			if (!(entity instanceof PlayerEntity || entity instanceof DreamscapeEntity)) {
				cir.setReturnValue(false);
			}
		}
	}
}
