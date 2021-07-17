package de.dafuqs.pigment.particle;

import de.dafuqs.pigment.PigmentCommon;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PigmentParticleTypes {

    public static DefaultParticleType SHOOTING_STAR;
    public static DefaultParticleType SPARKLESTONE_SPARKLE;
    public static DefaultParticleType PARTICLE_EMITTER;
    public static DefaultParticleType VOID_FOG;

    public static DefaultParticleType register(String name, boolean alwaysShow) {
        return Registry.register(Registry.PARTICLE_TYPE, new Identifier(PigmentCommon.MOD_ID, name), FabricParticleTypes.simple(alwaysShow));
    }

    public static void register() {
        SHOOTING_STAR = register("shooting_star", true);
        SPARKLESTONE_SPARKLE = register("sparklestone_sparkle", true);
        PARTICLE_EMITTER = register("particle_emitter", false);
        VOID_FOG = register("void_fog", false);
    }

}
