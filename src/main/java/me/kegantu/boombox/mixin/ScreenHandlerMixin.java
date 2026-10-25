package me.kegantu.boombox.mixin;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.init.ModItems;
import me.kegantu.boombox.init.ModPackets;
import me.kegantu.boombox.soundsystem.MusicManager;
import me.kegantu.boombox.soundsystem.Sound;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(AbstractContainerMenu.class)
public abstract class ScreenHandlerMixin {

    @Shadow public abstract ItemStack getCarried();

    @Shadow @Final public NonNullList<Slot> slots;

    @Shadow public abstract ItemStack quickMoveStack(Player player, int slot);

    @Inject(method = "doClick", at = @At("HEAD"), cancellable = true)
    private void insertBoombox(int slotIndex, int button, ClickType actionType, Player player, CallbackInfo ci){
        ItemStack stack = this.getCarried();

        if (actionType == ClickType.QUICK_MOVE){
            stack = this.slots.get(slotIndex).getItem();
        }

        /*NbtCompound musicUUIDCompound = stack.getSubNbt("MusicUUID");

        if (musicUUIDCompound == null){
            return;
        }

        if (slotIndex > slots.size() - 37){
            return;
        }

        if (stack.isOf(ModItems.BOOMBOX)){
            ci.cancel();
        }*/

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

    @Inject(method = "initializeContents", at = @At("TAIL"))
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
