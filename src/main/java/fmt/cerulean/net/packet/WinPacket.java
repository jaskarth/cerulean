package fmt.cerulean.net.packet;

import java.util.List;

import fmt.cerulean.net.CeruleanNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record WinPacket(long seed, List<Integer> moves) implements CustomPayload {
	public static final Id<WinPacket> ID = new Id<>(CeruleanNetworking.WIN);
	public static final PacketCodec<RegistryByteBuf, WinPacket> CODEC = PacketCodec.tuple(
			PacketCodecs.VAR_LONG, p -> p.seed,
			PacketCodecs.INTEGER.collect(PacketCodecs.toList()), p -> p.moves,
			WinPacket::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}

