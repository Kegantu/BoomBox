package me.kegantu.boombox.networking.payloads.S2C;

import me.kegantu.boombox.BoomBox;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public record BoomboxPlayS2CPayload(String url, float volume, Vector3f position, String musicUUID, String musicOwnerUUID) implements CustomPayload {

    public static final Identifier BOOMBOX_PLAY_S2C_ID = Identifier.of(BoomBox.MOD_ID, "boombox_play_client");
    public static final CustomPayload.Id<BoomboxPlayS2CPayload> ID = new CustomPayload.Id<>(BOOMBOX_PLAY_S2C_ID);
    public static final PacketCodec<RegistryByteBuf, BoomboxPlayS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING,
            BoomboxPlayS2CPayload::url,
            PacketCodecs.FLOAT,
            BoomboxPlayS2CPayload::volume,
            PacketCodecs.VECTOR3F,
            BoomboxPlayS2CPayload::position,
            PacketCodecs.STRING,
            BoomboxPlayS2CPayload::musicUUID,
            PacketCodecs.STRING,
            BoomboxPlayS2CPayload::musicOwnerUUID,
            BoomboxPlayS2CPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
