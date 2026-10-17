package me.kegantu.boombox.mixin.client;

import me.kegantu.boombox.extensionAccessor.PlayerEntityRenderStateAccessor;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntityRenderState.class)
public class PlayerEntityRenderStateExtensionMixin implements PlayerEntityRenderStateAccessor {

    @Unique private ItemStack boomboxItemStackRight;
    @Unique private ItemStack boomboxItemStackLeft;

    @Override
    public ItemStack getBoomboxItemStackRightHand() {
        return boomboxItemStackRight;
    }

    @Override
    public ItemStack getBoomboxItemStackLeftHand() {
        return boomboxItemStackLeft;
    }

    @Override
    public void setBoomboxItemStackRightHand(ItemStack rightHandStack) {
        this.boomboxItemStackRight = rightHandStack;
    }

    @Override
    public void setBoomboxItemStackLeftHand(ItemStack leftHandStack) {
        this.boomboxItemStackLeft = leftHandStack;
    }
}
