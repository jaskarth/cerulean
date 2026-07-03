package fmt.cerulean.client.screen;

import fmt.cerulean.Cerulean;
import fmt.cerulean.client.ClientState;
import fmt.cerulean.client.render.frame.CeruleanFramebuffers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.text.Text;

import java.util.function.BooleanSupplier;

@Environment(EnvType.CLIENT)
public class ImgDownloadScreen extends Screen {
	private final long loadStartTime;
	private final BooleanSupplier shouldClose;

	public ImgDownloadScreen(BooleanSupplier shouldClose) {
		super(NarratorManager.EMPTY);
		this.shouldClose = shouldClose;
		this.loadStartTime = System.currentTimeMillis();
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}

	@Override
	protected boolean hasUsageText() {
		return false;
	}

	@Override
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
		CeruleanFramebuffers.getMainCopy1().draw(this.client.getWindow().getFramebufferWidth(), this.client.getWindow().getFramebufferHeight(), false);
	}

	@Override
	public void tick() {
		if (this.shouldClose.getAsBoolean() || System.currentTimeMillis() > this.loadStartTime + 30000L) {
			this.close();
		}
	}

	@Override
	public void close() {
		this.client.getNarratorManager().narrate(Text.translatable("narrator.ready_to_play"));
		super.close();
	}

	@Override
	public boolean shouldPause() {
		return false;
	}
}
