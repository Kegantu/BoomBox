package me.kegantu.boombox.mixin;

import me.kegantu.boombox.entity.BoomBoxEntity;
import me.kegantu.boombox.init.ModComponents;
import me.kegantu.boombox.init.ModItems;
import me.kegantu.boombox.item.components.BoomboxComponentDataType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    @Shadow public abstract ItemStack getItem();

    public ItemEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo ci){
        ItemStack stack = this.getItem();

        if (!stack.is(ModItems.BOOMBOX)){
            return;
        }

        if (stack.get(ModComponents.BOOMBOX_COMPONENT) == null){
            return;
        }

        BoomboxComponentDataType data = stack.get(ModComponents.BOOMBOX_COMPONENT);
        String musicUUID = data.musicUUID();

        if (this.level().isClientSide()) {
            return;
        }

        BoomBoxEntity boomBoxEntity = new BoomBoxEntity(this.level(), this.position(), UUID.fromString(musicUUID), data.volume());
        stack.set(ModComponents.BOOMBOX_COMPONENT, null);
        stack.shrink(1);
        //boomBoxEntity.setYaw(this.getYaw());
        this.level().addFreshEntity(boomBoxEntity);
        ci.cancel();
    }
}
