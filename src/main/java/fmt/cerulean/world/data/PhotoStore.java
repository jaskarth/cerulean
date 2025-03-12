package fmt.cerulean.world.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PhotoStore {
	@Nullable byte[] get(int id);

	boolean add(int id, @NotNull byte[] data);
}
