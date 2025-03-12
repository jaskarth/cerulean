package fmt.cerulean.net.packet;

import fmt.cerulean.net.CeruleanNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record RequestMemoryPacket(int id) implements CustomPayload {
	public static final Id<RequestMemoryPacket> ID = new Id<>(CeruleanNetworking.REQUEST_MEMORY);
	public static final PacketCodec<RegistryByteBuf, RequestMemoryPacket> CODEC = PacketCodec.tuple(
			PacketCodecs.INTEGER, p -> p.id,
			RequestMemoryPacket::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
