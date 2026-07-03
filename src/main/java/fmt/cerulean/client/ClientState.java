package fmt.cerulean.client;

import fmt.cerulean.world.data.ClientPhotoStore;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class ClientState {
	public static ClientWorld lastWorld = null;

	public static boolean truthful = false;
	public static int virtigo = 0;
	public static int virtigoColor = 0x000000;

	public static MemoryKind remember = MemoryKind.NONE;
	public static boolean forget = false;
	public static boolean shouldEnact = false;

	public static final ClientPhotoStore PHOTOS = new ClientPhotoStore();

	public static final List<PlayerEntity> EMERGENCIES = new ArrayList<>();

	public static int emergencyRender = -2;

	public static Rect2i memoryLimits = null;
	public static boolean cautious = false;

	public static long seed = -1;
	public static int wins = 0;

	public enum MemoryKind {
		NONE,
		SWEEP_K1,
		ENACT_K2;

		public int k() {
			return ordinal();
		}
	}
}
