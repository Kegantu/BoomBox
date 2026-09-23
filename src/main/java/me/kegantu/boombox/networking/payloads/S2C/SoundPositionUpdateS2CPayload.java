package me.kegantu.boombox.networking.payloads.S2C;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.networking.payloads.C2S.SoundPositionUpdateC2SPayload;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public record SoundPositionUpdateS2CPayload(Vector3f position, String musicUUID) implements CustomPayload {

    public static final Identifier SOUND_POSITION_UPDATE_S2C_ID = Identifier.of(BoomBox.MOD_ID, "sound_position_update_client");
    public static final CustomPayload.Id<SoundPositionUpdateS2CPayload> ID = new CustomPayload.Id<>(SOUND_POSITION_UPDATE_S2C_ID);
    public static final PacketCodec<RegistryByteBuf, SoundPositionUpdateS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VECTOR3F,
            SoundPositionUpdateS2CPayload::position,
            PacketCodecs.STRING,
            SoundPositionUpdateS2CPayload::musicUUID,
            SoundPositionUpdateS2CPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return null;
    }
}
