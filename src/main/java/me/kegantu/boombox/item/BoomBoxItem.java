package me.kegantu.boombox.item;

import me.kegantu.boombox.entity.BoomBoxEntity;
import me.kegantu.boombox.init.ModComponents;
import me.kegantu.boombox.item.components.BoomboxComponentDataType;
import me.kegantu.boombox.networking.payloads.C2S.BoomboxStopC2SPayload;
import me.kegantu.boombox.networking.payloads.C2S.SoundPositionUpdateC2SPayload;
import me.kegantu.boombox.networking.payloads.S2C.BoomboxStopS2CPayload;
import me.kegantu.boombox.networking.payloads.S2C.SoundPositionUpdateS2CPayload;
import me.kegantu.boombox.soundsystem.MusicManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public class BoomBoxItem extends Item {
    public BoomBoxItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);

        if (world.isClientSide()){
            return InteractionResult.PASS;
        }

        if (user.isShiftKeyDown()){
            if (stack.get(ModComponents.BOOMBOX_COMPONENT) != null){
                BoomboxComponentDataType data = stack.get(ModComponents.BOOMBOX_COMPONENT);
                BoomboxStopS2CPayload payload = new BoomboxStopS2CPayload(data.musicUUID());
                for (ServerPlayer playerEntity : world.getServer().getPlayerList().getPlayers()){
                    ServerPlayNetworking.send(playerEntity, payload);
                }
                stack.set(ModComponents.BOOMBOX_COMPONENT, null);
            }
            return InteractionResult.SUCCESS_SERVER;
        }

        if (stack.get(ModComponents.BOOMBOX_COMPONENT) != null){
            BoomboxComponentDataType data = stack.get(ModComponents.BOOMBOX_COMPONENT);;
            String musicUUID = data.musicUUID();
            BoomBoxEntity boomBoxEntity = new BoomBoxEntity(world, user.position(), UUID.fromString(musicUUID), data.volume());
            stack.set(ModComponents.BOOMBOX_COMPONENT, null);
            stack.shrink(1);
            boomBoxEntity.setYRot(user.getYRot());
            world.addFreshEntity(boomBoxEntity);
            return InteractionResult.SUCCESS_SERVER;
        }
        BoomBoxEntity boomBoxEntity = new BoomBoxEntity(world, user.position());
        boomBoxEntity.setYRot(user.getYRot());
        world.addFreshEntity(boomBoxEntity);
        return InteractionResult.SUCCESS_SERVER;
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel world, Entity entity, @Nullable EquipmentSlot slot) {
        Player playerEntity = (Player) entity;
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

        if (!world.isClientSide()){
            SoundPositionUpdateS2CPayload payloadClient = new SoundPositionUpdateS2CPayload(playerEntity.position().toVector3f(), UUID);

            for (ServerPlayer serverPlayerEntity : world.getServer().getPlayerList().getPlayers()){
                ServerPlayNetworking.send(serverPlayerEntity, payloadClient);
            }
        }
    }
}