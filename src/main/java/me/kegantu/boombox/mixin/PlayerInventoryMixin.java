package me.kegantu.boombox.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.kegantu.boombox.init.ModComponents;
import me.kegantu.boombox.init.ModItems;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

    @WrapOperation(method = "readNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;fromNbt(Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;Lnet/minecraft/nbt/NbtElement;)Ljava/util/Optional;"))
    private Optional<ItemStack> readNbt(RegistryWrapper.WrapperLookup registries, NbtElement nbt, Operation<Optional<ItemStack>> original){
        Optional<ItemStack> stack = original.call(registries, nbt);

        if (!stack.get().isOf(ModItems.BOOMBOX)){
            return stack;
        }

        ItemStack boomboxStack = stack.get();

        if (boomboxStack.get(ModComponents.MUSIC_UUID) == null){
            return stack;
        }

        boomboxStack.set(ModComponents.MUSIC_UUID, null);
        boomboxStack.set(ModComponents.VOLUME, null);
        return stack;
    }
}
