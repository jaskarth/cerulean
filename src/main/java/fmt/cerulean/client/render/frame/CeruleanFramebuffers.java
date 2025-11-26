package fmt.cerulean.client.render.frame;

import net.minecraft.resource.ResourceFactory;

public class CeruleanFramebuffers {
	private static ScreenFramebuffer BUF_158x59;
	private static boolean init;

	public static void init(ResourceFactory resources) {
		if (init) {
			return;
		}
		BUF_158x59 = new ScreenFramebuffer(158, 59, resources);
		init = true;
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
}
