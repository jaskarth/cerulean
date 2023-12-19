package fmt.cerulean.client.screen;

import java.util.function.BooleanSupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class ColorDownloadScreen extends Screen {
	private final long loadStartTime;
	private final BooleanSupplier shouldClose;
	private final int color;

	public ColorDownloadScreen(BooleanSupplier shouldClose, int color) {
		super(NarratorManager.EMPTY);
		this.shouldClose = shouldClose;
		this.color = color;
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
		context.fill(RenderLayer.getGuiOverlay(), 0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), this.color);
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
