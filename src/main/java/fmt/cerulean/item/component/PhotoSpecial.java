package fmt.cerulean.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.dynamic.Codecs;

import java.util.BitSet;

public record PhotoSpecial(String key, BitSet data) {
	public static final Codec<PhotoSpecial> CODEC = RecordCodecBuilder.create(instance -> instance.group(
	        Codec.STRING.fieldOf("key").forGetter(PhotoSpecial::key),
	        Codecs.BIT_SET.fieldOf("data").forGetter(PhotoSpecial::data)
	).apply(instance, PhotoSpecial::new));

	public static final PacketCodec<ByteBuf, PhotoSpecial> PACKET_CODEC = PacketCodec.tuple(
			PacketCodecs.STRING, PhotoSpecial::key,
			PacketCodecs.BYTE_ARRAY.xmap(BitSet::valueOf, BitSet::toByteArray), PhotoSpecial::data,
			PhotoSpecial::new
	);
}
