package fmt.cerulean.item.component;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.math.GlobalPos;

public record ReturnToSenderComponent(Optional<GlobalPos> target) {
	public static final Codec<ReturnToSenderComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			GlobalPos.CODEC.optionalFieldOf("target").forGetter(ReturnToSenderComponent::target)
	).apply(instance, ReturnToSenderComponent::new));

	public static final PacketCodec<ByteBuf, ReturnToSenderComponent> PACKET_CODEC = PacketCodec.tuple(
			GlobalPos.PACKET_CODEC.collect(PacketCodecs::optional), ReturnToSenderComponent::target,
			ReturnToSenderComponent::new
	);

	public static PhotoComponent create(int id) {
		return new PhotoComponent(id);
	}
}
