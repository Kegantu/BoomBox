package me.kegantu.boombox.mixin;

import me.kegantu.boombox.entity.BoomBoxEntity;
import me.kegantu.boombox.init.ModComponents;
import me.kegantu.boombox.init.ModItems;
import me.kegantu.boombox.item.components.BoomboxComponentDataType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

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
		//BoomBox.lastUsedChestBlockPos = null;
	}

	@Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("HEAD"), cancellable = true)
	private void dropBoombox(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir){
		if (!stack.isOf(ModItems.BOOMBOX)){
			return;
		}

		if (stack.get(ModComponents.BOOMBOX_COMPONENT) == null){
			return;
		}

		BoomboxComponentDataType data = stack.get(ModComponents.BOOMBOX_COMPONENT);
		String musicUUID = data.musicUUID();

		if (this.getWorld().isClient) {
			this.swingHand(Hand.MAIN_HAND);
			return;
		}

		BoomBoxEntity boomBoxEntity = new BoomBoxEntity(this.getWorld(), this.getPos(), UUID.fromString(musicUUID), data.volume());
		stack.set(ModComponents.BOOMBOX_COMPONENT, null);
		stack.decrement(1);
		boomBoxEntity.setYaw(this.getYaw());
		this.getWorld().spawnEntity(boomBoxEntity);
		cir.cancel();
	}
}