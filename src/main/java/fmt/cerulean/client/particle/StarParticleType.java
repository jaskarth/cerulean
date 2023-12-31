package fmt.cerulean.client.particle;

import fmt.cerulean.registry.CeruleanParticleTypes;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

public class StarParticleType implements ParticleEffect {
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

	@Override
	public void write(PacketByteBuf buf) {
	}

	@Override
	public String asString() {
		return super.toString();
	}
}
