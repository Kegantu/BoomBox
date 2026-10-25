package me.kegantu.boombox.networking.payloads.C2S;

import me.kegantu.boombox.BoomBox;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record UpdateVolumeC2SPayload(float volume, int entityID, String musicUUID) implements CustomPacketPayload {

    public static final Identifier UPDATE_VOLUME_C2S_ID = Identifier.fromNamespaceAndPath(BoomBox.MOD_ID, "update_volume_server");
    public static final CustomPacketPayload.Type<UpdateVolumeC2SPayload> ID = new CustomPacketPayload.Type<>(UPDATE_VOLUME_C2S_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateVolumeC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            UpdateVolumeC2SPayload::volume,
            ByteBufCodecs.INT,
            UpdateVolumeC2SPayload::entityID,
            ByteBufCodecs.STRING_UTF8,
            UpdateVolumeC2SPayload::musicUUID,
            UpdateVolumeC2SPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
