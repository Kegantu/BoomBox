package me.kegantu.boombox.mixin.client;

import me.kegantu.boombox.init.ModComponents;
import me.kegantu.boombox.init.ModItems;
//import me.kegantu.boombox.extensionAccessor.PlayerEntityRenderStateAccessor;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityRenderState, PlayerEntityModel> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    /*@Inject(method = "updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderState;clear()V", shift = At.Shift.AFTER))
    private void updateRenderStateBoombox(AbstractClientPlayerEntity abstractClientPlayerEntity, PlayerEntityRenderState playerEntityRenderState, float f, CallbackInfo ci){
        ItemStack itemStack = abstractClientPlayerEntity.getMainHandStack();
        ItemStack offHandStack = abstractClientPlayerEntity.getOffHandStack();
        ((PlayerEntityRenderStateAccessor)playerEntityRenderState).setBoomboxItemStackRightHand(itemStack);
        ((PlayerEntityRenderStateAccessor)playerEntityRenderState).setBoomboxItemStackLeftHand(offHandStack);
    }*/
}
