package me.kegantu.boombox.mixin;

import me.kegantu.boombox.BoomBox;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestBlock.class)
public abstract class ChestBlockMixin {

    @Inject(method = "getMenuProvider", at = @At("HEAD"))
    private void createChestScreenHandlerFactory(BlockState state, Level world, BlockPos pos, CallbackInfoReturnable<MenuProvider> cir){
        //BoomBox.lastUsedChestBlockPos = pos;
    }
}
