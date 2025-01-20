package fmt.cerulean.net.packet;

import fmt.cerulean.net.CeruleanNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public class CloseBehindPacket implements CustomPayload {
	public static final CustomPayload.Id<CloseBehindPacket> ID = new Id<>(CeruleanNetworking.CLOSE_BEHIND);
	public static final CloseBehindPacket INST = new CloseBehindPacket();
	public static final PacketCodec<RegistryByteBuf, CloseBehindPacket> CODEC = PacketCodec.unit(INST);


	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
