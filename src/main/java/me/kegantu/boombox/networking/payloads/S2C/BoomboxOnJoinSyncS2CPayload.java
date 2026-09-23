package me.kegantu.boombox.networking.payloads.S2C;

import me.kegantu.boombox.BoomBox;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public record BoomboxOnJoinSyncS2CPayload(String url, float volume, Vector3f position, String musicUUID, float playback) implements CustomPayload {

    public static final Identifier BOOMBOX_ON_JOIN_SYNC_S2C_ID = Identifier.of(BoomBox.MOD_ID, "boombox_on_join_sync_client");
    public static final CustomPayload.Id<BoomboxOnJoinSyncS2CPayload> ID = new CustomPayload.Id<>(BOOMBOX_ON_JOIN_SYNC_S2C_ID);
    public static final PacketCodec<RegistryByteBuf, BoomboxOnJoinSyncS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING,
            BoomboxOnJoinSyncS2CPayload::url,
            PacketCodecs.FLOAT,
            BoomboxOnJoinSyncS2CPayload::volume,
            PacketCodecs.VECTOR3F,
            BoomboxOnJoinSyncS2CPayload::position,
            PacketCodecs.STRING,
            BoomboxOnJoinSyncS2CPayload::musicUUID,
            PacketCodecs.FLOAT,
            BoomboxOnJoinSyncS2CPayload::playback,
            BoomboxOnJoinSyncS2CPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
