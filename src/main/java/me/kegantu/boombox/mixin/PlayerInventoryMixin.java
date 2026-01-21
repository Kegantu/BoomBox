package me.kegantu.boombox.mixin;

import me.kegantu.boombox.init.ModItems;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

    @Shadow @Final public DefaultedList<ItemStack> offHand;

    @Shadow @Final public DefaultedList<ItemStack> main;

    @Shadow @Final public DefaultedList<ItemStack> armor;

    @Inject(method = "writeNbt", at = @At("HEAD"), cancellable = true)
    private void writeNbt(NbtList nbtList, CallbackInfoReturnable<NbtList> cir){
        for (int i = 0; i < this.main.size(); i++) {
            ItemStack stack = this.main.get(i);
            if (stack.isEmpty()) {
                continue;
            }

            if (stack.isOf(ModItems.BOOMBOX)){
                stack.removeSubNbt("MusicUUID");
            }

            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putByte("Slot", (byte)i);
            this.main.get(i).writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }

        for (int ix = 0; ix < this.armor.size(); ix++) {
            if (this.armor.get(ix).isEmpty()) {
                continue;
            }

            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putByte("Slot", (byte)(ix + 100));
            this.armor.get(ix).writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }

        for (int ixx = 0; ixx < this.offHand.size(); ixx++) {
            ItemStack stack = this.offHand.get(ixx);
            if (stack.isEmpty()) {
                continue;
            }

            if (stack.isOf(ModItems.BOOMBOX)){
                stack.removeSubNbt("MusicUUID");
            }

            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putByte("Slot", (byte)(ixx + 150));
            this.offHand.get(ixx).writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }

        cir.setReturnValue(nbtList);
    }
}
