package me.kegantu.boombox.mixin.client;

import com.mojang.authlib.GameProfile;
import me.kegantu.boombox.init.ModComponents;
import me.kegantu.boombox.init.ModItems;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayer {

    public ClientPlayerEntityMixin(ClientLevel world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "drop(Z)Z", at = @At("HEAD"), cancellable = true)
    private void dropBoombox(boolean entireStack, CallbackInfoReturnable<Boolean> cir){
        ItemStack stack = this.getMainHandItem();
        if (stack.is(ModItems.BOOMBOX) && stack.get(ModComponents.BOOMBOX_COMPONENT) != null){
            cir.cancel();
            //g
        }
    }
}
