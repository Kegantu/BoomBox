package me.kegantu.boombox.networking.payloads.C2S;

import me.kegantu.boombox.BoomBox;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public record SoundPositionUpdateC2SPayload(Vector3f position, String musicUUID) implements CustomPayload {

    public static final Identifier SOUND_POSITION_UPDATE_C2S_ID = Identifier.of(BoomBox.MOD_ID, "sound_position_update_server");
    public static final CustomPayload.Id<SoundPositionUpdateC2SPayload> ID = new CustomPayload.Id<>(SOUND_POSITION_UPDATE_C2S_ID);
    public static final PacketCodec<RegistryByteBuf, SoundPositionUpdateC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VECTOR3F,
            SoundPositionUpdateC2SPayload::position,
            PacketCodecs.STRING,
            SoundPositionUpdateC2SPayload::musicUUID,
            SoundPositionUpdateC2SPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
