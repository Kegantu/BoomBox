package me.kegantu.boombox.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.kegantu.boombox.BoomBox;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

    @WrapOperation(method = "readNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;fromNbt(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/item/ItemStack;"))
    private ItemStack readNbt(NbtCompound nbt, Operation<ItemStack> original){
        ItemStack stack = original.call(nbt);

        if (!stack.isOf(ModItems.BOOMBOX)){
            return stack;
        }

        if (stack.getNbt() == null){
            return stack;
        }

        stack.getNbt().remove("MusicUUID");
        return stack;
    }
}
