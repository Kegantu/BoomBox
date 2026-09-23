package me.kegantu.boombox.networking.payloads.C2S;

import com.mojang.serialization.Codec;
import me.kegantu.boombox.BoomBox;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import org.joml.Vector3f;

import java.util.UUID;

public record BoomboxPlayC2SPayload(String url, float volume, Vector3f position, String musicUUID, int entityID, String musicOwnerUUID) implements CustomPayload {

    public static final Identifier BOOMBOX_PLAY_C2S_ID = Identifier.of(BoomBox.MOD_ID, "boombox_play_server");
    public static final CustomPayload.Id<BoomboxPlayC2SPayload> ID = new CustomPayload.Id<>(BOOMBOX_PLAY_C2S_ID);
    public static final PacketCodec<RegistryByteBuf, BoomboxPlayC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING,
            BoomboxPlayC2SPayload::url,
            PacketCodecs.FLOAT,
            BoomboxPlayC2SPayload::volume,
            PacketCodecs.VECTOR3F,
            BoomboxPlayC2SPayload::position,
            PacketCodecs.STRING,
            BoomboxPlayC2SPayload::musicUUID,
            PacketCodecs.INTEGER,
            BoomboxPlayC2SPayload::entityID,
            PacketCodecs.STRING,
            BoomboxPlayC2SPayload::musicOwnerUUID,
            BoomboxPlayC2SPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
