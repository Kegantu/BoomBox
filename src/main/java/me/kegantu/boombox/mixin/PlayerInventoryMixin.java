package me.kegantu.boombox.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.kegantu.boombox.init.ModComponents;
import me.kegantu.boombox.init.ModItems;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Optional;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

    @ModifyArg(method = "readData", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;setStack(ILnet/minecraft/item/ItemStack;)V"))
    private ItemStack readData(ItemStack x){
        if (!x.isOf(ModItems.BOOMBOX)){
            return x;
        }

        if (x.get(ModComponents.BOOMBOX_COMPONENT) == null){
            return x;
        }

        x.set(ModComponents.BOOMBOX_COMPONENT, null);
        return x;
    }
}
