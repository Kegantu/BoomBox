package me.kegantu.boombox.init;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.client.hud.NotificationToast;
import me.kegantu.boombox.entity.BoomBoxEntity;
import me.kegantu.boombox.networking.payloads.C2S.BoomboxPlayC2SPayload;
import me.kegantu.boombox.networking.payloads.C2S.BoomboxStopC2SPayload;
import me.kegantu.boombox.networking.payloads.C2S.SoundPositionUpdateC2SPayload;
import me.kegantu.boombox.networking.payloads.C2S.UpdateVolumeC2SPayload;
import me.kegantu.boombox.networking.payloads.S2C.*;
import me.kegantu.boombox.soundsystem.MusicManager;
import me.kegantu.boombox.soundsystem.ServerMusicManager;
import me.kegantu.boombox.soundsystem.Sound;
import me.kegantu.boombox.utils.AudioDownloader;
import me.kegantu.boombox.utils.YoutubeUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import oshi.util.tuples.Triplet;

import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ModPackets {

    public static void registerC2SPackets(){
        PayloadTypeRegistry.playC2S().register(BoomboxPlayC2SPayload.ID, BoomboxPlayC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(BoomboxStopC2SPayload.ID, BoomboxStopC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SoundPositionUpdateC2SPayload.ID, SoundPositionUpdateC2SPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(UpdateVolumeC2SPayload.ID, UpdateVolumeC2SPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(BoomboxPlayC2SPayload.ID, (payload, context) -> {
            String url = payload.url();
            float volume = payload.volume();
            Vector3f soundPos = payload.position();
            String musicUUID = payload.musicUUID();
            int entityId = payload.entityID();
            BoomBox.LOGGER.info("YA PIDARAS");

            BoomBoxEntity entity = (BoomBoxEntity) context.player().getWorld().getEntityById(entityId);
            entity.setMusicUUID(UUID.fromString(musicUUID));
            BoomboxPlayS2CPayload payloadClient = new BoomboxPlayS2CPayload(url, volume, soundPos, musicUUID, payload.musicOwnerUUID());

            ServerMusicManager.addMusicURL(musicUUID, new Triplet<>(url, soundPos, volume));

            for (ServerPlayerEntity playerEntity : context.server().getPlayerManager().getPlayerList()){
                ServerPlayNetworking.send(playerEntity, payloadClient);
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(BoomboxStopC2SPayload.ID, (payload, context) -> {
            String uuid = payload.musicUUID();
            BoomBox.LOGGER.info("YA DAUN");

            BoomboxStopS2CPayload payloadClient = new BoomboxStopS2CPayload(uuid);

            ServerMusicManager.remove(uuid);

            for (ServerPlayerEntity playerEntity : context.server().getPlayerManager().getPlayerList()){
                ServerPlayNetworking.send(playerEntity, payloadClient);
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(SoundPositionUpdateC2SPayload.ID, (payload, context) -> {
            SoundPositionUpdateS2CPayload payloadClient = new SoundPositionUpdateS2CPayload(payload.position(), payload.musicUUID());
            BoomBox.LOGGER.info("YA CHMO");

            for (ServerPlayerEntity playerEntity : context.server().getPlayerManager().getPlayerList()){
                ServerPlayNetworking.send(playerEntity, payloadClient);
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(UpdateVolumeC2SPayload.ID, (payload, context) -> {
            float volume = payload.volume();
            int entityId = payload.entityID();
            BoomBox.LOGGER.info("YA GANDON");

            UpdateVolumeS2CPayload payloadClient = new UpdateVolumeS2CPayload(volume, payload.musicUUID());

            BoomBoxEntity entity = (BoomBoxEntity) context.player().getWorld().getEntityById(entityId);
            entity.setVolumeServer(volume);
            for (ServerPlayerEntity playerEntity : context.server().getPlayerManager().getPlayerList()){
                ServerPlayNetworking.send(playerEntity, payloadClient);
            }
        });
    }

    public static void registerS2CPackets(){
        PayloadTypeRegistry.playS2C().register(BoomboxPlayS2CPayload.ID, BoomboxPlayS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(BoomboxStopS2CPayload.ID, BoomboxStopS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SoundPositionUpdateS2CPayload.ID, SoundPositionUpdateS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(UpdateVolumeS2CPayload.ID, UpdateVolumeS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(BoomboxOnJoinSyncS2CPayload.ID, BoomboxOnJoinSyncS2CPayload.CODEC);

        ClientPlayNetworking.registerGlobalReceiver(BoomboxPlayS2CPayload.ID, (payload, context) -> {
            String youtubeLink = payload.url();
            float volume = payload.volume();
            Vec3d position = new Vec3d(payload.position());
            String uuid = payload.musicUUID();
            String musicOwner = payload.musicOwnerUUID();
            BoomBox.LOGGER.info("YA PIDARAS CLIENT");

            CompletableFuture<Path> futureFfmpeg = CompletableFuture.supplyAsync(() -> AudioDownloader.download(youtubeLink, uuid));
            futureFfmpeg.whenComplete((path, exception) -> {
                BoomBox.LOGGER.info(exception.toString());
                if (exception != null && context.player().squaredDistanceTo(position) <= 32 * 32 && context.player().getUuid() == UUID.fromString(musicOwner)) {
                    context.player().sendMessage(Text.literal("Failed To Download an Audio").formatted(Formatting.RED), true);
                    return;
                }

                playMusic(path, volume, position, UUID.fromString(uuid));
                if (context.player().squaredDistanceTo(position) <= 32 * 32){
                    context.client().getToastManager().add(new NotificationToast(YoutubeUtils.getTitle(youtubeLink)));
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(BoomboxStopS2CPayload.ID, (payload, context) -> {
            String musicUUID = payload.musicUUID();
            BoomBox.LOGGER.info("YA DAUN CLIENT");

            if (MusicManager.getSound(musicUUID) != null){
                MusicManager.getSound(musicUUID).stop();
                MusicManager.remove(musicUUID);
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(SoundPositionUpdateS2CPayload.ID, (payload, context) -> {
            Vector3f position = payload.position();
            String musicUUID = payload.musicUUID();
            BoomBox.LOGGER.info("YA CHMO CLIENT");

            if (MusicManager.getSound(musicUUID) == null){
                return;
            }

            MusicManager.getSound(musicUUID).setPosition(position);
        });

        ClientPlayNetworking.registerGlobalReceiver(BoomboxOnJoinSyncS2CPayload.ID, (payload, context) -> {
            String youtubeLink = payload.url();
            float volume = payload.volume();
            Vec3d position = new Vec3d(payload.position());
            String uuid = payload.musicUUID();
            float playback = payload.playback();
            BoomBox.LOGGER.info("YA HZ IDI NAHUI");

            CompletableFuture<Path> futureFfmpeg = CompletableFuture.supplyAsync(() -> AudioDownloader.download(youtubeLink, uuid));
            futureFfmpeg.thenAccept(path -> playMusic(path, volume, position, UUID.fromString(uuid), playback));
        });

        ClientPlayNetworking.registerGlobalReceiver(UpdateVolumeS2CPayload.ID, (payload, context) -> {
            float volume = payload.volume();
            String musicUUID = payload.musicUUID();
            BoomBox.LOGGER.info("YA GANDON CLIENT");

            if (MusicManager.getSound(musicUUID) == null){
                return;
            }

            MusicManager.getSound(musicUUID).setVolume(volume);
        });
    }

    private static void playMusic(Path output, float volume, Vec3d position, UUID uuid){
        Sound music = new Sound(output, position, volume, uuid);
        music.play();
    }

    private static void playMusic(Path output, float volume, Vec3d position, UUID uuid, float playback){
        Sound music = new Sound(output, position, volume, uuid, playback);
        music.play();
    }
}
