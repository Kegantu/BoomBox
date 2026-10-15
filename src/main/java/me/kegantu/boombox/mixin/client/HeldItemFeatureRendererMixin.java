package me.kegantu.boombox.mixin.client;

import me.kegantu.boombox.init.ModComponents;
import me.kegantu.boombox.init.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemFeatureRenderer.class)
public abstract class HeldItemFeatureRendererMixin<S extends ArmedEntityRenderState, M extends EntityModel<S> & ModelWithArms> extends FeatureRenderer<S, M> {

    public HeldItemFeatureRendererMixin(FeatureRendererContext<S, M> context) {
        super(context);
    }

    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    private void renderPlayingBoombox(S entityState, ItemRenderState itemState, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci){
        ItemStack stack = arm == Arm.RIGHT ? MinecraftClient.getInstance().player.getMainHandStack() : MinecraftClient.getInstance().player.getOffHandStack();

        if (!stack.isOf(ModItems.BOOMBOX)){
            return;
        }

        if (stack.get(ModComponents.BOOMBOX_COMPONENT) == null){
            return;
        }

        if (itemState.modelTransformationMode.isFirstPerson()){
            return;
        }

        matrices.push();
        this.getContextModel().setArmAngle(arm, matrices);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
        //matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
        matrices.translate((arm == Arm.LEFT ? -1 : 1) * -0.15f, 0.15f, 0.4f);
        itemState.render(matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV);
        matrices.pop();
        ci.cancel();
    }
}
