package me.kegantu.boombox.init;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.client.particle.BoomBoxNoteParticle;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class ModParticles {

    public static final SimpleParticleType BOOMBOX_NOTE = FabricParticleTypes.simple();

    public static void register(){
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, Identifier.fromNamespaceAndPath(BoomBox.MOD_ID, "boombox_note"), BOOMBOX_NOTE);
    }

    public static void registerClient(){
        ParticleFactoryRegistry.getInstance().register(BOOMBOX_NOTE, BoomBoxNoteParticle.Factory::new);
    }
}
