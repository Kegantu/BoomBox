package me.kegantu.boombox.item;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.entity.BoomBoxEntity;
import me.kegantu.boombox.init.ModPackets;
import me.kegantu.boombox.soundsystem.MusicManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
                if (stack.getSubNbt("MusicUUID") != null){
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeString(stack.getSubNbt("MusicUUID").getString("UUID"));
                    for (ServerPlayerEntity playerEntity : world.getServer().getPlayerManager().getPlayerList()){
                        ServerPlayNetworking.send(playerEntity, ModPackets.BOOMBOX_STOP_S2C, buf);
                    }
                    stack.getNbt().remove("MusicUUID");
                }
                return TypedActionResult.success(stack, true);
            }

            if (stack.getSubNbt("MusicUUID") != null){
                BoomBoxEntity boomBoxEntity = new BoomBoxEntity(world, user.getPos(), UUID.fromString(stack.getSubNbt("MusicUUID").getString("UUID")));
                stack.getNbt().remove("MusicUUID");
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
        if (stack.getSubNbt("MusicUUID") == null){
            return;
        }

        if (MusicManager.getSound(stack.getSubNbt("MusicUUID").getString("UUID")) == null){
            return;
        }

        if (!MusicManager.getSound(stack.getSubNbt("MusicUUID").getString("UUID")).isPlaying()){
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeString(stack.getSubNbt("MusicUUID").getString("UUID"));
            stack.getNbt().remove("MusicUUID");
            ClientPlayNetworking.send(ModPackets.BOOMBOX_STOP_C2S, buf);
            return;
        }

        if (!world.isClient()){
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) playerEntity;

            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeVector3f(serverPlayerEntity.getPos().toVector3f());
            buf.writeString(stack.getSubNbt("MusicUUID").getString("UUID"));
            ServerPlayNetworking.send(serverPlayerEntity, ModPackets.SOUND_POSITION_UPDATE_C2S, buf);
        }
    }
}