package fmt.cerulean.mixin.client;

import fmt.cerulean.client.screen.ColorProgressScreen;
import fmt.cerulean.world.CeruleanDimensions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {
	@Unique
	private ClientWorld cerulean$changingWorld;

	@Inject(method = "joinWorld", at = @At("HEAD"))
	private void cerulean$captureJoinedWorld(ClientWorld world, CallbackInfo ci) {
		cerulean$changingWorld = world;
	}

	@ModifyVariable(method = "joinWorld", at = @At("STORE"), index = 2)
	private ProgressScreen cerulean$changeWorldJoined(ProgressScreen screen) {
		if (cerulean$changingWorld.getDimensionKey().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
			// TODO: 2 seconds of sleep fade-in

			return new ColorProgressScreen(true, 0xFF101020);
		}

		return screen;
	}
}
