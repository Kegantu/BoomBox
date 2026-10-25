package me.kegantu.boombox.networking.payloads.C2S;

import me.kegantu.boombox.BoomBox;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record BoomboxStopC2SPayload(String musicUUID) implements CustomPacketPayload {

    public static final Identifier BOOMBOX_STOP_C2S_ID = Identifier.fromNamespaceAndPath(BoomBox.MOD_ID, "boombox_stop_server");
    public static final CustomPacketPayload.Type<BoomboxStopC2SPayload> ID = new CustomPacketPayload.Type<>(BOOMBOX_STOP_C2S_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, BoomboxStopC2SPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            BoomboxStopC2SPayload::musicUUID,
            BoomboxStopC2SPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
