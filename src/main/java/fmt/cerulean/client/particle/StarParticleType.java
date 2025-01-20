package fmt.cerulean.client.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fmt.cerulean.registry.CeruleanParticleTypes;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

public class StarParticleType implements ParticleEffect {
	public static final MapCodec<StarParticleType> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.FLOAT.fieldOf("red").forGetter(t -> t.red),
			Codec.FLOAT.fieldOf("green").forGetter(t -> t.green),
			Codec.FLOAT.fieldOf("blue").forGetter(t -> t.blue),
			Codec.BOOL.fieldOf("collision").forGetter(t -> t.collision),
			Codec.BOOL.fieldOf("short_life").forGetter(t -> t.shortLife)
	).apply(instance, StarParticleType::new));

	public static final PacketCodec<RegistryByteBuf, StarParticleType> PACKET_CODEC = PacketCodec.tuple(
			PacketCodecs.FLOAT, t -> t.red,
			PacketCodecs.FLOAT, t -> t.green,
			PacketCodecs.FLOAT, t -> t.blue,
			PacketCodecs.BOOL, t -> t.collision,
			PacketCodecs.BOOL, t -> t.shortLife,
			StarParticleType::new
	);

	public final float red, green, blue;
	public final boolean collision;
	public final boolean shortLife;

	public StarParticleType(float red, float green, float blue, boolean collision, boolean shortLife) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.collision = collision;
		this.shortLife = shortLife;
	}

	@Override
	public ParticleType<?> getType() {
		return CeruleanParticleTypes.STAR;
	}
}
