package me.kegantu.boombox.networking.payloads.S2C;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.networking.payloads.C2S.BoomboxStopC2SPayload;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record BoomboxStopS2CPayload(String musicUUID) implements CustomPayload {

    public static final Identifier BOOMBOX_STOP_S2C_ID = Identifier.of(BoomBox.MOD_ID, "boombox_stop_client");
    public static final CustomPayload.Id<BoomboxStopS2CPayload> ID = new CustomPayload.Id<>(BOOMBOX_STOP_S2C_ID);
    public static final PacketCodec<RegistryByteBuf, BoomboxStopS2CPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING,
            BoomboxStopS2CPayload::musicUUID,
            BoomboxStopS2CPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
