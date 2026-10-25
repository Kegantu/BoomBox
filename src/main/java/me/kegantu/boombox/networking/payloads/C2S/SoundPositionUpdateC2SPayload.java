package me.kegantu.boombox.networking.payloads.C2S;

import me.kegantu.boombox.BoomBox;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public record SoundPositionUpdateC2SPayload(Vector3fc position, String musicUUID) implements CustomPacketPayload {

    public static final Identifier SOUND_POSITION_UPDATE_C2S_ID = Identifier.fromNamespaceAndPath(BoomBox.MOD_ID, "sound_position_update_server");
    public static final CustomPacketPayload.Type<SoundPositionUpdateC2SPayload> ID = new CustomPacketPayload.Type<>(SOUND_POSITION_UPDATE_C2S_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SoundPositionUpdateC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F,
            SoundPositionUpdateC2SPayload::position,
            ByteBufCodecs.STRING_UTF8,
            SoundPositionUpdateC2SPayload::musicUUID,
            SoundPositionUpdateC2SPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
