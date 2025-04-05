package fmt.cerulean.net.packet;

import fmt.cerulean.net.CeruleanNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record WinsPacket(int wins, long seed) implements CustomPayload {
	public static final Id<WinsPacket> ID = new Id<>(CeruleanNetworking.WINS);
	public static final PacketCodec<RegistryByteBuf, WinsPacket> CODEC = PacketCodec.tuple(
			PacketCodecs.INTEGER, p -> p.wins,
			PacketCodecs.VAR_LONG, p -> p.seed,
			WinsPacket::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
