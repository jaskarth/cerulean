package fmt.cerulean.net.packet;

import fmt.cerulean.Cerulean;
import fmt.cerulean.net.CeruleanNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record CeruleanStateSyncPacket(NbtCompound nbt) implements CustomPayload {
	public static final CustomPayload.Id<CeruleanStateSyncPacket> ID = new Id<>(CeruleanNetworking.MAGIC_ATTACK);
	public static final PacketCodec<RegistryByteBuf, CeruleanStateSyncPacket> CODEC = PacketCodec.tuple(
			PacketCodecs.NBT_COMPOUND, p -> p.nbt,
			CeruleanStateSyncPacket::new
	);


	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
