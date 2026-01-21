package me.kegantu.boombox.mixin;

import com.mojang.authlib.GameProfile;
import me.kegantu.boombox.init.ModItems;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "dropSelectedItem", at = @At("HEAD"), cancellable = true)
    private void dropBoombox(boolean entireStack, CallbackInfoReturnable<Boolean> cir){
        ItemStack stack = this.getMainHandStack();
        if (stack.isOf(ModItems.BOOMBOX) && stack.getSubNbt("MusicUUID") != null){
            cir.cancel();
        }
    }
}
