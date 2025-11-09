package me.kegantu.boombox.init;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.client.particle.BoomBoxNoteParticle;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModParticles {

    public static final DefaultParticleType BOOMBOX_NOTE = FabricParticleTypes.simple();

    public static void register(){
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(BoomBox.MOD_ID, "boombox_note"), BOOMBOX_NOTE);
    }

    public static void registerClient(){
        ParticleFactoryRegistry.getInstance().register(BOOMBOX_NOTE, BoomBoxNoteParticle.Factory::new);
    }
}
