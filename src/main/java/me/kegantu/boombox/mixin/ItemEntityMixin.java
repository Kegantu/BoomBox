package me.kegantu.boombox.mixin;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.entity.BoomBoxEntity;
import me.kegantu.boombox.init.ModComponents;
import me.kegantu.boombox.init.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    @Shadow public abstract ItemStack getStack();

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo ci){
        ItemStack stack = this.getStack();

        if (!stack.isOf(ModItems.BOOMBOX)){
            return;
        }

        if (stack.get(ModComponents.MUSIC_UUID) == null){
            return;
        }

        String musicUUID = stack.get(ModComponents.MUSIC_UUID);

        if (this.getWorld().isClient) {
            return;
        }

        BoomBoxEntity boomBoxEntity = new BoomBoxEntity(this.getWorld(), this.getPos(), UUID.fromString(musicUUID), stack.get(ModComponents.VOLUME));
        stack.set(ModComponents.MUSIC_UUID, null);
        stack.set(ModComponents.VOLUME, null);
        stack.decrement(1);
        //boomBoxEntity.setYaw(this.getYaw());
        this.getWorld().spawnEntity(boomBoxEntity);
        ci.cancel();
    }
}
