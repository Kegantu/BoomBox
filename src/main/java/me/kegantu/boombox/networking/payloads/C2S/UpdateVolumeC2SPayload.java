package me.kegantu.boombox.networking.payloads.C2S;

import me.kegantu.boombox.BoomBox;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record UpdateVolumeC2SPayload(float volume, int entityID, String musicUUID) implements CustomPayload {

    public static final Identifier UPDATE_VOLUME_C2S_ID = Identifier.of(BoomBox.MOD_ID, "update_volume_server");
    public static final CustomPayload.Id<UpdateVolumeC2SPayload> ID = new CustomPayload.Id<>(UPDATE_VOLUME_C2S_ID);
    public static final PacketCodec<RegistryByteBuf, UpdateVolumeC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.FLOAT,
            UpdateVolumeC2SPayload::volume,
            PacketCodecs.INTEGER,
            UpdateVolumeC2SPayload::entityID,
            PacketCodecs.STRING,
            UpdateVolumeC2SPayload::musicUUID,
            UpdateVolumeC2SPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
