package fmt.cerulean.mixin.client;

import fmt.cerulean.Cerulean;
import fmt.cerulean.client.screen.ColorDownloadScreen;
import fmt.cerulean.world.CeruleanDimensions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.ClientConnection;
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

	@Inject(method = "startWorldLoading", at = @At("HEAD"), cancellable = true)
	private void cerulean$handleWorldLoad(ClientPlayerEntity player, ClientWorld world, CallbackInfo ci) {
		if (world.getDimensionKey().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
			this.worldLoadingState = new WorldLoadingState(player, world, this.client.worldRenderer);
			this.client.setScreen(new ColorDownloadScreen(this.worldLoadingState::isReady, 0xFF101020));
			ci.cancel();
		}
	}
}
