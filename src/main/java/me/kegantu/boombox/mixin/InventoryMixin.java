package me.kegantu.boombox.mixin;

import me.kegantu.boombox.init.ModComponents;
import me.kegantu.boombox.init.ModItems;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

//A.K.A PlayerInventory
@Mixin(Inventory.class)
public abstract class InventoryMixin {

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
