package fmt.cerulean.net.packet;

import fmt.cerulean.net.CeruleanNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public class ReturnAgainPacket implements CustomPayload {
	public static final Id<ReturnAgainPacket> ID = new Id<>(CeruleanNetworking.RETURN_AGAIN);
	public static final ReturnAgainPacket INST = new ReturnAgainPacket();
	public static final PacketCodec<RegistryByteBuf, ReturnAgainPacket> CODEC = PacketCodec.unit(INST);


	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
