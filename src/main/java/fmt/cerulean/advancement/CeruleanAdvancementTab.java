package fmt.cerulean.advancement;

import com.mojang.blaze3d.systems.RenderSystem;
import fmt.cerulean.client.tex.TextureRendering;
import net.minecraft.advancement.AdvancementDisplay;
import net.minecraft.advancement.PlacedAdvancement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.advancement.AdvancementTab;
import net.minecraft.client.gui.screen.advancement.AdvancementTabType;
import net.minecraft.client.gui.screen.advancement.AdvancementWidget;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;

import java.util.Optional;

public class CeruleanAdvancementTab extends AdvancementTab {
	private static final Identifier PARASTAR = Identifier.of("cerulean", "textures/gui/parastar.png");

	public CeruleanAdvancementTab(MinecraftClient client, AdvancementsScreen screen, AdvancementTabType type, int index, PlacedAdvancement root, AdvancementDisplay display) {
		super(client, screen, type, index, root, display);
	}

	public void render(DrawContext context, int x, int y) {
		if (!this.initialized) {
			this.originX = (double)(117 - (this.maxPanX + this.minPanX) / 2);
			this.originY = (double)(56 - (this.maxPanY + this.minPanY) / 2);
			this.initialized = true;
		}

		context.enableScissor(x, y, x + 234, y + 113);
		MatrixStack matrices = context.getMatrices();
		matrices.push();
		matrices.translate((float)x, (float)y, 0.0F);

		Identifier identifier = (Identifier)getDisplay().getBackground().orElse(TextureManager.MISSING_IDENTIFIER);
		int ox = MathHelper.floor(this.originX);
		int oz = MathHelper.floor(this.originY);
		int wrapX = ox % 16;
		int wrapZ = oz % 16;

		for (int ax = -1; ax <= 15; ax++) {
			for (int az = -1; az <= 8; az++) {
				context.drawTexture(identifier, wrapX + 16 * ax, wrapZ + 16 * az, 0.0F, 0.0F, 16, 16, 16, 16);
			}
		}

		// Ticks cannot be used because we're paused

		long time = System.currentTimeMillis();
		double tick = (time / 100.);

		RenderSystem.enableBlend();

		renderStarLayer(matrices, ox, oz, 0.5f, (tick / 9.1) % 32., (tick / 7.1) % 32., -2, 3, 0.125f);
		renderStarLayer(matrices, ox, oz, 0.75f, (tick / 6.4) % 48., (tick / 8.7) % 48., -2, 3, 0.15f);
		renderStarLayer(matrices, ox, oz, 1f, (-tick / 5.5) % 64, (-tick / 7.5) % 64, -2, 3, 0.25f);
		renderStarLayer(matrices, ox, oz, 1.5f, (tick / 3.5) % 96, (-tick / 4.5) % 96, -2, 3, 0.4f);
		renderStarLayer(matrices, ox, oz, 2, (-tick / 2.2) % 128., (tick / 2.4d) % 128., -2, 2, 0.8f);

		RenderSystem.disableBlend();

		for (AdvancementWidget child : this.rootWidget.children) {
			child.renderWidgets(context, ox, oz);
		}

		matrices.pop();
		context.disableScissor();
	}

	@Override
	public void move(double offsetX, double offsetY) {
		this.originX = MathHelper.clamp(this.originX + offsetX, (double)(-((this.maxPanX + 50) - 234)), -(this.minPanX - 50));
		this.originY = MathHelper.clamp(this.originY + offsetY, (double)(-((this.maxPanY + 50) - 113)), -(this.minPanY - 50));
	}

	@Override
	public void addAdvancement(PlacedAdvancement advancement) {
		Optional<AdvancementDisplay> optional = advancement.getAdvancement().display();
		if (!optional.isEmpty()) {
			AdvancementWidget advancementWidget = new CeruleanAdvancementWidget(this, MinecraftClient.getInstance(), advancement, optional.get());
			this.addWidget(advancementWidget, advancement.getAdvancementEntry());
		}
	}

	private static void renderStarLayer(MatrixStack matrices, int x, int z, float size, double transX, double transZ, int rStart, int rEnd, float alpha) {
		matrices.push();
		matrices.translate(transX, transZ, 0);

		matrices.scale(size, size, 1);

		for (int ax = rStart; ax <= rEnd; ax++) {
			for (int az = rStart; az <= rEnd; az++) {
				TextureRendering.drawTexturedQuad(matrices, PARASTAR, (x % 64) + (ax * 64), (z % 64) + (az * 64), 0.0F, 0.0F, 64, 64, 64, 64, 1, 1, 1, alpha);
			}
		}
		matrices.pop();
	}
}
