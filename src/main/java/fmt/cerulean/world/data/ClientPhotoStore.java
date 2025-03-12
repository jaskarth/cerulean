package fmt.cerulean.world.data;

import fmt.cerulean.net.packet.RequestMemoryPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class ClientPhotoStore implements PhotoStore {
	private final Map<Integer, NativeImageBackedTexture> store = new HashMap<>();
	private final Map<Integer, Identifier> ids = new HashMap<>();
	private final Map<Integer, Long> lastAsked = new HashMap<>();

	@Override
	public @Nullable byte[] get(int id) {
		throw new IllegalStateException("Client cannot retrieve raw photo data");
	}

	@Override
	public boolean add(int id, @NotNull byte[] data) {
		try {
			ByteBuffer buffer = BufferUtils.createByteBuffer(data.length);
			buffer.put(data);
			buffer.rewind();
			NativeImage img = NativeImage.read(buffer);

			NativeImageBackedTexture tex = new NativeImageBackedTexture(img);
			Identifier iid = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture("cerulean_photo/" + id, tex);
			store.put(id, tex);
			ids.put(id, iid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public @Nullable NativeImageBackedTexture getImage(int id) {
		return store.get(id);
	}

	public @Nullable Identifier getId(int id) {
		return this.ids.get(id);
	}

	public void ask(int id) {
		Long l = lastAsked.get(id);
		if (l == null || (System.currentTimeMillis() - l) > 15_000) {
			ClientPlayNetworking.send(new RequestMemoryPacket(id));
			lastAsked.put(id, System.currentTimeMillis());
		}
	}

	public void close() {
		store.clear();
		ids.clear();
		lastAsked.clear();
	}
}
