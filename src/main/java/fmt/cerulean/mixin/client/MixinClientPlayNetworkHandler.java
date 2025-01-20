package fmt.cerulean.mixin.client;

import fmt.cerulean.client.ClientState;
import fmt.cerulean.client.screen.ColorDownloadScreen;
import fmt.cerulean.world.CeruleanDimensions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.network.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinClientPlayNetworkHandler extends ClientCommonNetworkHandler {
	@Shadow private @Nullable WorldLoadingState worldLoadingState;

	protected MixinClientPlayNetworkHandler(MinecraftClient client, ClientConnection connection, ClientConnectionState connectionState) {
		super(client, connection, connectionState);
	}

	@Inject(method = "onPlayerRespawn", at = @At("HEAD"))
	private void cerulean$captureWorld(PlayerRespawnS2CPacket packet, CallbackInfo ci) {
		ClientState.lastWorld = MinecraftClient.getInstance().world;
	}

	@Inject(method = "startWorldLoading", at = @At("HEAD"), cancellable = true)
	private void cerulean$handleWorldLoad(ClientPlayerEntity player, ClientWorld world, DownloadingTerrainScreen.WorldEntryReason worldEntryReason, CallbackInfo ci) {
		ClientWorld lastWorld = ClientState.lastWorld;
		ClientState.lastWorld = null;

		if (lastWorld != null) {
			if (world.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
				this.worldLoadingState = new WorldLoadingState(player, world, this.client.worldRenderer);
				this.client.setScreen(new ColorDownloadScreen(this.worldLoadingState::isReady, 0xFF101020));
				ci.cancel();
			}

			if (lastWorld.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.SKIES)) {
				this.worldLoadingState = new WorldLoadingState(player, world, this.client.worldRenderer);
				this.client.setScreen(new ColorDownloadScreen(this.worldLoadingState::isReady, 0xFF000000));
				ci.cancel();
			}

			if (lastWorld.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
				if (world.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.SKIES)) {
					this.worldLoadingState = new WorldLoadingState(player, world, this.client.worldRenderer);
					this.client.setScreen(new ColorDownloadScreen(this.worldLoadingState::isReady, 0xFFFFFFFF));
					ci.cancel();
				} else {
					this.worldLoadingState = new WorldLoadingState(player, world, this.client.worldRenderer);
					this.client.setScreen(new ColorDownloadScreen(this.worldLoadingState::isReady, 0xFF000000));
					ci.cancel();
				}
			}
		}
	}
}
