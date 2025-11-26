package fmt.cerulean.client.screen.handled;

import com.mojang.blaze3d.systems.RenderSystem;
import fmt.cerulean.Cerulean;
import fmt.cerulean.client.color.CrtColor;
import fmt.cerulean.client.color.CrtColors;
import fmt.cerulean.client.color.LerpCrtColor;
import fmt.cerulean.client.render.frame.CeruleanFramebuffers;
import fmt.cerulean.client.render.frame.ScreenFramebuffer;
import fmt.cerulean.client.tex.TextureRendering;
import fmt.cerulean.screen.RealityAnchorScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.*;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.joml.Random;

import java.util.Locale;

public class RealityAnchorScreen extends HandledScreen<RealityAnchorScreenHandler> {
	private static final Identifier TEXTURE = Cerulean.id("textures/gui/block/reality_anchor.png");
	private int tick;
	private final RealityAnchorScreenHandler handler;
	private String key;

	public RealityAnchorScreen(RealityAnchorScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		this.handler = handler;
	}

	@Override
	protected void init() {
		super.init();

		this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
	}

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		setupKey();

		context.drawTexture(TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);

		ScreenFramebuffer framebuffer = CeruleanFramebuffers.get158x59();

		framebuffer.clear();

		RenderSystem.disableCull();

		ScreenFramebuffer.ProjectedDrawContext proj = framebuffer.setupFrameContext();

		DrawContext inner = proj.context();
		int fullness = handler.getFullness();

		LerpCrtColor pulse = new LerpCrtColor((MathHelper.sin(tick / 3f) + 1) / 2f, CrtColors.RED, CrtColors.DEEPRED);

		drawScreenPhase(inner, fullness, pulse, CrtColor.ColorType.HAZE);
		framebuffer.blur(4, delta);

		drawScreenPhase(inner, fullness, pulse, CrtColor.ColorType.GLOW);
		framebuffer.blur(8, delta);

		drawScreenPhase(inner, fullness, pulse, CrtColor.ColorType.BASE);

		proj.reset();

		framebuffer.end();
		framebuffer.beginRead();

		drawFramebuffer(context, framebuffer, x + 9, y + 9);
	}

	private void setupKey() {
		if (key == null) {
			Random random = new Random(handler.getHash());
			StringBuilder sb;
			do {
				sb = new StringBuilder();
				sb.append("RA-");
				for (int i = 0; i < 10; i++) {
					int i1 = random.nextInt(16);
					sb.append(Integer.toHexString(i1).toUpperCase(Locale.ROOT));
				}
			} while (sb.length() > 78);
			key = sb.toString();
		}
	}

	@Override
	protected void handledScreenTick() {
		tick++;
	}

	private static int sinusoidal(int fullness, int tick) {
		return switch (fullness) {
			default -> 8;
			case 1 -> {
				float value = tick / 5.f;
				yield (int) (MathHelper.sin(value) * 5) + 14;
			}
			case 2 -> {
				float value = tick / 5.f;
				yield (int) ((MathHelper.sin(value) * 5) + (MathHelper.sin(value * 0.45f) * 7) + (MathHelper.sin(value * 1.25f) * 3) + 24);
			}
			case 3 -> {
				float value = tick / 4.f;
				yield (int) ((MathHelper.sin(value) * 5) + (MathHelper.sin(value * 4.25f) * 2) + (MathHelper.sin(value * 0.45f) * 4) + (MathHelper.sin(value * 1.7f) * 3) + 32);
			}
			case 4 -> {
				float value = tick / 3.f;
				yield (int) ((MathHelper.sin(value) * 5) + (MathHelper.sin(value * 3.25f) * 3) + (MathHelper.sin(value * 0.35f) * 6) + (MathHelper.sin(value * 2f) * 3) + 48);
			}
			case 5 -> {
				float value = tick / 2.f;
				yield (int) ((MathHelper.sin(value) * 5) + (MathHelper.sin(value * 4.25f) * 2) + (MathHelper.sin(value * 0.35f) * 7) + (MathHelper.sin(value * 2.25f) * 3) + 60);
			}
		};
	}

	private void drawScreenPhase(DrawContext context, int fullness, CrtColor redpulse, CrtColor.ColorType type) {
		int argb = CrtColors.OFFWHITE.get(type);
		int orangeArgb = CrtColors.ORANGE.get(type);

		TextureRendering.drawTexture(context.getMatrices(), TEXTURE, 2, 2, 83, 24, 0, 166, argb, 83, 24, 256, 256);
		TextureRendering.drawTexture(context.getMatrices(), TEXTURE, 2, 2, 83, 24, 0, 166, argb, 83, 24, 256, 256);
		TextureRendering.drawTexture(context.getMatrices(), TEXTURE, 2, 44, 102, 13, 0, 211, fullness == 0 ? redpulse.get(type) : orangeArgb, 102, 13, 256, 256);

		TextureRendering.drawTexture(context.getMatrices(), TEXTURE, 104, 0, 53, 59, 116, 166, orangeArgb, 53, 59, 256, 256);

		int end1 = sinusoidal(fullness, tick);
		int end2 = sinusoidal(fullness, tick + 10);

		context.fill(0, 30, end1, 34, argb);
		context.fill(0, 36, end2, 40, argb);

		for (int i = 0; i < 5; i++) {
			boolean isRed = i > (fullness - 1);
			int greenArgb = isRed ? CrtColors.RED.get(type) : CrtColors.GREEN.get(type);
			TextureRendering.drawTexture(context.getMatrices(), TEXTURE, 112, 4 + (i * 11), 36, 7, 192, isRed ? 7 : 0, greenArgb, 36, 7, 256, 256);
		}

		context.drawText(client.textRenderer, key, 5, 5, argb, false);
		context.drawText(client.textRenderer, "STATUS: ", 5, 16, argb, false);
		if (fullness > 0) {
			context.drawText(client.textRenderer, "OK", 46, 16, CrtColors.GREEN.get(type), false);
		} else {
			context.drawText(client.textRenderer, "ERROR", 46, 16, CrtColors.RED.get(type), false);
		}

		if (fullness == 0) {
			context.drawText(client.textRenderer, "DE-PRESSURIZED", 5, 47, redpulse.get(type), false);
		} else if (fullness == 1) {
			context.drawText(client.textRenderer, "BORDER-LINE", 5, 47, orangeArgb, false);
		} else {
			context.drawText(client.textRenderer, "NOMINAL", 5, 47, orangeArgb, false);
		}
	}

	private void drawFramebuffer(DrawContext context, ScreenFramebuffer framebuffer, int x, int y) {
		int x2 = x + framebuffer.getPixelWidth();
		int y2 = y + framebuffer.getPixelHeight();
		int z = 0;

		// Flip vertically
		int yy = y;
		y = y2;
		y2 = yy;

		float u1 = 0;
		float u2 = 1;
		float v1 = 0;
		float v2 = 1;

		RenderSystem.enableDepthTest();

		RenderSystem.setShaderTexture(0, framebuffer.getColorAttachment());
		RenderSystem.setShader(GameRenderer::getPositionTexProgram);
		Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
		BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

		RenderSystem.disableCull();

		bufferBuilder.vertex(matrix4f, (float)x, (float)y, (float)z).texture(u1, v1);
		bufferBuilder.vertex(matrix4f, (float)x, (float)y2, (float)z).texture(u1, v2);
		bufferBuilder.vertex(matrix4f, (float)x2, (float)y2, (float)z).texture(u2, v2);
		bufferBuilder.vertex(matrix4f, (float)x2, (float)y, (float)z).texture(u2, v1);
		BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());

		RenderSystem.enableCull();
	}
}
