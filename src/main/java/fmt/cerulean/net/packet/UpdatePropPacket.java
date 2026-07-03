package fmt.cerulean.net.packet;

import fmt.cerulean.net.CeruleanNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record UpdatePropPacket(int index, int value) implements CustomPayload {
	public static final CustomPayload.Id<UpdatePropPacket> ID = new CustomPayload.Id<>(CeruleanNetworking.UPDATE_PROP);
	public static final PacketCodec<RegistryByteBuf, UpdatePropPacket> CODEC = PacketCodec.tuple(
			PacketCodecs.INTEGER, p -> p.index,
			PacketCodecs.INTEGER, p -> p.value,
			UpdatePropPacket::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
