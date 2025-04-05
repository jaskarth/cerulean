package fmt.cerulean.mixin.client;

import fmt.cerulean.block.entity.WellBlockEntity;
import fmt.cerulean.client.ClientState;
import fmt.cerulean.client.particle.StarParticleType;
import fmt.cerulean.client.screen.ColorProgressScreen;
import fmt.cerulean.flow.FlowState;
import fmt.cerulean.registry.CeruleanItems;
import fmt.cerulean.world.CeruleanDimensions;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {
	@Shadow @Nullable public ClientWorld world;
	@Shadow @Nullable public ClientPlayerInteractionManager interactionManager;

	@Shadow public abstract void setScreen(@Nullable Screen screen);

	@Shadow @Nullable public ClientPlayerEntity player;
	@Shadow @Final public GameOptions options;
	@Unique
	private ClientWorld cerulean$changingWorld;

	@Inject(method = "joinWorld", at = @At("HEAD"))
	private void cerulean$captureJoinedWorld(ClientWorld world, DownloadingTerrainScreen.WorldEntryReason worldEntryReason, CallbackInfo ci) {
		cerulean$changingWorld = world;
	}

	@Inject(method = "reset", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V", shift = At.Shift.AFTER))
	private void cerulean$changeWorldJoined(Screen resettingScreen, CallbackInfo ci) {
		if (!(resettingScreen instanceof DownloadingTerrainScreen)) {
			return;
		}

		if (cerulean$changingWorld.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
			ClientState.virtigo = 45;
			ClientState.virtigoColor = 0x101020;

			setScreen(new ColorProgressScreen(true, 0xFF101020));
			return;
		}

		if (cerulean$changingWorld.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.SKIES)) {
			if (this.world != null && this.world.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
				ClientState.virtigo = 45;
				ClientState.virtigoColor = 0xFFFFFF;

				setScreen(new ColorProgressScreen(true, 0xFFFFFFFF));

			}
		}

		if (this.world != null && this.world.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.SKIES)) {
			ClientState.virtigo = 45;
			ClientState.virtigoColor = 0x000000;

			setScreen(new ColorProgressScreen(true, 0xFF000000));
			return;
		}

		if (this.world != null && this.world.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
			ClientState.virtigo = 45;
			ClientState.virtigoColor = 0x000000;

			setScreen(new ColorProgressScreen(true, 0xFF000000));
		}
	}

	@Inject(method = "handleBlockBreaking", at = @At(value = "INVOKE", target =
		"Lnet/minecraft/client/particle/ParticleManager;addBlockBreakingParticles(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)V", shift = At.Shift.AFTER),
		locals = LocalCapture.CAPTURE_FAILHARD)
	private void cerulean$starwellBreaking(boolean breaking, CallbackInfo ci, BlockHitResult blockHitResult, BlockPos pos, Direction direction) {
		BlockEntity be = this.world.getBlockEntity(pos);
		if (be instanceof WellBlockEntity wbe) {
			Random random = this.world.random;
			double x = pos.getX() + 0.5;
			double y = pos.getY() + 0.5;
			double z = pos.getZ() + 0.5;
			FlowState state = wbe.getExportedState(Direction.UP);
			if (state == null) {
				return;
			}

			for (int i = 0; i < this.interactionManager.getBlockBreakingProgress(); i++) {
				StarParticleType star = WellBlockEntity.createParticle(state, true, random);
				double vx = random.nextGaussian() * 0.15;
				double vy = random.nextGaussian() * 0.15;
				double vz = random.nextGaussian() * 0.15;

				world.addParticle(star, true, x, y, z, vx, vy, vz);
			}
		}
	}

	@Inject(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;wasPressed()Z", ordinal = 10,
			shift = At.Shift.BEFORE))
	public void cerulean$leftClickActionWhileHoldingDown(CallbackInfo ci) {
		if (this.player.isUsingItem() && this.player.getActiveItem().isOf(CeruleanItems.CAMERA) && !this.player.getItemCooldownManager().isCoolingDown(CeruleanItems.CAMERA)) {
			while (this.options.attackKey.wasPressed()) {
				int idx = this.player.getInventory().indexOf(new ItemStack(CeruleanItems.FILM));
				if (idx != -1) {
					this.player.getInventory().getStack(idx).decrement(1);
					this.player.getItemCooldownManager().set(CeruleanItems.CAMERA, 9999); // Server knows best

					ClientState.remember = true;
					ClientState.forget = true;
					if (!this.options.getHideLightningFlashes().getValue()) {
						ClientState.virtigo = 10;
						ClientState.virtigoColor = 0xFFFFFF;
					}
				}
			}
		}
	}

	@Inject(method = "doItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;resetEquipProgress(Lnet/minecraft/util/Hand;)V", shift = At.Shift.BEFORE),
			locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	private void cerulean$holdDownPhoto(CallbackInfo ci, Hand[] var1, int var2, int var3, Hand hand) {
		ItemStack itemStack = this.player.getStackInHand(hand);
		if (itemStack.isOf(CeruleanItems.PHOTOGRAPH)) {
			ci.cancel();
		}
	}
}
