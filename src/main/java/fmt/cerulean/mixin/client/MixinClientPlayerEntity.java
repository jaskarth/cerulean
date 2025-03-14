package fmt.cerulean.mixin.client;

import com.mojang.authlib.GameProfile;
import fmt.cerulean.block.entity.MimicBlockEntity;
import fmt.cerulean.client.ClientState;
import fmt.cerulean.client.render.item.Pressable;
import fmt.cerulean.item.component.PhotoComponent;
import fmt.cerulean.net.CeruleanNetworking;
import fmt.cerulean.net.packet.CloseBehindPacket;
import fmt.cerulean.net.packet.StaringPacket;
import fmt.cerulean.registry.CeruleanItemComponents;
import fmt.cerulean.registry.CeruleanItems;
import fmt.cerulean.world.CeruleanDimensions;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity implements Pressable {
	@Shadow public abstract float getYaw(float tickDelta);

	@Unique
	private int cerulean$replaceTimer = 0;

	@Unique
	private int cerulean$photoUsingTicks = 0;

	@Unique
	private boolean cerulean$photoLetGo = false;

	public MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V", shift = At.Shift.AFTER))
	private void cerulean$clientPlayerTick(CallbackInfo ci) {
		if (ClientState.virtigo > 0) {
			ClientState.virtigo--;
		}

		if (this.getWorld().getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
			if (cerulean$replaceTimer == 0) {
				BlockPos pos = this.getLandingPos();

				BlockEntity be = this.getWorld().getBlockEntity(pos);
				if (be instanceof MimicBlockEntity mbe) {
					if (mbe.alone && mbe.dist > 2) {
						Direction dir = mbe.facing;

						if (dir == this.getHorizontalFacing()) {
							cerulean$replaceTimer = 10;
							ClientPlayNetworking.send(CloseBehindPacket.INST);
						}
					}
				}
			} else {
				cerulean$replaceTimer--;
			}
		} else {
			cerulean$replaceTimer = 0;
		}

		if (this.getOffHandStack().isEmpty() && getActiveItem().isOf(CeruleanItems.PHOTOGRAPH)) {
			if (isUsingItem()) {
				cerulean$photoUsingTicks++;
				cerulean$photoLetGo = false;
			} else {
				cerulean$photoUsingTicks--;
				cerulean$photoLetGo = true;
			}
		} else {
			cerulean$photoUsingTicks--;
			cerulean$photoLetGo = true;
		}

		cerulean$photoUsingTicks = Math.clamp(cerulean$photoUsingTicks, -1, 4);

		if (cerulean$photoUsingTicks == 4 && (this.getWorld().getTime() & 1) == 0) {
			ItemStack stack = getActiveItem();
			PhotoComponent photo = stack.get(CeruleanItemComponents.PHOTO);

			if (photo != null) {
				ClientPlayNetworking.send(new StaringPacket(photo.id(), getYaw(), getPitch()));
			}
		}

		if (ClientState.emergencyRender > -2) {
			ClientState.emergencyRender++;

			if (ClientState.emergencyRender > 40) {
				ClientState.emergencyRender = -2;
			}
		}
	}

	@Inject(method = "dropSelectedItem", at = @At("HEAD"), cancellable = true)
	private void cerulean$noDrops(boolean entireStack, CallbackInfoReturnable<Boolean> cir) {
		if (this.getWorld().getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
			cir.setReturnValue(false);
		}
	}

	@Override
	public int cerulean$photoHeldDownTicks() {
		return cerulean$photoUsingTicks;
	}

	@Override
	public boolean cerulean$lettingGo() {
		return cerulean$photoLetGo;
	}
}
