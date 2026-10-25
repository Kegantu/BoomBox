package me.kegantu.boombox.mixin;

import me.kegantu.boombox.entity.BoomBoxEntity;
import me.kegantu.boombox.init.ModComponents;
import me.kegantu.boombox.init.ModItems;
import me.kegantu.boombox.item.components.BoomboxComponentDataType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At(value = "TAIL"))
	private void tick(CallbackInfo ci){
		//AL10.alListener3f(AL10.AL_POSITION, (float) this.getX(), (float) getY(), (float) getZ());
	}

	@Inject(method = "closeContainer", at = @At("HEAD"))
	private void resetLastUsedChestBlockPos(CallbackInfo ci){
		//BoomBox.lastUsedChestBlockPos = null;
	}

	@Inject(method = "drop", at = @At("HEAD"), cancellable = true)
	private void dropBoombox(ItemStack stack, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir){
		if (!stack.is(ModItems.BOOMBOX)){
			return;
		}

		if (stack.get(ModComponents.BOOMBOX_COMPONENT) == null){
			return;
		}

		BoomboxComponentDataType data = stack.get(ModComponents.BOOMBOX_COMPONENT);
		String musicUUID = data.musicUUID();

		if (this.level().isClientSide()) {
			this.swing(InteractionHand.MAIN_HAND);
			return;
		}

		BoomBoxEntity boomBoxEntity = new BoomBoxEntity(this.level(), this.position(), UUID.fromString(musicUUID), data.volume());
		stack.set(ModComponents.BOOMBOX_COMPONENT, null);
		stack.shrink(1);
		boomBoxEntity.setYRot(this.getYRot());
		this.level().addFreshEntity(boomBoxEntity);
		cir.cancel();
	}
}