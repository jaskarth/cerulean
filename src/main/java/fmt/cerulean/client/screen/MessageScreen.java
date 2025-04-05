package fmt.cerulean.client.screen;

import fmt.cerulean.client.ClientState;
import fmt.cerulean.client.render.Prism;
import fmt.cerulean.mixin.client.GameRendererAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class MessageScreen extends Screen {
	private int tick = 0;
	private int life = -12;
	private int death = 0;
	private int alpha = 255;

	public MessageScreen() {
		super(Text.empty());
		ClientState.truthful = false;
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		int alpha = Math.clamp(this.alpha, 0, 255) << 24;
		int bgColor = alpha | 0x000000;
		context.fill(RenderLayer.getGuiOverlay(), 0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), bgColor);

		if (tick < 4) {
			return;
		}

		if (tick < 24) {
			lightString(context, "Something is changing.", delta);
		} else if (tick > 32 && tick < 52) {
			lightString(context, "Who are you?", delta);
		} else if (tick > 60 && tick < 80) {
			lightString(context, "Open your eyes.", delta);
		}
	}

	private static void lightString(DrawContext context, String text, float delta) {
		MinecraftClient client = MinecraftClient.getInstance();
		int x = (context.getScaledWindowWidth() - client.textRenderer.getWidth(text)) / 2;
		int y = context.getScaledWindowHeight() / 2;
		PostEffectProcessor proc = ((GameRendererAccessor)client.gameRenderer).getBlurPostProcessor();

		context.drawText(client.textRenderer, text, x, y, 0xFF_72592a, false);

		if (proc != null) {
			proc.setUniforms("Radius", 4);
			proc.render(delta);
			client.getFramebuffer().beginWrite(false);
		}

		context.drawText(client.textRenderer, text, x, y, 0xFF_ab8848, false);

		if (proc != null) {
			proc.setUniforms("Radius", 8);
			proc.render(delta);
			client.getFramebuffer().beginWrite(false);
		}

		context.drawText(client.textRenderer, text, x, y, 0xFF_FFFFFF, false);
	}

	@Override
	public void tick() {
		tick++;
//		if (tick == 1) {
//			tick = 0;
//			if (life >= 33) {
//				death++;
//			}
//			life++;
//		}

		if (tick > 84) {
			alpha -= 25;
		}

		if (alpha < -4) {
			this.client.setScreen(null);
		}
	}

	@Override
	public boolean shouldPause() {
		return false;
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
}
