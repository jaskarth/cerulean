package fmt.cerulean.mixin.client;

import fmt.cerulean.client.ClientState;
import fmt.cerulean.util.Counterful;
import fmt.cerulean.world.DimensionState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud {

	@Shadow @Final private MinecraftClient client;

	@ModifyConstant(method = "renderSleepOverlay", constant = @Constant(floatValue = 220.f))
	private float cerulean$fullFadeout(float constant) {
		return 0;
	}

	@Inject(method = "renderSleepOverlay", at = @At("HEAD"))
	private void cerulean$renderOverlays(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		if (this.client.player.getSleepTimer() > 0) {
			this.client.getProfiler().push("sleep");
			float h = (float)this.client.player.getSleepTimer();
			float j = h / 100.0F;
			if (j > 1.0F) {
				j = 1.0F - (h - 100.0F) / 10.0F;
			}

			int k = (int)(255.0F * j) << 24 | 1052704;
			context.fill(RenderLayer.getGuiOverlay(), 0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), k);
			this.client.getProfiler().pop();
		}

		DimensionState state = Counterful.get(MinecraftClient.getInstance().player);
		if (state.melancholy > 140) {
			float amt = MathHelper.clamp((state.melancholy - 140) / 80.f, 0, 1);
			int color = (int)(255.0F * amt) << 24 | 0x000000;
			context.fill(RenderLayer.getGuiOverlay(), 0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), color);
		}

		if (state.indifference > 0) {
			float amt = MathHelper.clamp(state.indifference / 55.f, 0, 1);
			int color = (int)(255.0F * amt) << 24 | 0x000000;
			context.fill(RenderLayer.getGuiOverlay(), 0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), color);
		}

		if (ClientState.virtigo > 0) {
			float amt = MathHelper.clamp(ClientState.virtigo / 40.f, 0, 1);
			int color = (int)(255.0F * amt) << 24 | ClientState.virtigoColor;
			context.fill(RenderLayer.getGuiOverlay(), 0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), color);

			return;
		}

		if (state.ennui > 0) {
			float amt = MathHelper.clamp(state.ennui / 45.f, 0, 1);
			int color = (int)(255.0F * amt) << 24 | 0xFFFFFF;
			context.fill(RenderLayer.getGuiOverlay(), 0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), color);
		}
	}
}
