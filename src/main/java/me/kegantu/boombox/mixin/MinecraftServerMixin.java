package me.kegantu.boombox.mixin;

import me.kegantu.boombox.soundsystem.ServerMusicManager;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Inject(method = "stop", at = @At("HEAD"))
    private void cleanServerMusicManager(boolean waitForShutdown, CallbackInfo ci){
        ServerMusicManager.clearServerSounds();
    }
}
