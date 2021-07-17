package de.dafuqs.pigment.particle;

import de.dafuqs.pigment.particle.client.ParticleEmitterParticle;
import de.dafuqs.pigment.particle.client.ShootingStarParticle;
import de.dafuqs.pigment.particle.client.SparklestoneSparkleParticle;
import de.dafuqs.pigment.particle.client.VoidFogParticle;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class PigmentParticleFactories {

	public static void register() {
		ParticleFactoryRegistry.getInstance().register(PigmentParticleTypes.SHOOTING_STAR, ShootingStarParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(PigmentParticleTypes.SPARKLESTONE_SPARKLE, SparklestoneSparkleParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(PigmentParticleTypes.PARTICLE_EMITTER, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			ParticleEmitterParticle particle = new ParticleEmitterParticle(world, x, y, z, velocityX, velocityY, velocityZ);
			particle.setSprite(provider);
			return particle;
		});
		ParticleFactoryRegistry.getInstance().register(PigmentParticleTypes.VOID_FOG, VoidFogParticle.Factory::new);
	}

}