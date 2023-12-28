package fmt.cerulean.mixin.client;

import com.mojang.authlib.GameProfile;
import fmt.cerulean.block.entity.MimicBlockEntity;
import fmt.cerulean.client.ClientState;
import fmt.cerulean.net.CeruleanNetworking;
import fmt.cerulean.world.CeruleanDimensions;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity {
	private int cerulean$replaceTimer = 0;

	public MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V", shift = At.Shift.AFTER))
	private void cerulean$clientPlayerTick(CallbackInfo ci) {
		if (ClientState.virtigo > 0) {
			ClientState.virtigo--;
		}

		if (this.getWorld().getDimensionKey().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
			if (cerulean$replaceTimer == 0) {
				BlockPos pos = this.getLandingPos();

				BlockEntity be = this.getWorld().getBlockEntity(pos);
				if (be instanceof MimicBlockEntity mbe) {
					if (mbe.alone && mbe.dist > 2) {
						Direction dir = mbe.facing;

						if (dir == this.getHorizontalFacing()) {
							cerulean$replaceTimer = 10;
							ClientPlayNetworking.send(CeruleanNetworking.CLOSE_BEHIND, PacketByteBufs.empty());
						}
					}
				}
			} else {
				cerulean$replaceTimer--;
			}
		} else {
			cerulean$replaceTimer = 0;
		}
	}
}
