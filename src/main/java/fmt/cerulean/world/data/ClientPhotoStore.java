package fmt.cerulean.world.data;

import fmt.cerulean.client.tex.forma.FiguraMentis;
import fmt.cerulean.client.tex.gen.StaticTexture;
import fmt.cerulean.item.component.PhotoSpecial;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ClientPhotoStore implements PhotoStore {
	private final Map<Integer, NativeImageBackedTexture> store = new HashMap<>();
	private final Map<String, NativeImageBackedTexture> specialStore = new HashMap<>();
	private final Map<Integer, Identifier> ids = new HashMap<>();
	private final Map<String, Identifier> specialIds = new HashMap<>();
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
		for (NativeImageBackedTexture value : store.values()) {
			value.close();
		}

		for (NativeImageBackedTexture value : specialStore.values()) {
			value.close();
		}

		store.clear();
		ids.clear();
		lastAsked.clear();
		specialStore.clear();
		specialIds.clear();
	}

	public Identifier getSpecial(PhotoSpecial special) {
		String key = special.key();
		Identifier id = this.specialIds.get(key);
		if (id != null) {
			return id;
		}

		FiguraMentis mens = FiguraMentis.imitatur(special.data());

		id = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture("cerulean_simulacrum/" + id, mens);

		specialStore.put(key, mens);
		specialIds.put(key, id);
		return id;
	}

	public Identifier getClient(String s) {
		Path photos = Paths.get("photos", s);
		try {
			Identifier v = specialIds.get(s);
			if (v != null) {
				return v;
			}
			if (Files.exists(photos)) {
				byte[] bytes = Files.readAllBytes(photos);

				ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length);
				buffer.put(bytes);
				buffer.rewind();
				NativeImage img = NativeImage.read(buffer);

				NativeImageBackedTexture tex = new NativeImageBackedTexture(img);
				Identifier iid = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture("cerulean_photo_client/" + s, tex);
				specialStore.put(s, tex);
				specialIds.put(s, iid);

				return iid;
			}
		} catch (Exception e) {

		}

		return StaticTexture.ID;
	}
}
