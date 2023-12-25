package fmt.cerulean.mixin.client;

import fmt.cerulean.client.ClientState;
import fmt.cerulean.client.screen.ColorProgressScreen;
import fmt.cerulean.util.Counterful;
import fmt.cerulean.world.CeruleanDimensions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {
	@Shadow @Nullable public ClientWorld world;
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
}
