package fmt.cerulean.registry;

import fmt.cerulean.client.particle.StarParticle;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class CeruleanParticles {
	
	public static void init() {
		ParticleFactoryRegistry.getInstance().register(CeruleanParticleTypes.STAR, StarParticle.Factory::new);
	}
}
