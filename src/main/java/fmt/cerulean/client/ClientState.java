package fmt.cerulean.client;

import fmt.cerulean.world.data.ClientPhotoStore;
import net.minecraft.client.world.ClientWorld;

public class ClientState {
	public static ClientWorld lastWorld = null;

	public static boolean truthful = false;
	public static int virtigo = 0;
	public static int virtigoColor = 0x000000;

	public static boolean remember = false;
	public static boolean forget = false;

	public static final ClientPhotoStore PHOTOS = new ClientPhotoStore();
}
