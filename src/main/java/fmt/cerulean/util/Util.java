package fmt.cerulean.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Util {
	public static final PacketCodec<ByteBuf, Optional<BlockPos>> OPTIONAL_NETWORK_BLOCKPOS = new PacketCodec<>() {
		public Optional<BlockPos> decode(ByteBuf byteBuf) {
			boolean opt = byteBuf.readBoolean();
			if (opt) {
				return Optional.of(PacketByteBuf.readBlockPos(byteBuf));
			} else {
				return Optional.empty();
			}
		}

		public void encode(ByteBuf byteBuf, Optional<BlockPos> optional) {
			if (optional.isPresent()) {
				byteBuf.writeBoolean(true);
				PacketByteBuf.writeBlockPos(byteBuf, optional.get());
			} else {
				byteBuf.writeBoolean(false);
			}
		}
	};

	public static final Direction[] DIRECTIONS = Direction.values();

	public static <T> T pick(List<T> list, Random random) {
		return list.get(random.nextInt(list.size()));
	}

	public static int remuxColor(int c) {
		int r = c & 0xFF;
		int g = (c >> 8) & 0xFF;
		int b = (c >> 16) & 0xFF;

		return (r << 16) | (g << 8) | (b << 0);
	}
}
