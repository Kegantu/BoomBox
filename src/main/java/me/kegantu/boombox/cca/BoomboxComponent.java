package me.kegantu.boombox.cca;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import me.kegantu.boombox.BoomBox;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public class BoomboxComponent implements CommonTickingComponent, AutoSyncedComponent {

    private final PlayerEntity player;

    private BlockPos lastUsedLootableBlockEntity;

    public BoomboxComponent(PlayerEntity player) {
        this.player = player;
    }

    public static BoomboxComponent get(@NotNull PlayerEntity player) {
        return BoomBox.BOOMBOX_COMPONENT.get(player);
    }

    @Override
    public void tick() {

    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        this.lastUsedLootableBlockEntity = NbtHelper.toBlockPos(nbtCompound);
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        if (lastUsedLootableBlockEntity == null){
            return;
        }

        nbtCompound.putInt("X", this.lastUsedLootableBlockEntity.getX());
        nbtCompound.putInt("Y", this.lastUsedLootableBlockEntity.getY());
        nbtCompound.putInt("Z", this.lastUsedLootableBlockEntity.getZ());
    }

    private void sync() {
        BoomBox.BOOMBOX_COMPONENT.sync(this.player);
    }

    public BlockPos getLastUsedLootableBlockEntity() {
        return lastUsedLootableBlockEntity;
    }

    public void setLastUsedLootableBlockEntity(BlockPos lastUsedLootableBlockEntity) {
        this.lastUsedLootableBlockEntity = lastUsedLootableBlockEntity;
        this.sync();
    }
}
