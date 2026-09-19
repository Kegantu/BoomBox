package me.kegantu.boombox.item;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.entity.BoomBoxEntity;
import me.kegantu.boombox.init.ModComponents;
import me.kegantu.boombox.init.ModPackets;
import me.kegantu.boombox.soundsystem.MusicManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.UUID;

public class BoomBoxItem extends Item {
    public BoomBoxItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient()){
            if (user.isSneaking()){
                if (stack.get(ModComponents.MUSIC_UUID) != null){
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeString(stack.get(ModComponents.MUSIC_UUID));
                    for (ServerPlayerEntity playerEntity : world.getServer().getPlayerManager().getPlayerList()){
                        ServerPlayNetworking.send(playerEntity, ModPackets.BOOMBOX_STOP_S2C, buf);
                    }
                    stack.set(ModComponents.MUSIC_UUID, null);
                    stack.set(ModComponents.VOLUME, null);
                }
                return TypedActionResult.success(stack, true);
            }

            if (stack.get(ModComponents.MUSIC_UUID) != null){
                String musicUUID = stack.get(ModComponents.MUSIC_UUID);
                BoomBoxEntity boomBoxEntity = new BoomBoxEntity(world, user.getPos(), UUID.fromString(musicUUID), stack.get(ModComponents.VOLUME));
                stack.set(ModComponents.MUSIC_UUID, null);
                stack.set(ModComponents.VOLUME, null);
                stack.decrement(1);
                boomBoxEntity.setYaw(user.getYaw());
                world.spawnEntity(boomBoxEntity);
                return TypedActionResult.success(stack, true);
            }
            BoomBoxEntity boomBoxEntity = new BoomBoxEntity(world, user.getPos());
            boomBoxEntity.setYaw(user.getYaw());
            world.spawnEntity(boomBoxEntity);
        }
        return TypedActionResult.success(stack, true);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        PlayerEntity playerEntity = (PlayerEntity) entity;
        if (stack.get(ModComponents.MUSIC_UUID) == null){
            return;
        }

        String UUID = stack.get(ModComponents.MUSIC_UUID);

        if (MusicManager.getSound(UUID) == null){
            return;
        }

        if (!MusicManager.getSound(UUID).isPlaying()){
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeString(UUID);
            stack.set(ModComponents.MUSIC_UUID, null);
            stack.set(ModComponents.VOLUME, null);
            ClientPlayNetworking.send(ModPackets.BOOMBOX_STOP_C2S, buf);
            return;
        }

        if (!world.isClient()){
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) playerEntity;
        }

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeVector3f(playerEntity.getPos().toVector3f());
        buf.writeString(UUID);
        ClientPlayNetworking.send(ModPackets.SOUND_POSITION_UPDATE_C2S, buf);
    }
}