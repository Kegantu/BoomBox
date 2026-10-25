package me.kegantu.boombox.mixin.client;

import me.kegantu.boombox.soundsystem.MusicManager;
import me.kegantu.boombox.soundsystem.Sound;
import me.kegantu.boombox.utils.AudioDownloader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.server.integrated.IntegratedServer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow @Nullable public ClientPlayerEntity player;

    @Shadow @Final public GameOptions options;

    @Shadow
    public abstract @org.jspecify.annotations.Nullable IntegratedServer getServer();

    @Inject(method = "setScreen", at = @At(value = "HEAD"))
    private void pauseBoomboxAudio(Screen screen, CallbackInfo ci){
        if (!(screen instanceof GameMenuScreen)){
            return;
        }

        if (this.getServer() == null){
            return;
        }

        List<Sound> music = MusicManager.getCurrentlyPlayingMusic();
        for (int i = 0; i < music.size(); i++) {
            music.get(i).pause();
        }
    }

    @Inject(method = "setScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundManager;resumeAll()V", shift = At.Shift.AFTER))
    private void unpauseBoomboxAudio(Screen screen, CallbackInfo ci){
        List<Sound> music = MusicManager.getCurrentlyPlayingMusic();
        for (int i = 0; i < music.size(); i++) {
            music.get(i).resume();
        }
    }

    @Inject(method = "setWorld(Lnet/minecraft/client/world/ClientWorld;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundManager;stopAll()V", shift = At.Shift.AFTER))
    private void stopAllMusic(CallbackInfo ci){
        List<Sound> music = MusicManager.getCurrentlyPlayingMusic();
        for (int i = 0; i < music.size(); i++) {
            music.get(i).stop();
        }
    }

    @Inject(method = "close", at = @At("HEAD"))
    private void onQuitGame(CallbackInfo ci){
        AudioDownloader.cleanAllSaveDirectory();
    }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;ZZ)V", at = @At("TAIL"))
    private void onDisconnect(CallbackInfo ci){
        MusicManager.clearSounds();
    }
}
