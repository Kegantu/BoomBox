package me.kegantu.boombox.item;

import me.kegantu.boombox.entity.BoomBoxEntity;
import me.kegantu.boombox.init.ModComponents;
import me.kegantu.boombox.init.ModPackets;
import me.kegantu.boombox.item.components.BoomboxComponentDataType;
import me.kegantu.boombox.networking.payloads.C2S.BoomboxStopC2SPayload;
import me.kegantu.boombox.networking.payloads.C2S.SoundPositionUpdateC2SPayload;
import me.kegantu.boombox.networking.payloads.S2C.BoomboxStopS2CPayload;
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
                if (stack.get(ModComponents.BOOMBOX_COMPONENT) != null){
                    BoomboxComponentDataType data = stack.get(ModComponents.BOOMBOX_COMPONENT);
                    BoomboxStopS2CPayload payload = new BoomboxStopS2CPayload(data.musicUUID());
                    for (ServerPlayerEntity playerEntity : world.getServer().getPlayerManager().getPlayerList()){
                        ServerPlayNetworking.send(playerEntity, payload);
                    }
                    stack.set(ModComponents.BOOMBOX_COMPONENT, null);
                }
                return TypedActionResult.success(stack, true);
            }

            if (stack.get(ModComponents.BOOMBOX_COMPONENT) != null){
                BoomboxComponentDataType data = stack.get(ModComponents.BOOMBOX_COMPONENT);;
                String musicUUID = data.musicUUID();
                BoomBoxEntity boomBoxEntity = new BoomBoxEntity(world, user.getPos(), UUID.fromString(musicUUID), data.volume());
                stack.set(ModComponents.BOOMBOX_COMPONENT, null);
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
        if (stack.get(ModComponents.BOOMBOX_COMPONENT) == null){
            return;
        }

        BoomboxComponentDataType data = stack.get(ModComponents.BOOMBOX_COMPONENT);
        String UUID = data.musicUUID();

        if (MusicManager.getSound(UUID) == null){
            return;
        }

        if (!MusicManager.getSound(UUID).isPlaying()){
            BoomboxStopC2SPayload payload = new BoomboxStopC2SPayload(UUID);
            stack.set(ModComponents.BOOMBOX_COMPONENT, null);
            ClientPlayNetworking.send(payload);
            return;
        }

        if (!world.isClient()){
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) playerEntity;
        }

        SoundPositionUpdateC2SPayload payload = new SoundPositionUpdateC2SPayload(playerEntity.getPos().toVector3f(), UUID);
        ClientPlayNetworking.send(payload);
    }
}