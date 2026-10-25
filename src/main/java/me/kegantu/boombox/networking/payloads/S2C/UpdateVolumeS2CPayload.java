package me.kegantu.boombox.networking.payloads.S2C;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.networking.payloads.C2S.UpdateVolumeC2SPayload;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record UpdateVolumeS2CPayload(float volume, String musicUUID) implements CustomPacketPayload {

    public static final Identifier UPDATE_VOLUME_S2C_ID = Identifier.fromNamespaceAndPath(BoomBox.MOD_ID, "update_volume_client");
    public static final CustomPacketPayload.Type<UpdateVolumeS2CPayload> ID = new CustomPacketPayload.Type<>(UPDATE_VOLUME_S2C_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateVolumeS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            UpdateVolumeS2CPayload::volume,
            ByteBufCodecs.STRING_UTF8,
            UpdateVolumeS2CPayload::musicUUID,
            UpdateVolumeS2CPayload::new
    );


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
