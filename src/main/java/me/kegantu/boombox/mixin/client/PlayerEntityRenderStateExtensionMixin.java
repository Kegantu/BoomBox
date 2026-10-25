package me.kegantu.boombox.mixin.client;

import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntityRenderState.class)
public class PlayerEntityRenderStateExtensionMixin {

    @Unique private ItemStack boomboxItemStackRight;
    @Unique private ItemStack boomboxItemStackLeft;

    public ItemStack getBoomboxItemStackRightHand() {
        return boomboxItemStackRight;
    }

    public ItemStack getBoomboxItemStackLeftHand() {
        return boomboxItemStackLeft;
    }

    public void setBoomboxItemStackRightHand(ItemStack rightHandStack) {
        this.boomboxItemStackRight = rightHandStack;
    }

    public void setBoomboxItemStackLeftHand(ItemStack leftHandStack) {
        this.boomboxItemStackLeft = leftHandStack;
    }
}
