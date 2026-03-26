package me.kegantu.boombox.mixin;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.entity.BoomBoxEntity;
import me.kegantu.boombox.init.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
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

        if (stack.getSubNbt("MusicUUID") == null){
            return;
        }

        if (this.getWorld().isClient) {
            return;
        }

        BoomBoxEntity boomBoxEntity = new BoomBoxEntity(this.getWorld(), this.getPos(), UUID.fromString(stack.getSubNbt("MusicUUID").getString("UUID")));
        stack.getNbt().remove("MusicUUID");
        stack.decrement(1);
        //boomBoxEntity.setYaw(this.getYaw());
        this.getWorld().spawnEntity(boomBoxEntity);
        ci.cancel();
    }
}
