package me.kegantu.boombox.mixin.client;

import me.kegantu.boombox.init.ModComponents;
import me.kegantu.boombox.init.ModItems;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin <T extends BipedEntityRenderState> extends EntityModel<T> implements ModelWithArms, ModelWithHead {

    @Shadow @Final public ModelPart rightArm;

    //@Inject(method = "animateArms", at = @At("HEAD"))

    @Shadow @Final public ModelPart leftArm;

    protected BipedEntityModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "positionLeftArm", at = @At("HEAD"), cancellable = true)
    private void positionLeftArm(T state, CallbackInfo ci){
        ItemStack itemStack = state.leftHandItem;

        if (!itemStack.isOf(ModItems.BOOMBOX)){
            return;
        }

        if (itemStack.get(ModComponents.BOOMBOX_COMPONENT) == null){
            return;
        }

        this.leftArm.pitch = 0;
        this.leftArm.yaw = 0;
        this.leftArm.roll = (float) Math.PI + 0.6f;

        if(state.isInSneakingPose){
            this.leftArm.pitch -= 1.0F;
            this.leftArm.originY = 5.2F;
        }

        ci.cancel();
    }

    @Inject(method = "positionRightArm", at = @At("HEAD"), cancellable = true)
    private void positionRightArm(T state, CallbackInfo ci){
        ItemStack itemStack = state.getMainHandItemStack();

        if (!itemStack.isOf(ModItems.BOOMBOX)){
            return;
        }

        if (itemStack.get(ModComponents.BOOMBOX_COMPONENT) == null){
            return;
        }

        this.rightArm.pitch = 0;
        this.rightArm.yaw = 0;
        this.rightArm.roll = 2.5f;

        if(state.isInSneakingPose){
            this.rightArm.pitch -= 1.0F;
            this.rightArm.originY = 5.2F;
        }

        ci.cancel();
    }
}
