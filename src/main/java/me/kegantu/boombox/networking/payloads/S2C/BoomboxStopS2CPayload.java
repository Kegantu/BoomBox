package me.kegantu.boombox.networking.payloads.S2C;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.networking.payloads.C2S.BoomboxStopC2SPayload;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record BoomboxStopS2CPayload(String musicUUID) implements CustomPacketPayload {

    public static final Identifier BOOMBOX_STOP_S2C_ID = Identifier.fromNamespaceAndPath(BoomBox.MOD_ID, "boombox_stop_client");
    public static final CustomPacketPayload.Type<BoomboxStopS2CPayload> ID = new CustomPacketPayload.Type<>(BOOMBOX_STOP_S2C_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, BoomboxStopS2CPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            BoomboxStopS2CPayload::musicUUID,
            BoomboxStopS2CPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
