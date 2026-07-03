package fmt.cerulean.client.screen;

import fmt.cerulean.Cerulean;
import fmt.cerulean.client.ClientState;
import fmt.cerulean.client.render.frame.CeruleanFramebuffers;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.render.RenderLayer;

public class ImgProgressScreen extends ProgressScreen {
	private final boolean closeAfterFinished;

	public ImgProgressScreen(boolean closeAfterFinished) {
		super(closeAfterFinished);
		this.closeAfterFinished = closeAfterFinished;
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		if (this.done) {
			if (this.closeAfterFinished) {
				this.client.setScreen(null);
			}
		} else {
			CeruleanFramebuffers.getMainCopy1().draw(this.client.getWindow().getFramebufferWidth(), this.client.getWindow().getFramebufferHeight(), false);
		}
	}
}
