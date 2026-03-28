package me.kegantu.boombox.init;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.client.hud.NotificationToast;
import me.kegantu.boombox.entity.BoomBoxEntity;
import me.kegantu.boombox.soundsystem.MusicManager;
import me.kegantu.boombox.soundsystem.Sound;
import me.kegantu.boombox.utils.AudioDownloader;
import me.kegantu.boombox.utils.YoutubeUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ModPackets {

    public static final Identifier BOOMBOX_PLAY_S2C = new Identifier(BoomBox.MOD_ID, "boombox_play_client");
    public static final Identifier BOOMBOX_STOP_S2C = new Identifier(BoomBox.MOD_ID, "boombox_stop_client");
    public static final Identifier SOUND_POSITION_UPDATE_S2C = new Identifier(BoomBox.MOD_ID, "sound_position_update_client");

    public static final Identifier BOOMBOX_PLAY_C2S = new Identifier(BoomBox.MOD_ID, "boombox_play_server");
    public static final Identifier BOOMBOX_STOP_C2S = new Identifier(BoomBox.MOD_ID, "boombox_stop_server");
    public static final Identifier SOUND_POSITION_UPDATE_C2S = new Identifier(BoomBox.MOD_ID, "sound_position_update_server");

    public static void registerC2SPackets(){
        ServerPlayNetworking.registerGlobalReceiver(BOOMBOX_PLAY_C2S,
                (server, player, handler, buf, responseSender) ->{
                    PacketByteBuf bufClient = PacketByteBufs.create();
                    bufClient.writeString(buf.readString());
                    bufClient.writeFloat(buf.readFloat());
                    bufClient.writeVector3f(buf.readVector3f());
                    String musicUUID = buf.readString();
                    int entityId = buf.readInt();
                    BoomBoxEntity entity = (BoomBoxEntity) player.getWorld().getEntityById(entityId);
                    entity.setMusicUUID(UUID.fromString(musicUUID));
                    bufClient.writeString(musicUUID);
                    bufClient.writeInt(entityId);
                    bufClient.writeUuid(buf.readUuid());
                    BoomBox.LOGGER.info(musicUUID + " server play");

                    for (ServerPlayerEntity playerEntity : server.getPlayerManager().getPlayerList()){
                        ServerPlayNetworking.send(playerEntity, BOOMBOX_PLAY_S2C, bufClient);
                    }
                });

        ServerPlayNetworking.registerGlobalReceiver(BOOMBOX_STOP_C2S,
                (server, player, handler, buf, responseSender) -> {
                    PacketByteBuf bufClient = PacketByteBufs.create();
                    String uuid = buf.readString();
                    bufClient.writeString(uuid);
                    BoomBox.LOGGER.info(uuid + " server stop");

                    for (ServerPlayerEntity playerEntity : server.getPlayerManager().getPlayerList()){
                        ServerPlayNetworking.send(playerEntity, BOOMBOX_STOP_S2C, bufClient);
                    }
        });

        ServerPlayNetworking.registerGlobalReceiver(SOUND_POSITION_UPDATE_C2S,
                (server, player, handler, buf, responseSender) -> {
                    PacketByteBuf bufClient = PacketByteBufs.create();
                    bufClient.writeVector3f(buf.readVector3f());
                    bufClient.writeString(buf.readString());

                    for (ServerPlayerEntity playerEntity : server.getPlayerManager().getPlayerList()){
                        ServerPlayNetworking.send(playerEntity, SOUND_POSITION_UPDATE_S2C, bufClient);
                    }
                });
    }

    public static void registerS2CPackets(){
        ClientPlayNetworking.registerGlobalReceiver(BOOMBOX_PLAY_S2C, (client, handler, buf, responseSender) -> {
            String youtubeLink = buf.readString();
            float volume = buf.readFloat();
            Vec3d position = new Vec3d(buf.readVector3f());
            String uuid = buf.readString();
            UUID musicOwner = buf.readUuid();
            BoomBox.LOGGER.info(uuid + " client play");

            CompletableFuture<Path> futureFfmpeg = CompletableFuture.supplyAsync(() -> AudioDownloader.download(youtubeLink, uuid));
            futureFfmpeg.whenComplete((path, exception) -> {
                if (exception != null && client.player.squaredDistanceTo(new Vec3d(position.toVector3f())) <= 16 * 16 && client.player.getUuid() == musicOwner) {
                    client.player.sendMessage(Text.literal("Failed To Download an Audio").formatted(Formatting.RED), true);
                    return;
                }

                playMusic(path, volume, position, UUID.fromString(uuid));
                if (client.player.squaredDistanceTo(new Vec3d(position.toVector3f())) <= 16 * 16){
                    client.getToastManager().add(new NotificationToast(YoutubeUtils.getTitle(youtubeLink)));
                }
            });

        });

        ClientPlayNetworking.registerGlobalReceiver(BOOMBOX_STOP_S2C, (client, handler, buf, responseSender) -> {
            String musicUUID = buf.readString();
            BoomBox.LOGGER.info(musicUUID + " client stop");

            if (MusicManager.getSound(musicUUID) != null){
                MusicManager.getSound(musicUUID).stop();
                MusicManager.remove(musicUUID);
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(SOUND_POSITION_UPDATE_S2C, (client, handler, buf, responseSender) -> {
            Vector3f position = buf.readVector3f();
            String musicUUID = buf.readString();

            if (MusicManager.getSound(musicUUID) == null){
                BoomBox.LOGGER.info("ne na hod");
                return;
            }

            MusicManager.getSound(musicUUID).setPosition(position);
        });
    }

    private static void playMusic(Path output, float volume, Vec3d position, UUID uuid){
        Sound music = new Sound(output, position, volume, uuid);
        music.play();
    }
}
