package me.kegantu.boombox.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.kegantu.boombox.init.ModComponents;
import me.kegantu.boombox.init.ModItems;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Optional;

@Mixin(Inventory.class)
public abstract class PlayerInventoryMixin {

    @ModifyArg(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setItem(ILnet/minecraft/world/item/ItemStack;)V"))
    private ItemStack readData(ItemStack x){
        if (!x.is(ModItems.BOOMBOX)){
            return x;
        }

        if (x.get(ModComponents.BOOMBOX_COMPONENT) == null){
            return x;
        }

        x.set(ModComponents.BOOMBOX_COMPONENT, null);
        return x;
    }
}
