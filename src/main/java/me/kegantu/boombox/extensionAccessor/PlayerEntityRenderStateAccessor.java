package me.kegantu.boombox.extensionAccessor;

import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.item.ItemStack;

public interface PlayerEntityRenderStateAccessor {
    ItemStack getBoomboxItemStackRightHand();
    ItemStack getBoomboxItemStackLeftHand();
    void setBoomboxItemStackRightHand(ItemStack rightHandStack);
    void setBoomboxItemStackLeftHand(ItemStack leftHandStack);
}
