package me.kegantu.boombox.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.random.Random;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class BoomBoxNoteParticle extends BillboardParticle {

    public BoomBoxNoteParticle(ClientWorld clientWorld, double x, double y, double z, double xVelocity, double yVelocity, double zVelocity, Sprite sprite) {
        super(clientWorld, x, y, z, xVelocity, yVelocity, zVelocity, sprite);
        this.setSprite(sprite);
        this.setVelocity(0f, 0.1f, 0f);
        this.setMaxAge(20);
    }

    @Override
    protected RenderType getRenderType() {
        return BillboardParticle.RenderType.PARTICLE_ATLAS_OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Random random) {
            return new BoomBoxNoteParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider.getSprite(random));
        }
    }
}
