package me.kegantu.boombox.mixin;

import me.kegantu.boombox.init.ModItems;
import me.kegantu.boombox.soundsystem.MusicManager;
import me.kegantu.boombox.soundsystem.Sound;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow @Nullable public ClientPlayerEntity player;

    @Shadow @Final public GameOptions options;

    @Inject(method = "openPauseMenu", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundManager;pauseAll()V", shift = At.Shift.AFTER))
    private void pauseBoomboxAudio(boolean pause, CallbackInfo ci){
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

    @Inject(method = "reset", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundManager;stopAll()V", shift = At.Shift.AFTER))
    private void stopAllMusic(CallbackInfo ci){
        List<Sound> music = MusicManager.getCurrentlyPlayingMusic();
        for (int i = 0; i < music.size(); i++) {
            music.get(i).stop();
        }
    }
/*
    @Inject(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;wasPressed()Z", ordinal = 6), cancellable = true)
    private void dropItem(CallbackInfo ci){
        while (this.options.dropKey.wasPressed()){
            if (!this.player.isSpectator() && !this.player.getActiveItem().isOf(ModItems.BOOMBOX) && this.player.dropSelectedItem(Screen.hasControlDown())) {
                this.player.swingHand(Hand.MAIN_HAND);
                ci.cancel();
            }
        }
    }*/
}
