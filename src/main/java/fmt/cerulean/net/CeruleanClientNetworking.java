package fmt.cerulean.net;

import fmt.cerulean.util.Counterful;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class CeruleanClientNetworking {
	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(CeruleanNetworking.DS_SYNC, (client, handler, buf, ret) -> {
			if (client.player != null) {
				Counterful.get(client.player).read(buf.readNbt());
			}
		});
	}
}
