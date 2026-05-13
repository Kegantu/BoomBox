package me.kegantu.boombox.mixin;

import me.kegantu.boombox.init.ModPackets;
import me.kegantu.boombox.soundsystem.ServerMusicManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import oshi.util.tuples.Triplet;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    private void loadMusicOnJoin(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci){
        for (String musicUUID : ServerMusicManager.getCurrentlyPlayingMusicServerKeys()){
            Triplet<String, Vector3f, Float> musicInfo = ServerMusicManager.getMusicServerInfo(musicUUID);

            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeString(musicInfo.getA());
            buf.writeFloat(musicInfo.getC());
            buf.writeVector3f(musicInfo.getB());
            buf.writeString(musicUUID);
            ServerPlayNetworking.send(player, ModPackets.BOOMBOX_ON_JOIN_SYNC_S2C, buf);
        }
    }
}
