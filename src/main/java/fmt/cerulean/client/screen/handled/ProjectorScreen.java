package fmt.cerulean.client.screen.handled;

import com.mojang.blaze3d.systems.RenderSystem;
import fmt.cerulean.Cerulean;
import fmt.cerulean.client.color.CrtColor;
import fmt.cerulean.client.color.CrtColors;
import fmt.cerulean.client.color.LerpCrtColor;
import fmt.cerulean.client.color.RecCrtColor;
import fmt.cerulean.client.render.frame.CeruleanFramebuffers;
import fmt.cerulean.client.render.frame.ScreenFramebuffer;
import fmt.cerulean.client.tex.TextureRendering;
import fmt.cerulean.screen.ProjectorScreenHandler;
import fmt.cerulean.util.Vec2i;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;

public class ProjectorScreen extends HandledScreen<ProjectorScreenHandler> {
	private static final Identifier TEXTURE = Cerulean.id("textures/gui/block/projector.png");

	private final ProjectorScreenHandler handler;
	private int tick;
	private final List<Slider> sliders = new ArrayList<>();
	private Slider activeSlider = null;

	public ProjectorScreen(ProjectorScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		this.handler = handler;
	}

	@Override
	protected void init() {
		super.init();

		this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;

		// rotation
		sliders.add(new Slider(new Rect2i(x + 78 + 9, y + 1 + 9, 24, 7),
				new Vec2i(78, 101), 4, false,
				-180, 180, this.handler::setRotX));

		sliders.add(new Slider(new Rect2i(x + 78 + 9, y + 1 + 9 + 9, 24, 7),
				new Vec2i(78, 101), 13, false,
				-180, 180, this.handler::setRotY));

		sliders.add(new Slider(new Rect2i(x + 78 + 9, y + 1 + 9 + 18, 24, 7),
				new Vec2i(78, 101), 22, false,
				-180, 180, this.handler::setRotZ));

		// dist
		sliders.add(new Slider(new Rect2i(x + 2 + 9, y + 1 + 9, 37, 7),
				new Vec2i(2, 38), 4, false,
				1, 10, this.handler::setDist));

		// size
		sliders.add(new Slider(new Rect2i(x + 43 + 9, y + 2 + 9, 7, 28),
				new Vec2i(2, 29), 46, true,
				1, 7, this.handler::setSize));

		// alpha
		sliders.add(new Slider(new Rect2i(x + 32 + 9, y + 12 + 9, 7, 24),
					new Vec2i(12, 35), 35, true,
				30, 255, this.handler::setAlpha));
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(context, mouseX, mouseY);
	}

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		for (int i = 0; i < 6; i++) {
			if (handler.changed.get(i)) {
				this.sliders.get(i).setupValue(handler.get(i + 1));
				handler.changed.set(i, false);
			}
		}

		context.drawTexture(TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);

		ScreenFramebuffer framebuffer = CeruleanFramebuffers.get158x59();

		framebuffer.clear();

		RenderSystem.disableCull();

		ScreenFramebuffer.ProjectedDrawContext proj = framebuffer.setupFrameContext();

		DrawContext inner = proj.context();

		LerpCrtColor pulse = new LerpCrtColor((MathHelper.sin(tick / 3f) + 1) / 2f, CrtColors.RED, CrtColors.DEEPRED);

		drawScreenPhase(inner, pulse, CrtColor.ColorType.HAZE);
		framebuffer.blur(4, delta);

		drawScreenPhase(inner, pulse, CrtColor.ColorType.GLOW);
		framebuffer.blur(8, delta);

		drawScreenPhase(inner, pulse, CrtColor.ColorType.BASE);

		proj.reset();

		framebuffer.end();

		ScreenFramebuffer.drawFramebuffer(context, framebuffer, x + 9, y + 9);

		for (Slider slider : sliders) {
//			slider.debugBounds(context);
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (Slider slider : sliders) {
			if (slider.bounds.contains((int) mouseX, (int) mouseY)) {
				this.activeSlider = slider;
				break;
			}
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		this.activeSlider = null;
		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (this.activeSlider != null) {
			if (this.activeSlider.vertical) {
				this.activeSlider.progress = MathHelper.clampedMap(mouseY, this.activeSlider.bounds.getY(),
						this.activeSlider.bounds.getY() + this.activeSlider.bounds.getHeight(), 0, 1);
			} else {
				this.activeSlider.progress = MathHelper.clampedMap(mouseX, this.activeSlider.bounds.getX(),
						this.activeSlider.bounds.getX() + this.activeSlider.bounds.getWidth(), 0, 1);
			}

			this.activeSlider.rehash();
		}
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override
	protected void handledScreenTick() {
		tick++;
	}

	private void drawScreenPhase(DrawContext context, CrtColor redpulse, CrtColor.ColorType type) {
		int greenArgb = new RecCrtColor(0xFF_41ba2c, 0xFF_66e151, 0xFF_98facb).get(type);
		int orangeArgb = CrtColors.ORANGE.get(type);

		TextureRendering.drawTexture(context.getMatrices(), TEXTURE, 0, 0, 158, 59, 0, 167, orangeArgb, 158, 59, 256, 256);

		TextureRendering.drawTexture(context.getMatrices(), TEXTURE, 111, 16, 9, 20, 166, 166, greenArgb, 9, 20, 256, 256);

		context.drawText(client.textRenderer, "DIST", 2, 10, greenArgb, false);

		// alpha
		TextureRendering.drawTexture(context.getMatrices(), TEXTURE, 2, 29, 28, 7, 175, 167, greenArgb, 28, 7, 256, 256);
		context.drawText(client.textRenderer, "ROT", 56, 1, greenArgb, false);
		context.drawText(client.textRenderer, "SIZE", 52, 23, greenArgb, false);

		if (!this.handler.active()) {
			context.drawBorder(2, 44, 93, 13, redpulse.get(type));
			context.drawText(client.textRenderer, "DE-PRESSURIZED", 4, 47, redpulse.get(type), false);
		} else {
			context.drawBorder(2, 44, 93, 13, CrtColors.OFFWHITE.get(type));
			if (handler.slots.get(36).getStack().isEmpty()) {
				context.drawText(client.textRenderer, "NO SUBSTRATE", 4, 47, CrtColors.OFFWHITE.get(type), false);
			} else {
				TextureRendering.drawTexture(context.getMatrices(), TEXTURE, 129, 2, 28, 17, 176, 75, orangeArgb, 28, 17, 256, 256);
				context.drawText(client.textRenderer, "NOMINAL", 4, 47, CrtColors.OFFWHITE.get(type), false);
			}
		}

		for (Slider slider : sliders) {
			slider.render(context, type);
		}
	}

	private static class Slider {
		public final Rect2i bounds;
		public final Vec2i start;
		public final int centerline;
		public final boolean vertical;
		private final int min;
		private final int max;
		public double progress;
		private final IntConsumer applicator;

		public Slider(Rect2i bounds, Vec2i start, int centerline, boolean vertical, int min, int max, IntConsumer applicator) {
			this.bounds = bounds;
			this.start = start;
			this.centerline = centerline;
			this.vertical = vertical;
			this.min = min;
			this.max = max;
			this.progress = 0;
			this.applicator = applicator;
		}

		public void setupValue(int v) {
			this.progress = MathHelper.clampedMap(v, min, max, 0, 1);
		}

		public void render(DrawContext context, CrtColor.ColorType type) {
			double prog = MathHelper.clampedMap(progress, 0, 1, start.x(), start.z());
			int color = CrtColors.OFFWHITE.get(type);
			if (vertical) {
				context.drawHorizontalLine(centerline - 3, centerline + 3, (int) prog, color);
				context.drawHorizontalLine(centerline - 3, centerline + 3, (int) prog - 1 , color);
				context.drawHorizontalLine(centerline - 3, centerline + 3, (int) prog + 1, color);
			} else {
				context.drawVerticalLine((int) prog, centerline - 4, centerline + 4, color);
				context.drawVerticalLine((int) prog - 1, centerline - 4, centerline + 4, color);
				context.drawVerticalLine((int) prog + 1, centerline - 4, centerline + 4, color);
			}
		}

		public void rehash() {
			applicator.accept((int) MathHelper.clampedMap(progress, 0, 1, min, max));
		}

		public void debugBounds(DrawContext context) {
			context.drawBorder(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight(), 0xFF00FF00);
		}
	}
}
