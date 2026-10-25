package me.kegantu.boombox.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.kegantu.boombox.init.ModComponents;
import me.kegantu.boombox.init.ModItems;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerItemInHandLayer.class)
public abstract class PlayerHeldItemFeatureRendererMixin<S extends AvatarRenderState, M extends EntityModel<S> & ArmedModel & HeadedModel>
        extends ItemInHandLayer<S, M> {

    public PlayerHeldItemFeatureRendererMixin(RenderLayerParent<S, M> featureRendererContext) {
        super(featureRendererContext);
    }

    @Inject(method = "submitArmWithItem(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lnet/minecraft/client/renderer/item/ItemStackRenderState;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/HumanoidArm;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V", at = @At("HEAD"), cancellable = true)
    private void renderBoombox(S playerEntityRenderState, ItemStackRenderState itemRenderState, ItemStack itemStack, HumanoidArm arm, PoseStack matrixStack, SubmitNodeCollector orderedRenderCommandQueue, int i, CallbackInfo ci){
        if (!itemRenderState.isEmpty()) {
            if (itemStack.is(ModItems.BOOMBOX) && itemStack.has(ModComponents.BOOMBOX_COMPONENT)) {
                this.renderPlayingBoombox(playerEntityRenderState, itemRenderState, arm, matrixStack, orderedRenderCommandQueue, i);
                ci.cancel();
            }
        }
    }

    private void renderPlayingBoombox(S playerEntityRenderState, ItemStackRenderState itemState, HumanoidArm arm, PoseStack matrices, SubmitNodeCollector queue, int light){
        matrices.pushPose();
        this.getParentModel().translateToHand(playerEntityRenderState, arm, matrices);
        matrices.mulPose(Axis.XP.rotationDegrees(-90.0F));
        //matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
        matrices.translate((arm == HumanoidArm.LEFT ? -1 : 1) * -0.15f, 0.15f, 0.4f);
        itemState.submit(matrices, queue, light, OverlayTexture.NO_OVERLAY, playerEntityRenderState.outlineColor);
        matrices.popPose();
    }
}
