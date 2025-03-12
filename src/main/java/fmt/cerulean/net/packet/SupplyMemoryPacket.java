package fmt.cerulean.net.packet;

import fmt.cerulean.net.CeruleanNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record SupplyMemoryPacket(int id, byte[] data) implements CustomPayload {
	public static final Id<SupplyMemoryPacket> ID = new CustomPayload.Id<>(CeruleanNetworking.SUPPLY_MEMORY);
	public static final PacketCodec<RegistryByteBuf, SupplyMemoryPacket> CODEC = PacketCodec.tuple(
			PacketCodecs.INTEGER, p -> p.id,
			// 4M max size
			PacketCodecs.byteArray(1 << 22), p -> p.data,
			SupplyMemoryPacket::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
