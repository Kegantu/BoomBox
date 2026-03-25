package me.kegantu.boombox.cca.interfaces;

import net.minecraft.util.math.BlockPos;

public interface CustomPlayerData {

    BlockPos getLastUsedLootableBlockEntity();

    void setLastUsedLootableBlockEntity(BlockPos pos);
}
