package fmt.cerulean.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fmt.cerulean.util.Util;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public record PosPair(Optional<BlockPos> first, Optional<BlockPos> second) {
	public static final Codec<PosPair> CODEC = RecordCodecBuilder.create(instance -> instance.group(
	        BlockPos.CODEC.optionalFieldOf("first").forGetter(PosPair::first),
	        BlockPos.CODEC.optionalFieldOf("second").forGetter(PosPair::second)
	).apply(instance, PosPair::new));

	public static final PacketCodec<ByteBuf, PosPair> PACKET_CODEC = PacketCodec.tuple(
			Util.OPTIONAL_NETWORK_BLOCKPOS, PosPair::first,
			Util.OPTIONAL_NETWORK_BLOCKPOS, PosPair::second,
			PosPair::new
	);

	public static PosPair empty() {
		return new PosPair(Optional.empty(), Optional.empty());
	}

	public boolean isEmpty() {
		return first.isEmpty() && second.isEmpty();
	}

	public PosPair insert(BlockPos pos) {
		if (first.isEmpty()) {
			return new PosPair(Optional.of(pos), Optional.empty());
		} else if (second.isEmpty()) {
			return new PosPair(first, Optional.of(pos));
		}

		throw new IllegalStateException("Pair is full!");
	}
}
