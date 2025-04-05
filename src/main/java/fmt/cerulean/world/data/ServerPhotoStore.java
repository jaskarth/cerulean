package fmt.cerulean.world.data;

import fmt.cerulean.mixin.MinecraftServerAccessor;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.level.storage.LevelStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerPhotoStore implements PhotoStore {
	private final Path base;

	public ServerPhotoStore(ServerWorld world) {
		LevelStorage.Session session = ((MinecraftServerAccessor) world.getServer()).getSession();

		Path path = session.getWorldDirectory(world.getRegistryKey()).resolve("data").resolve("cerulean_photos");
		path.toFile().mkdirs();
		this.base = path;
	}

	@Override
	public @Nullable byte[] get(int id) {
		try {
			return Files.readAllBytes(base.resolve("img_" + id + ".png"));
		} catch (Exception e) {
			return null;
		}
	}

	public boolean add(int id, @NotNull byte[] data) {
		try {
			Files.write(base.resolve("img_" + id + ".png"), data);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void remove(int id) {
		try {
			Files.deleteIfExists(base.resolve("img_" + id + ".png"));
		} catch (Exception e) {

		}
	}
}
