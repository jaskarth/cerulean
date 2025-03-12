package fmt.cerulean.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record PhotoComponent(int id) {
	public static final Codec<PhotoComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
	        Codec.INT.fieldOf("id").forGetter(c -> c.id)
	).apply(instance, PhotoComponent::new));

	public static final PacketCodec<ByteBuf, PhotoComponent> PACKET_CODEC = PacketCodec.tuple(
			PacketCodecs.INTEGER, PhotoComponent::id,
			PhotoComponent::new
	);

	public static PhotoComponent create(int id) {
		return new PhotoComponent(id);
	}
}
