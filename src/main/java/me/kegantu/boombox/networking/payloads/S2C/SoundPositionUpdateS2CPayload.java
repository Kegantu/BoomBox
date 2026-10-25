package me.kegantu.boombox.networking.payloads.S2C;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.networking.payloads.C2S.SoundPositionUpdateC2SPayload;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public record SoundPositionUpdateS2CPayload(Vector3fc position, String musicUUID) implements CustomPacketPayload {

    public static final Identifier SOUND_POSITION_UPDATE_S2C_ID = Identifier.fromNamespaceAndPath(BoomBox.MOD_ID, "sound_position_update_client");
    public static final CustomPacketPayload.Type<SoundPositionUpdateS2CPayload> ID = new CustomPacketPayload.Type<>(SOUND_POSITION_UPDATE_S2C_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SoundPositionUpdateS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F,
            SoundPositionUpdateS2CPayload::position,
            ByteBufCodecs.STRING_UTF8,
            SoundPositionUpdateS2CPayload::musicUUID,
            SoundPositionUpdateS2CPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
