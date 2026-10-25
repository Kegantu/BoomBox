package me.kegantu.boombox.networking.payloads.S2C;

import me.kegantu.boombox.BoomBox;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public record BoomboxOnJoinSyncS2CPayload(String url, float volume, Vector3fc position, String musicUUID, float playback) implements CustomPacketPayload {

    public static final Identifier BOOMBOX_ON_JOIN_SYNC_S2C_ID = Identifier.fromNamespaceAndPath(BoomBox.MOD_ID, "boombox_on_join_sync_client");
    public static final CustomPacketPayload.Type<BoomboxOnJoinSyncS2CPayload> ID = new CustomPacketPayload.Type<>(BOOMBOX_ON_JOIN_SYNC_S2C_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, BoomboxOnJoinSyncS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            BoomboxOnJoinSyncS2CPayload::url,
            ByteBufCodecs.FLOAT,
            BoomboxOnJoinSyncS2CPayload::volume,
            ByteBufCodecs.VECTOR3F,
            BoomboxOnJoinSyncS2CPayload::position,
            ByteBufCodecs.STRING_UTF8,
            BoomboxOnJoinSyncS2CPayload::musicUUID,
            ByteBufCodecs.FLOAT,
            BoomboxOnJoinSyncS2CPayload::playback,
            BoomboxOnJoinSyncS2CPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
