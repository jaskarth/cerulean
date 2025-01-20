package fmt.cerulean.net;

import fmt.cerulean.net.packet.CeruleanStateSyncPacket;
import fmt.cerulean.util.Counterful;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

public class CeruleanClientNetworking {
	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(CeruleanStateSyncPacket.ID, (payload, ctx) -> {
			MinecraftClient client = ctx.client();
			if (client.player != null) {
				Counterful.get(client.player).read(payload.nbt());
			}
		});
	}
}
