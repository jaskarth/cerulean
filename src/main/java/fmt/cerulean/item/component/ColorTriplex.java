package fmt.cerulean.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fmt.cerulean.flow.FlowResource;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

import java.util.Optional;

public record ColorTriplex(Optional<FlowResource.Color> a, Optional<FlowResource.Color> b, Optional<FlowResource.Color> c) {
	public static final Codec<ColorTriplex> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.xmap(ColorTriplex::in, ColorTriplex::out).fieldOf("a").forGetter(c -> c.a),
			Codec.INT.xmap(ColorTriplex::in, ColorTriplex::out).fieldOf("b").forGetter(c -> c.b),
			Codec.INT.xmap(ColorTriplex::in, ColorTriplex::out).fieldOf("c").forGetter(c -> c.c)
	).apply(instance, ColorTriplex::new));

	public static final PacketCodec<ByteBuf, ColorTriplex> PACKET_CODEC = PacketCodec.tuple(
			PacketCodecs.INTEGER.xmap(ColorTriplex::in, ColorTriplex::out), c -> c.a,
			PacketCodecs.INTEGER.xmap(ColorTriplex::in, ColorTriplex::out), c -> c.b,
			PacketCodecs.INTEGER.xmap(ColorTriplex::in, ColorTriplex::out), c -> c.c,
			ColorTriplex::new
	);

	private static Optional<FlowResource.Color> in(int i) {
		return i == 0 ? Optional.empty() : Optional.of(FlowResource.Color.values()[i - 1]);
	}

	private static int out(Optional<FlowResource.Color> color) {
		return color.isEmpty() ? 0 : color.get().ordinal() + 1;
	}

	public static ColorTriplex empty() {
		return new ColorTriplex(Optional.empty(), Optional.empty(), Optional.empty());
	}

	public boolean contains(FlowResource.Color color) {
		return a.orElse(null) == color || b.orElse(null) == color || c.orElse(null) == color;
	}

	public boolean isPartial() {
		return contains(null);
	}

	public ColorTriplex fill(FlowResource.Color color) {
		if (color == null) {
			throw new IllegalStateException("Can't fill with null!");
		}

		if (a.isEmpty()) {
			return new ColorTriplex(Optional.of(color), b, c);
		}

		if (b.isEmpty()) {
			return new ColorTriplex(a, Optional.of(color), c);
		}

		if (c.isEmpty()) {
			return new ColorTriplex(a, b, Optional.of(color));
		}

		throw new IllegalStateException("Triplex is full!");
	}

	public int toABGR() {
		Vector3f colA = a.map(FlowResource.Color::toRGBVec).orElse(new Vector3f(0, 0, 0));
		Vector3f colB = b.map(FlowResource.Color::toRGBVec).orElse(new Vector3f(0, 0, 0));
		Vector3f colC = c.map(FlowResource.Color::toRGBVec).orElse(new Vector3f(0, 0, 0));

		Vector3f res = colA.add(colB).add(colC);

		int r = (int) MathHelper.clampedMap(res.x, 0, 1, 0, 255);
		int g = (int) MathHelper.clampedMap(res.y, 0, 1, 0, 255);
		int b = (int) MathHelper.clampedMap(res.z, 0, 1, 0, 255);

		return (0xFF << 24) | (r << 16) | (g << 8) | b;
	}
}
