package fmt.cerulean.mixin;

import com.mojang.authlib.GameProfile;
import fmt.cerulean.block.entity.MimicBlockEntity;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.util.Counterful;
import fmt.cerulean.util.Util;
import fmt.cerulean.world.CeruleanDimensions;
import fmt.cerulean.world.DimensionState;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends PlayerEntity {
	public MixinServerPlayerEntity(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
		super(world, pos, yaw, gameProfile);
	}

	@Shadow public abstract ServerWorld getServerWorld();

	@Shadow public ServerPlayNetworkHandler networkHandler;

	@Shadow public boolean notInAnyWorld;

	@Shadow @Final public MinecraftServer server;

	@Shadow public abstract @Nullable Entity teleportTo(TeleportTarget teleportTarget);

	@Inject(method = "teleportTo", at = @At("RETURN"))
	private void cerulean$resetState(TeleportTarget teleportTarget, CallbackInfoReturnable<Entity> cir) {
		DimensionState st = Counterful.get((PlayerEntity) (Object) this);
		st.reset();
		st.sync((ServerPlayerEntity) (Object)this);
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void cerulean$writeState(NbtCompound nbt, CallbackInfo ci) {
		nbt.put("CeruleanDS", Counterful.get((PlayerEntity) (Object)this).nbt());
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	private void cerulean$readState(NbtCompound nbt, CallbackInfo ci) {
		Counterful.get((PlayerEntity) (Object)this).read(nbt.getCompound("CeruleanDS"));
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void cerulean$playerTick(CallbackInfo ci) {
		DimensionState st = Counterful.get((PlayerEntity) (Object) this);
		World world = getWorld();
		if (st.melancholy > 230) {
			this.detach();
			this.notInAnyWorld = true;
			this.getServerWorld().removePlayer((ServerPlayerEntity) (Object) this, Entity.RemovalReason.CHANGED_DIMENSION);

			Criteria.CHANGED_DIMENSION.trigger((ServerPlayerEntity) (Object) this, world.getRegistryKey(), World.OVERWORLD);
			this.networkHandler.onClientStatus(new ClientStatusC2SPacket(ClientStatusC2SPacket.Mode.PERFORM_RESPAWN));

			st.reset();
			st.sync((ServerPlayerEntity) (Object)this);
			return;
		}

		if (this.getWorld().getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
			st.dissonance++;

			if (st.ennui > 0) {
				st.ennui++;
			} else {
				BlockPos pos = this.getLandingPos();

				BlockEntity be = this.getWorld().getBlockEntity(pos);
				if (be instanceof MimicBlockEntity mbe) {
					if (mbe.dist >= 15) {
						st.ennui = 1;
					}
				}
			}

			if (st.dissonance >= 1800) {
				if (st.indifference == 0) {
					st.indifference = 1;
				}
			}

			if (st.indifference > 0) {
				st.indifference++;
			} else if ((this.getWorld().getTime() & 7) == 0) {
				BlockPos bp = this.getBlockPos();
				for (Direction dir : Util.DIRECTIONS) {
					if (this.getWorld().getBlockState(bp.offset(dir)).isOf(CeruleanBlocks.INKY_VOID)) {
						st.indifference = 1;
					}
				}
			}

			if (st.indifference > 60) {
				this.detach();
				this.notInAnyWorld = true;
				this.getServerWorld().removePlayer((ServerPlayerEntity) (Object) this, Entity.RemovalReason.CHANGED_DIMENSION);

				this.networkHandler.onClientStatus(new ClientStatusC2SPacket(ClientStatusC2SPacket.Mode.PERFORM_RESPAWN));
				st.reset();
				st.sync((ServerPlayerEntity) (Object)this);
				return;
			}

			st.sync((ServerPlayerEntity) (Object)this);

			if (st.ennui > 50) {
				ServerWorld skies = this.server.getWorld(RegistryKey.of(RegistryKeys.WORLD, CeruleanDimensions.SKIES));

				if (skies == null) {
					throw new RuntimeException("I stepped into the light, but only darkness engulfed me...");
				}

				BlockPos tp = CeruleanDimensions.findSkiesSpawn(skies, new BlockPos(0, 0, 0));

				if (tp != null) {
					teleportTo(new TeleportTarget(skies, tp.up(2).toCenterPos(), Vec3d.ZERO, this.getYaw(), this.getPitch(), e -> {}));

					st.reset();
					st.sync((ServerPlayerEntity) (Object)this);
				} else {
					this.detach();
					this.notInAnyWorld = true;
					this.getServerWorld().removePlayer((ServerPlayerEntity) (Object) this, Entity.RemovalReason.CHANGED_DIMENSION);

					this.networkHandler.onClientStatus(new ClientStatusC2SPacket(ClientStatusC2SPacket.Mode.PERFORM_RESPAWN));

					st.reset();
					st.sync((ServerPlayerEntity) (Object)this);
				}
			}
		}
	}

	@Inject(method = "dropSelectedItem", at = @At("HEAD"), cancellable = true)
	private void cerulean$noDrops(boolean entireStack, CallbackInfoReturnable<Boolean> cir) {
		if (this.getWorld().getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "copyFrom", at = @At("TAIL"))
	private void cerulean$copy(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
//		Counterful.get((PlayerEntity) (Object)this).read(Counterful.get(oldPlayer).nbt());
	}
}
