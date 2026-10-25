package me.kegantu.boombox.mixin.client;

import me.kegantu.boombox.soundsystem.MusicManager;
import me.kegantu.boombox.soundsystem.Sound;
import me.kegantu.boombox.utils.AudioDownloader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.server.IntegratedServer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Minecraft.class)
public abstract class MinecraftClientMixin {

    @Shadow @Nullable public LocalPlayer player;

    @Shadow
    protected abstract boolean isMultiplayerServer();

    @Inject(method = "setScreen", at = @At(value = "HEAD"))
    private void pauseBoomboxAudio(Screen screen, CallbackInfo ci){
        if (!(screen instanceof PauseScreen)){
            return;
        }

        if (this.isMultiplayerServer()){
            return;
        }

        List<Sound> music = MusicManager.getCurrentlyPlayingMusic();
        for (int i = 0; i < music.size(); i++) {
            music.get(i).pause();
        }
    }

    @Inject(method = "setScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/SoundManager;resume()V", shift = At.Shift.AFTER))
    private void unpauseBoomboxAudio(Screen screen, CallbackInfo ci){
        List<Sound> music = MusicManager.getCurrentlyPlayingMusic();
        for (int i = 0; i < music.size(); i++) {
            music.get(i).resume();
        }
    }

    @Inject(method = "updateLevelInEngines(Lnet/minecraft/client/multiplayer/ClientLevel;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/SoundManager;stop()V", shift = At.Shift.AFTER))
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

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;ZZ)V", at = @At("TAIL"))
    private void onDisconnect(CallbackInfo ci){
        MusicManager.clearSounds();
    }
}
