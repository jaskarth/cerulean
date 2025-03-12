package fmt.cerulean.net.packet;

import fmt.cerulean.net.CeruleanNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record StaringPacket(int id, float yaw, float pitch) implements CustomPayload {
	public static final Id<StaringPacket> ID = new Id<>(CeruleanNetworking.STARING);
	public static final PacketCodec<RegistryByteBuf, StaringPacket> CODEC = PacketCodec.tuple(
			PacketCodecs.INTEGER, p -> p.id,
			PacketCodecs.FLOAT, p -> p.yaw,
			PacketCodecs.FLOAT, p -> p.pitch,
			StaringPacket::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
