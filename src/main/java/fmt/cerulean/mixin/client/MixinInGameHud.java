package fmt.cerulean.mixin.client;

import java.util.List;
import java.util.Random;

import com.mojang.blaze3d.systems.RenderSystem;
import fmt.cerulean.Cerulean;
import fmt.cerulean.client.render.EmergencyOverlay;
import fmt.cerulean.client.render.EmergencyStack;
import fmt.cerulean.client.tex.gen.StaticTexture;
import fmt.cerulean.registry.CeruleanItems;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Lists;

import fmt.cerulean.block.base.Instructor;
import fmt.cerulean.client.ClientState;
import fmt.cerulean.util.Counterful;
import fmt.cerulean.world.CeruleanDimensions;
import fmt.cerulean.world.data.DimensionState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Mixin(InGameHud.class)
public abstract class MixinInGameHud implements Instructor {
	@Unique
	private final List<Fragment> fragments = Lists.newArrayList();

	@Shadow @Final private MinecraftClient client;

	@Shadow public abstract TextRenderer getTextRenderer();

	@Shadow private @Nullable Text overlayMessage;

	@Shadow private int overlayRemaining;

	@Shadow private boolean overlayTinted;

	@Unique
	private float cerulean$viewfinderScale;

	@ModifyConstant(method = "renderSleepOverlay", constant = @Constant(floatValue = 220.f))
	private float cerulean$fullFadeout(float constant) {
		return 0;
	}

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	public void cerulean$cameraOverlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		EmergencyOverlay.render(context);
		if (ClientState.forget) {
			int color = 0xFF000000;
			context.fill(RenderLayer.getGuiOverlay(), 0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), color);

			this.cerulean$viewfinderScale = 0.5f;
			ClientState.forget = false;
			ci.cancel();
		}
	}

	@Inject(method = "renderSleepOverlay", at = @At("HEAD"))
	private void cerulean$renderOverlays(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {

		if (MinecraftClient.getInstance().world != null && MinecraftClient.getInstance().world.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
			TextRenderer textRenderer = this.getTextRenderer();
			if (this.overlayMessage != null && this.overlayRemaining > 0) {
				this.client.getProfiler().push("overlayMessage");
				float f = (float)this.overlayRemaining - tickCounter.getTickDelta(false);
				int i = (int)(f * 255.0F / 20.0F);
				if (i > 255) {
					i = 255;
				}

				if (i > 8) {
					context.getMatrices().push();
					context.getMatrices().translate((float)(context.getScaledWindowWidth() / 2), (float)(context.getScaledWindowHeight() - 68), 0.0F);
					int j;
					if (this.overlayTinted) {
						j = MathHelper.hsvToArgb(f / 50.0F, 0.7F, 0.6F, i);
					} else {
						j = ColorHelper.Argb.withAlpha(i, -1);
					}

					int k = textRenderer.getWidth(this.overlayMessage);
					context.drawTextWithBackground(textRenderer, this.overlayMessage, -k / 2, -4, k, j);
					context.getMatrices().pop();
				}

				this.client.getProfiler().pop();
			}
		}

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
			int addend;
			if (ClientState.truthful) {
				addend = 0x000000;
			} else {
				addend = 0xFFFFFF;
			}
			int color = (int)(255.0F * amt) << 24 | addend;
			context.fill(RenderLayer.getGuiOverlay(), 0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), color);
		}
	}

	@Inject(method = "renderOverlayMessage", at = @At("HEAD"), cancellable = true)
	public void cerulean$renderOverlayColor(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		if (MinecraftClient.getInstance().world != null && MinecraftClient.getInstance().world.getDimensionEntry().getKey().get().getValue().equals(CeruleanDimensions.DREAMSCAPE)) {
			ci.cancel();
		}
	}
	
	@Inject(at = @At("RETURN"), method = "render")
	public void render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo info) {
		int e = ClientState.emergencyRender;
//		e = 20;
		if (e >= 0) {
			// TODO: needs shader that doesn't discard alpha
//			((DrawContextAccessor)context)
//					.callDrawTexturedQuad(StaticTexture.ID, 0, context.getScaledWindowWidth(), 0, context.getScaledWindowHeight(),
//							0, 0, 1, 0, 1, 1, 1, 1, 20.0f / 255.0f);

			TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
			int color = 0xFFFF0000;
			context.drawText(textRenderer, EmergencyStack.T_1, 10, 10, color, false);

			if (e >= 2) {
				context.drawText(textRenderer, EmergencyStack.T_2, 10, 19, color, false);
			}
			if (e >= 4) {
				context.drawText(textRenderer, EmergencyStack.T_3, 10, 28, color, false);
			}
			if (e >= 6) {
				context.drawText(textRenderer, EmergencyStack.T_4, 10, 37, color, false);
			}
			if (e >= 8) {
				context.drawText(textRenderer, EmergencyStack.T_5, 10, 46, color, false);
			}
			if (e >= 10) {
				context.drawText(textRenderer, EmergencyStack.T_6, 10, 55, color, false);
			}
			if (e >= 12) {
				context.drawText(textRenderer, EmergencyStack.T_7, 10, 64, color, false);
			}
			if (e >= 14) {
				context.drawText(textRenderer, EmergencyStack.T_8, 10, 73, color, false);
			}
			if (e >= 16) {
				context.drawText(textRenderer, EmergencyStack.T_9, 10, 82, color, false);
			}
			if (e >= 18) {
				context.drawText(textRenderer, EmergencyStack.T_10, 10, 91, color, false);
			}
			if (e >= 20) {
				context.drawText(textRenderer, EmergencyStack.T_11, 10, 100, color, false);
			}
			if (e >= 22) {
				context.drawText(textRenderer, EmergencyStack.T_E, 10, 109, color, false);
			}
		}

		long time = System.currentTimeMillis();
		for (int i = 0; i < fragments.size(); i++) {
			Fragment fragment = fragments.get(i);
			float progress = (time - fragment.time()) / 2000f;
			if (progress >= 1f) {
				fragments.remove(i--);
			} else {
				int alpha = 255;
				if (progress < 0.2f) {
					alpha = (int) (Math.clamp((progress / 0.2f), 0f, 1f) * 255);
				} else if (progress > 0.9f) {
					alpha = (int) ((Math.clamp((1 - progress), 0f, 1f) / 0.1f) * 255);
				}
				alpha = Math.min(255, Math.max(0, alpha));
				if (alpha < 4) {
					continue;
				}
				int cx = context.getScaledWindowWidth() / 2;
				int cy = context.getScaledWindowHeight() / 2;
				Random r = new Random(fragment.hashCode());
				int ex = r.nextInt(200) - 100;
				int ey = r.nextInt(200) - 100;
				int er = r.nextInt(100) - 50;
				float scale = 0.5f + r.nextFloat(0.5f);
				MatrixStack matrices = context.getMatrices();
				matrices.push();
				matrices.translate(cx, cy, 0);
				matrices.translate(ex * progress, ey * progress, 0);
				matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(er * progress));
				matrices.scale(scale, scale, scale);
				float h = r.nextFloat();
				float s = 0.2f + r.nextFloat(0.2f);
				float v = 0.9f + r.nextFloat(0.1f);
				int color = MathHelper.hsvToRgb(h, s, v) | (alpha << 24);
				context.drawCenteredTextWithShadow(client.textRenderer, Text.literal(fragment.intuition()), 0, 0, color);
				matrices.pop();
			}
		}
	}

	@Inject(method = "renderMiscOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/Perspective;isFirstPerson()Z", shift = At.Shift.BEFORE))
	private void cerulean$renderViewfinder(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		float duration = tickCounter.getLastFrameDuration();
		this.cerulean$viewfinderScale = MathHelper.lerp(0.5F * duration, this.cerulean$viewfinderScale, 1.125F);
		if (this.client.options.getPerspective().isFirstPerson()) {
			if (this.client.player.isUsingItem() && this.client.player.getActiveItem().isOf(CeruleanItems.CAMERA)) {
				float f = (float)Math.min(context.getScaledWindowWidth(), context.getScaledWindowHeight());
				float h = Math.min((float)context.getScaledWindowWidth() / f, (float)context.getScaledWindowHeight() / f) * this.cerulean$viewfinderScale;
				int i = MathHelper.floor(f * h);
				int j = MathHelper.floor(f * h);
				int k = (context.getScaledWindowWidth() - i) / 2;
				int l = (context.getScaledWindowHeight() - j) / 2;
				int m = k + i;
				int n = l + j;
				RenderSystem.enableBlend();
				context.drawTexture(Cerulean.id("textures/gui/viewfinder.png"), k, l, -90, 0.0F, 0.0F, i, j, i, j);
				RenderSystem.disableBlend();

				context.fill(RenderLayer.getGuiOverlay(), 0, n, context.getScaledWindowWidth(), context.getScaledWindowHeight(), -90, Colors.BLACK);
				context.fill(RenderLayer.getGuiOverlay(), 0, 0, context.getScaledWindowWidth(), l, -90, Colors.BLACK);
				context.fill(RenderLayer.getGuiOverlay(), 0, l, k, n, -90, Colors.BLACK);
				context.fill(RenderLayer.getGuiOverlay(), m, l, context.getScaledWindowWidth(), n, -90, Colors.BLACK);
			} else {
				this.cerulean$viewfinderScale = 0.5f;
			}
		}
	}

	@Override
	public void recall(String intuition) {
		fragments.add(new Fragment(intuition, System.currentTimeMillis()));
	}
}
