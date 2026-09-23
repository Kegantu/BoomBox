package me.kegantu.boombox.networking.payloads.S2C;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.networking.payloads.C2S.UpdateVolumeC2SPayload;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record UpdateVolumeS2CPayload(float volume, String musicUUID) implements CustomPayload {

    public static final Identifier UPDATE_VOLUME_S2C_ID = Identifier.of(BoomBox.MOD_ID, "update_volume_client");
    public static final CustomPayload.Id<UpdateVolumeS2CPayload> ID = new CustomPayload.Id<>(UPDATE_VOLUME_S2C_ID);
    public static final PacketCodec<RegistryByteBuf, UpdateVolumeS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.FLOAT,
            UpdateVolumeS2CPayload::volume,
            PacketCodecs.STRING,
            UpdateVolumeS2CPayload::musicUUID,
            UpdateVolumeS2CPayload::new
    );


    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
