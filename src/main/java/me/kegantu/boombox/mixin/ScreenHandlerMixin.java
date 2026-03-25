package me.kegantu.boombox.mixin;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.init.ModItems;
import me.kegantu.boombox.init.ModPackets;
import me.kegantu.boombox.soundsystem.MusicManager;
import me.kegantu.boombox.soundsystem.Sound;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {

    @Shadow public abstract ItemStack getCursorStack();

    @Shadow @Final public DefaultedList<Slot> slots;

    @Shadow public abstract ItemStack quickMove(PlayerEntity player, int slot);

    @Inject(method = "internalOnSlotClick", at = @At("HEAD"), cancellable = true)
    private void insertBoombox(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci){
        ItemStack stack = this.getCursorStack();

        if (actionType == SlotActionType.QUICK_MOVE){
            stack = this.slots.get(slotIndex).getStack();
        }

        NbtCompound musicUUIDCompound = stack.getSubNbt("MusicUUID");

        if (musicUUIDCompound == null){
            return;
        }

        BoomBox.LOGGER.info(String.valueOf(slotIndex));
        BoomBox.LOGGER.info(String.valueOf(slots.size()));

        if (slotIndex > slots.size() - 37){
            return;
        }

        if (stack.isOf(ModItems.BOOMBOX)){
            ci.cancel();
        }

        /*NbtCompound musicUUIDCompound = stack.getSubNbt("MusicUUID");

        if (musicUUIDCompound == null){
            return;
        }

        String UUID = musicUUIDCompound.getString("UUID");
        Sound sound = MusicManager.getSound(UUID);

        if (sound == null){
            return;
        }

        if (slotIndex > slots.size()){
            return;
        }

        PacketByteBuf buf = PacketByteBufs.create();
        Vector3f chestPosition = new Vector3f(BoomBox.lastUsedChestBlockPos.getX(), BoomBox.lastUsedChestBlockPos.getY(), BoomBox.lastUsedChestBlockPos.getZ());
        buf.writeVector3f(chestPosition);
        buf.writeString(UUID);

        //sound.setPosition(chestPosition);
        ClientPlayNetworking.send(ModPackets.SOUND_POSITION_UPDATE_C2S, buf);*/
    }

    //@Inject(method = "insertItem")

    @Inject(method = "updateSlotStacks", at = @At("TAIL"))
    private void updateBoombox(int revision, List<ItemStack> stacks, ItemStack cursorStack, CallbackInfo ci){
        /*for (int i = 0; i < stacks.size(); i++) {
            if (!stacks.get(i).isOf(ModItems.BOOMBOX)){
                continue;
            }

            if (stacks.get(i).getSubNbt("MusicUUID") == null){
                return;
            }

            if (MusicManager.getSound(stacks.get(i).getSubNbt("MusicUUID").getString("UUID")) == null){
                stacks.get(i).getNbt().remove("MusicUUID");
                return;
            }

            if (MusicManager.getSound(stacks.get(i).getSubNbt("MusicUUID").getString("UUID")).isPlaying()){
                return;
            }

            stacks.get(i).getNbt().remove("MusicUUID");
        }*/
    }
}
