package fmt.cerulean.client.screen;

import fmt.cerulean.client.ClientState;
import fmt.cerulean.client.render.Prism;
import net.minecraft.client.font.TextRenderer;
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
		int w1 = client.textRenderer.getWidth("the world behind the wall");
		int w2 = client.textRenderer.getWidth("Seek ");
		int w3 = client.textRenderer.getWidth("Touch ");
		int seekWidth = (context.getScaledWindowWidth() - (w1 + w2)) / 2;
		int touchWidth = ((context.getScaledWindowWidth() - (w1 + w3)) / 2) + 15;

		int yOff = -4;

		VertexConsumerProvider.Immediate buffers = context.getVertexConsumers();
		MatrixStack matrices = context.getMatrices();
		Prism prism = (Prism) this.client.textRenderer;

		for (int y = 0; y < life; y++) {

			if ((y & 1) == 0) {
				if (y >= death) {
					prism.cerulean$drawTransparentText("Seek ", seekWidth, (y * 11) + yOff, alpha | 0xFFFFFF, false, matrices.peek().getPositionMatrix(), buffers, TextRenderer.TextLayerType.SEE_THROUGH, 0, 0xf000f0);
					context.draw();
				}
				prism.cerulean$drawTransparentText("the world behind the wall", seekWidth + w2 - 1, (y * 11) + yOff, alpha | 0xFFFFFF, false, matrices.peek().getPositionMatrix(), buffers, TextRenderer.TextLayerType.SEE_THROUGH, 0, 0xf000f0);
				context.draw();
			} else {
				if (y >= death) {
					prism.cerulean$drawTransparentText("Touch ", touchWidth, (y * 11) + yOff, alpha | 0xFFFFFF, false, matrices.peek().getPositionMatrix(), buffers, TextRenderer.TextLayerType.SEE_THROUGH, 0, 0xf000f0);
					context.draw();
				}
				prism.cerulean$drawTransparentText("the world behind the wall", touchWidth + w3 - 1, (y * 11) + yOff, alpha | 0xFFFFFF, false, matrices.peek().getPositionMatrix(), buffers, TextRenderer.TextLayerType.SEE_THROUGH, 0, 0xf000f0);
				context.draw();
			}
		}
	}

	@Override
	public void tick() {
		tick++;
		if (tick == 1) {
			tick = 0;
			if (life >= 33) {
				death++;
			}
			life++;
		}

		alpha -= 2;

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
