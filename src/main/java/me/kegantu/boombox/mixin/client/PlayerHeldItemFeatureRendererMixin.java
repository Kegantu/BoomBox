package me.kegantu.boombox.mixin.client;

import me.kegantu.boombox.extensionAccessor.PlayerEntityRenderStateAccessor;
import me.kegantu.boombox.init.ModComponents;
import me.kegantu.boombox.init.ModItems;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.feature.PlayerHeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerHeldItemFeatureRenderer.class)
public abstract class PlayerHeldItemFeatureRendererMixin<S extends PlayerEntityRenderState, M extends EntityModel<S> & ModelWithArms & ModelWithHead>
        extends HeldItemFeatureRenderer<S, M> {

    public PlayerHeldItemFeatureRendererMixin(FeatureRendererContext<S, M> featureRendererContext) {
        super(featureRendererContext);
    }

    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    private void renderBoombox(S entityState, ItemRenderState itemState, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci){
        if (!itemState.isEmpty()) {
            ItemStack rightStack = ((PlayerEntityRenderStateAccessor)entityState).getBoomboxItemStackRightHand();
            ItemStack leftStack = ((PlayerEntityRenderStateAccessor)entityState).getBoomboxItemStackLeftHand();
            if (arm == Arm.RIGHT && rightStack.isOf(ModItems.BOOMBOX) && rightStack.contains(ModComponents.BOOMBOX_COMPONENT)) {
                this.renderPlayingBoombox(itemState, arm, matrices, vertexConsumers, light);
                ci.cancel();
            }
            if (arm == Arm.LEFT && leftStack.isOf(ModItems.BOOMBOX) && leftStack.contains(ModComponents.BOOMBOX_COMPONENT)) {
                this.renderPlayingBoombox(itemState, arm, matrices, vertexConsumers, light);
                ci.cancel();
            }
        }
    }

    private void renderPlayingBoombox(ItemRenderState itemState, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light){
        matrices.push();
        this.getContextModel().setArmAngle(arm, matrices);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
        //matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
        matrices.translate((arm == Arm.LEFT ? -1 : 1) * -0.15f, 0.15f, 0.4f);
        itemState.render(matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV);
        matrices.pop();
    }
}
