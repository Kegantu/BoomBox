package me.kegantu.boombox.networking.payloads.S2C;

import me.kegantu.boombox.BoomBox;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public record BoomboxPlayS2CPayload(String url, float volume, Vector3fc position, String musicUUID, String musicOwnerUUID) implements CustomPacketPayload {

    public static final Identifier BOOMBOX_PLAY_S2C_ID = Identifier.fromNamespaceAndPath(BoomBox.MOD_ID, "boombox_play_client");
    public static final CustomPacketPayload.Type<BoomboxPlayS2CPayload> ID = new CustomPacketPayload.Type<>(BOOMBOX_PLAY_S2C_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, BoomboxPlayS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            BoomboxPlayS2CPayload::url,
            ByteBufCodecs.FLOAT,
            BoomboxPlayS2CPayload::volume,
            ByteBufCodecs.VECTOR3F,
            BoomboxPlayS2CPayload::position,
            ByteBufCodecs.STRING_UTF8,
            BoomboxPlayS2CPayload::musicUUID,
            ByteBufCodecs.STRING_UTF8,
            BoomboxPlayS2CPayload::musicOwnerUUID,
            BoomboxPlayS2CPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
