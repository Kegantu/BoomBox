package me.kegantu.boombox.soundsystem;

import me.kegantu.boombox.init.ModPackets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ItemStackTiedSound extends Sound{

    public ItemStackTiedSound(Path soundPath, Vec3d position, double volume, UUID uuid) {
        super(soundPath, position, volume, uuid);
    }

    @Override
    public void stop() {
        super.stop();


    }
}
