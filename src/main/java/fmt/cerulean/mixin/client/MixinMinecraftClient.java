package fmt.cerulean.mixin.client;

import fmt.cerulean.block.entity.WellBlockEntity;
import fmt.cerulean.client.ClientState;
import fmt.cerulean.client.particle.StarParticleType;
import fmt.cerulean.client.screen.ColorProgressScreen;
import fmt.cerulean.flow.FlowState;
import fmt.cerulean.world.CeruleanDimensions;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {
	@Shadow @Nullable public ClientWorld world;
	@Shadow @Nullable public ClientPlayerInteractionManager interactionManager;
	@Unique
	private ClientWorld cerulean$changingWorld;

	@Inject(method = "joinWorld", at = @At("HEAD"))
	private void cerulean$captureJoinedWorld(ClientWorld world, CallbackInfo ci) {
		cerulean$changingWorld = world;
	}

	@ModifyVariable(method = "joinWorld", at = @At("STORE"), index = 2)
	private ProgressScreen cerulean$changeWorldJoined(ProgressScreen screen) {
		if (cerulean$changingWorld.getDimensionKey().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
			ClientState.virtigo = 45;
			ClientState.virtigoColor = 0x101020;

			return new ColorProgressScreen(true, 0xFF101020);
		}

		if (cerulean$changingWorld.getDimensionKey().getValue().equals(CeruleanDimensions.SKIES)) {
			if (this.world != null && this.world.getDimensionKey().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
				ClientState.virtigo = 45;
				ClientState.virtigoColor = 0xFFFFFF;

				return new ColorProgressScreen(true, 0xFFFFFFFF);
			}
		}

		if (this.world != null && this.world.getDimensionKey().getValue().equals(CeruleanDimensions.SKIES)) {
			ClientState.virtigo = 45;
			ClientState.virtigoColor = 0x000000;

			return new ColorProgressScreen(true, 0xFF000000);
		}

		if (this.world != null && this.world.getDimensionKey().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
			ClientState.virtigo = 45;
			ClientState.virtigoColor = 0x000000;

			return new ColorProgressScreen(true, 0xFF000000);
		}

		return screen;
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
}
