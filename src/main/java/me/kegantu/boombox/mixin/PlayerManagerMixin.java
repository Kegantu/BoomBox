package me.kegantu.boombox.mixin;

import me.kegantu.boombox.init.ModPackets;
import me.kegantu.boombox.networking.payloads.S2C.BoomboxOnJoinSyncS2CPayload;
import me.kegantu.boombox.soundsystem.MusicManager;
import me.kegantu.boombox.soundsystem.ServerMusicManager;
import me.kegantu.boombox.soundsystem.Sound;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import oshi.util.tuples.Triplet;

@Mixin(PlayerList.class)
public abstract class PlayerManagerMixin {

    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    private void loadMusicOnJoin(Connection connection, ServerPlayer player, CommonListenerCookie clientData, CallbackInfo ci){
        if (player.level().isClientSide()){
            return;
        }

        for (String musicUUID : ServerMusicManager.getCurrentlyPlayingMusicServerKeys()){
            Triplet<String, Vector3f, Float> musicInfo = ServerMusicManager.getMusicServerInfo(musicUUID);
            String url = musicInfo.getA();
            float volume = musicInfo.getC();
            Vector3f position = musicInfo.getB();

            BoomboxOnJoinSyncS2CPayload payload = new BoomboxOnJoinSyncS2CPayload(url, volume, position, musicUUID,
                    MusicManager.getSound(musicUUID) == null ? 0 : MusicManager.getSound(musicUUID).getPlayback());
            ServerPlayNetworking.send(player, payload);
        }
    }
}
