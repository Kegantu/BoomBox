package me.kegantu.boombox.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.toast.ToastManager.Entry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ToastManager.Entry.class)
public abstract class ToastManagerEntryMixin<T extends Toast> {

    @Shadow public abstract T getInstance();

    @Shadow @Final private int topIndex;

    @Shadow protected abstract float getDisappearProgress(long time);

    @WrapOperation(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V"))
    private void customToastAdjustment(MatrixStack instance, float p, float y, float z, Operation<Void> original, @Local(name = "x") int x){
        long l = Util.getMeasuringTimeMs();
        instance.translate(x - this.getInstance().getWidth() * this.getDisappearProgress(l), (float)(this.topIndex * this.getInstance().getHeight()), 800.0F);
    }
}
