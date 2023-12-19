package fmt.cerulean.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.render.RenderLayer;

public class ColorProgressScreen extends ProgressScreen {
	private final boolean closeAfterFinished;
	private final int color;

	public ColorProgressScreen(boolean closeAfterFinished, int color) {
		super(closeAfterFinished);
		this.closeAfterFinished = closeAfterFinished;
		this.color = color;
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		if (this.done) {
			if (this.closeAfterFinished) {
				this.client.setScreen(null);
			}
		} else {
			context.fill(RenderLayer.getGuiOverlay(), 0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), color);
		}
	}
}
