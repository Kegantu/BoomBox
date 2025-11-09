package me.kegantu.boombox.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class BoomBoxNoteParticle extends SpriteBillboardParticle {

    private final SpriteProvider spriteProvider;

    public BoomBoxNoteParticle(ClientWorld clientWorld, double x, double y, double z, double xVelocity, double yVelocity, double zVelocity, SpriteProvider sprite) {
        super(clientWorld, x, y, z, xVelocity, yVelocity, zVelocity);
        this.spriteProvider = sprite;
        this.setSprite(sprite);
        this.setVelocity(0f, 0.02f, 0f);
        this.setMaxAge(80);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double x, double y, double z, double xVelocity, double yVelocity, double zVelocity) {
            return new BoomBoxNoteParticle(clientWorld, x, y, z, xVelocity, yVelocity, zVelocity, this.spriteProvider);
        }
    }
}
