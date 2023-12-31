package fmt.cerulean.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.BillboardParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;

public class StarParticle extends BillboardParticle {
	private SpriteProvider provider;

	public StarParticle(ClientWorld clientWorld, double x, double y, double z, double vx, double vy, double vz) {
		super(clientWorld, x, y, z, vx, vy, vz);
		setVelocity(vx, vy, vz);
	}

	@Override
	public void tick() {
		super.tick();
		if (age == 0 || maxAge - age < 3) {
			setAlpha(0.5f);
		} else {
			setAlpha(1);
		}
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	private Sprite getCurrentSprite() {
		return provider.getSprite(0, 1);
	}

	@Override
	protected float getMinU() {
		return getCurrentSprite().getMinU();
	}

	@Override
	protected float getMinV() {
		return getCurrentSprite().getMinV();
	}

	@Override
	protected float getMaxU() {
		return getCurrentSprite().getMaxU();
	}

	@Override
	protected float getMaxV() {
		return getCurrentSprite().getMaxV();
	}

	@Override
	protected int getBrightness(float tint) {
		return LightmapTextureManager.MAX_LIGHT_COORDINATE;
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<StarParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(StarParticleType effect, ClientWorld clientWorld, double x, double y, double z, double vx, double vy, double vz) {
			StarParticle particle = new StarParticle(clientWorld, x, y, z, vx, vy, vz);
			particle.setColor(effect.red, effect.green, effect.blue);
			particle.provider = spriteProvider;
			particle.setAlpha(0.5f);
			particle.collidesWithWorld = effect.collision;
			if (effect.shortLife) {
				particle.setMaxAge(6);
			}
			return particle;
		}
	}
}
