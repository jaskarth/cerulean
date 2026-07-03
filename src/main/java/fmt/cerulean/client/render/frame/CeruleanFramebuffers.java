package fmt.cerulean.client.render.frame;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceFactory;

public class CeruleanFramebuffers {
	private static WindowLikeFramebuffer BUF_MainCopy1;
	private static ScreenFramebuffer BUF_158x59;
	private static boolean init;

	public static void init(ResourceFactory resources) {
		if (BUF_158x59 != null) {
			BUF_158x59.delete();
		}
		if (BUF_MainCopy1 != null) {
			BUF_MainCopy1.delete();
		}

		BUF_158x59 = new ScreenFramebuffer(158, 59, resources);
		MinecraftClient client = MinecraftClient.getInstance();
		BUF_MainCopy1 = new WindowLikeFramebuffer(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());
		init = true;
	}

	public static void onMainWindowResized() {
		if (BUF_MainCopy1 != null) {
			BUF_MainCopy1.delete();
		}

		MinecraftClient client = MinecraftClient.getInstance();
		BUF_MainCopy1 = new WindowLikeFramebuffer(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());
	}

	private static void checkInit() {
		if (!init) {
			throw new IllegalStateException("Trying to use framebuffer before it was init!");
		}
	}

	public static ScreenFramebuffer get158x59() {
		checkInit();

		return BUF_158x59;
	}

	public static WindowLikeFramebuffer getMainCopy1() {
		checkInit();

		return BUF_MainCopy1;
	}
}
