package me.kegantu.boombox.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class BoomBoxNoteParticle extends SingleQuadParticle {

    public BoomBoxNoteParticle(ClientLevel clientWorld, double x, double y, double z, double xVelocity, double yVelocity, double zVelocity, TextureAtlasSprite sprite) {
        super(clientWorld, x, y, z, xVelocity, yVelocity, zVelocity, sprite);
        this.setSprite(sprite);
        this.setParticleSpeed(0f, 0.1f, 0f);
        this.setLifetime(20);
    }

    @Override
    protected Layer getLayer() {
        return SingleQuadParticle.Layer.OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType parameters, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, RandomSource random) {
            return new BoomBoxNoteParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider.get(random));
        }
    }
}
