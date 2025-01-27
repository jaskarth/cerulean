package fmt.cerulean.net.packet;

import com.mojang.datafixers.util.Either;

import fmt.cerulean.net.CeruleanNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record InfluencePacket(Either<BlockPos, Integer> target, String intuition) implements CustomPayload {
	public static final CustomPayload.Id<InfluencePacket> ID = new Id<>(CeruleanNetworking.INFLUENCE);
	public static final PacketCodec<RegistryByteBuf, InfluencePacket> CODEC = PacketCodec.tuple(
			PacketCodecs.either(BlockPos.PACKET_CODEC, PacketCodecs.INTEGER), p -> p.target(),
			PacketCodecs.STRING, p -> p.intuition(),
			InfluencePacket::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
