package fmt.cerulean.net.packet;

import fmt.cerulean.net.CeruleanNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record MagicAttackPacket(int id) implements CustomPayload {
	public static final Id<MagicAttackPacket> ID = new Id<>(CeruleanNetworking.MAGIC_ATTACK);
	public static final PacketCodec<RegistryByteBuf, MagicAttackPacket> CODEC = PacketCodec.tuple(
			PacketCodecs.INTEGER, p -> p.id,
			MagicAttackPacket::new
	);


	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
