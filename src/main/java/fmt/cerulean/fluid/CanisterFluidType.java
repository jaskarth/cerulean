package fmt.cerulean.fluid;

import com.mojang.serialization.Codec;
import fmt.cerulean.registry.CeruleanItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public enum CanisterFluidType {
	NONE(null),
	EMPTY(CeruleanItems.EMPTY_DRUM),
	POLYETHYLENE(CeruleanItems.POLYETHYLENE_DRUM);

	public static final Codec<CanisterFluidType> CODEC = Codec.INT.xmap(i -> CanisterFluidType.values()[i], CanisterFluidType::ordinal);
	public static final PacketCodec<ByteBuf, CanisterFluidType> PACKET_CODEC = PacketCodecs.INTEGER.xmap(i -> CanisterFluidType.values()[i], CanisterFluidType::ordinal);
	public final Item item;

	CanisterFluidType(Item item) {
		this.item = item;
	}
}
