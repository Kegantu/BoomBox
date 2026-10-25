package me.kegantu.boombox.mixin.client;

import me.kegantu.boombox.init.ModComponents;
import me.kegantu.boombox.init.ModItems;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class BipedEntityModelMixin <T extends HumanoidRenderState> extends EntityModel<T> implements ArmedModel, HeadedModel {

    @Shadow @Final public ModelPart rightArm;

    //@Inject(method = "animateArms", at = @At("HEAD"))

    @Shadow @Final public ModelPart leftArm;

    protected BipedEntityModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "poseLeftArm", at = @At("HEAD"), cancellable = true)
    private void positionLeftArm(T state, CallbackInfo ci){
        ItemStack itemStack = state.leftHandItemStack;

        if (!itemStack.is(ModItems.BOOMBOX)){
            return;
        }

        if (itemStack.get(ModComponents.BOOMBOX_COMPONENT) == null){
            return;
        }

        this.leftArm.xRot = 0;
        this.leftArm.yRot = 0;
        this.leftArm.zRot = (float) Math.PI + 0.6f;

        if(state.isCrouching){
            this.leftArm.xRot -= 1.0F;
            this.leftArm.y = 5.2F;
        }

        ci.cancel();
    }

    @Inject(method = "poseRightArm", at = @At("HEAD"), cancellable = true)
    private void positionRightArm(T state, CallbackInfo ci){
        ItemStack itemStack = state.getMainHandItemStack();

        if (!itemStack.is(ModItems.BOOMBOX)){
            return;
        }

        if (itemStack.get(ModComponents.BOOMBOX_COMPONENT) == null){
            return;
        }

        this.rightArm.xRot = 0;
        this.rightArm.yRot = 0;
        this.rightArm.zRot = 2.5f;

        if(state.isCrouching){
            this.rightArm.xRot -= 1.0F;
            this.rightArm.y = 5.2F;
        }

        ci.cancel();
    }
}
