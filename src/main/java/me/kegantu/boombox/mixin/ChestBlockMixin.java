package me.kegantu.boombox.mixin;

import me.kegantu.boombox.BoomBox;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestBlock.class)
public abstract class ChestBlockMixin {

    @Inject(method = "createScreenHandlerFactory", at = @At("HEAD"))
    private void createChestScreenHandlerFactory(BlockState state, World world, BlockPos pos, CallbackInfoReturnable<NamedScreenHandlerFactory> cir){
        //BoomBox.lastUsedChestBlockPos = pos;
    }
}
