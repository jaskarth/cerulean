package fmt.cerulean.net.packet;

import fmt.cerulean.net.CeruleanNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record UploadMemoryPacket(byte[] data, float yaw, float pitch) implements CustomPayload {
	public static final Id<UploadMemoryPacket> ID = new Id<>(CeruleanNetworking.UPLOAD_MEMORY);

	public static final PacketCodec<RegistryByteBuf, UploadMemoryPacket> CODEC = PacketCodec.tuple(
			// 4M max size
			PacketCodecs.byteArray(1 << 22), p -> p.data,
			PacketCodecs.FLOAT, p -> p.yaw,
			PacketCodecs.FLOAT, p -> p.pitch,
			UploadMemoryPacket::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
