package fmt.cerulean.client.render.frame;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.texture.NativeImage;

import java.nio.file.Files;
import java.nio.file.Paths;

public class WindowLikeFramebuffer extends SimpleFramebuffer {
	public WindowLikeFramebuffer(int width, int height) {
		super(width, height, true, MinecraftClient.IS_SYSTEM_MAC);
		setClearColor(0f, 0f, 0f, 0f);
	}

	public void clear() {
		clear(MinecraftClient.IS_SYSTEM_MAC);
		beginWrite(true);
	}

	public void begin() {
		beginWrite(true);
	}

	public void end() {
		endWrite();
		MinecraftClient.getInstance().getFramebuffer().beginWrite(true);
	}

	public void debugWrite() {
		int width = this.textureWidth;
		int height = this.textureHeight;
		NativeImage nativeImage = new NativeImage(width, height, false);
		RenderSystem.bindTexture(this.getColorAttachment());
		nativeImage.loadFromTextureImage(0, true);
		nativeImage.mirrorVertically();
		try {
			Files.write(Paths.get("debug_wlfb.png"), nativeImage.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
