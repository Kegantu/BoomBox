package me.kegantu.boombox.networking.payloads.C2S;

import com.mojang.serialization.Codec;
import me.kegantu.boombox.BoomBox;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.UUID;

public record BoomboxPlayC2SPayload(String url, float volume, Vector3fc position, String musicUUID, int entityID, String musicOwnerUUID) implements CustomPacketPayload {

    public static final Identifier BOOMBOX_PLAY_C2S_ID = Identifier.fromNamespaceAndPath(BoomBox.MOD_ID, "boombox_play_server");
    public static final CustomPacketPayload.Type<BoomboxPlayC2SPayload> ID = new CustomPacketPayload.Type<>(BOOMBOX_PLAY_C2S_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, BoomboxPlayC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            BoomboxPlayC2SPayload::url,
            ByteBufCodecs.FLOAT,
            BoomboxPlayC2SPayload::volume,
            ByteBufCodecs.VECTOR3F,
            BoomboxPlayC2SPayload::position,
            ByteBufCodecs.STRING_UTF8,
            BoomboxPlayC2SPayload::musicUUID,
            ByteBufCodecs.INT,
            BoomboxPlayC2SPayload::entityID,
            ByteBufCodecs.STRING_UTF8,
            BoomboxPlayC2SPayload::musicOwnerUUID,
            BoomboxPlayC2SPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
