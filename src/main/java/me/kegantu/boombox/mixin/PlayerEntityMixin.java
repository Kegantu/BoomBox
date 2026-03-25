package me.kegantu.boombox.mixin;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.cca.BoomboxComponent;
import me.kegantu.boombox.cca.interfaces.CustomPlayerData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At(value = "TAIL"))
	private void tick(CallbackInfo ci){
		//AL10.alListener3f(AL10.AL_POSITION, (float) this.getX(), (float) getY(), (float) getZ());
	}

	@Inject(method = "closeHandledScreen", at = @At("HEAD"))
	private void resetLastUsedChestBlockPos(CallbackInfo ci){
		BoomBox.lastUsedChestBlockPos = null;
	}
}