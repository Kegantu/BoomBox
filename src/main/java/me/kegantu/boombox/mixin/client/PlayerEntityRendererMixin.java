package me.kegantu.boombox.mixin.client;

import me.kegantu.boombox.init.ModComponents;
import me.kegantu.boombox.init.ModItems;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, AvatarRenderState, PlayerModel> {

    public PlayerEntityRendererMixin(EntityRendererProvider.Context ctx, PlayerModel model, float shadowRadius) {
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
