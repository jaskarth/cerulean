package fmt.cerulean.net;

import fmt.cerulean.client.ClientState;
import fmt.cerulean.net.packet.CeruleanStateSyncPacket;
import fmt.cerulean.net.packet.SupplyMemoryPacket;
import fmt.cerulean.net.packet.WinsPacket;
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

		ClientPlayNetworking.registerGlobalReceiver(SupplyMemoryPacket.ID, (payload, ctx) -> {
			int id = payload.id();
			byte[] data = payload.data();

			ctx.client().execute(() -> {
				ClientState.PHOTOS.add(id, data);
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(WinsPacket.ID, (payload, ctx) -> {
			MinecraftClient client = ctx.client();
			if (client.player != null) {
				ClientState.wins = payload.wins();
				ClientState.seed = payload.seed();
			}
		});
	}
}
