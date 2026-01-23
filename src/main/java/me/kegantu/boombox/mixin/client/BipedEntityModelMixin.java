package me.kegantu.boombox.mixin.client;

import me.kegantu.boombox.init.ModItems;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin <T extends LivingEntity> extends AnimalModel<T> {

    @Shadow @Final public ModelPart rightArm;

    //@Inject(method = "animateArms", at = @At("HEAD"))

    @Shadow @Final public ModelPart leftArm;

    @Inject(method = "positionLeftArm", at = @At("HEAD"), cancellable = true)
    private void positionLeftArm(T entity, CallbackInfo ci){
        if (!(entity instanceof PlayerEntity player)){
            return;
        }

        ItemStack itemStack = player.getOffHandStack();
        if (itemStack.getSubNbt("MusicUUID") == null){
            return;
        }

        if (itemStack.getItem() != ModItems.BOOMBOX){
            return;
        }

        this.leftArm.pitch = 0;
        this.leftArm.yaw = 0;
        this.leftArm.roll = (float) Math.PI + 0.6f;
        ci.cancel();
    }

    @Inject(method = "positionRightArm", at = @At("HEAD"), cancellable = true)
    private void positionRightArm(T entity, CallbackInfo ci){
        if (!(entity instanceof PlayerEntity player)){
            return;
        }

        ItemStack itemStack = player.getMainHandStack();
        if (itemStack.getSubNbt("MusicUUID") == null){
            return;
        }

        if (itemStack.getItem() != ModItems.BOOMBOX){
            return;
        }

        this.rightArm.pitch = 0;
        this.rightArm.yaw = 0;
        this.rightArm.roll = 2.5f;
        ci.cancel();
    }
}
