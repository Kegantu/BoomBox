package me.kegantu.boombox.networking.payloads.C2S;

import me.kegantu.boombox.BoomBox;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record BoomboxStopC2SPayload(String musicUUID) implements CustomPayload {

    public static final Identifier BOOMBOX_STOP_C2S_ID = Identifier.of(BoomBox.MOD_ID, "boombox_stop_server");
    public static final CustomPayload.Id<BoomboxStopC2SPayload> ID = new CustomPayload.Id<>(BOOMBOX_STOP_C2S_ID);
    public static final PacketCodec<RegistryByteBuf, BoomboxStopC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING,
            BoomboxStopC2SPayload::musicUUID,
            BoomboxStopC2SPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
