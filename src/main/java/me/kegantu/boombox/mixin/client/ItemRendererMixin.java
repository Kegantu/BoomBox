package me.kegantu.boombox.mixin.client;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.init.ModItems;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Shadow @Final private ItemModels models;
    private final ModelIdentifier BOOMBOX_ON = new ModelIdentifier(BoomBox.MOD_ID, "boombox_on", "inventory");

    @Inject(method = "getModel", at = @At("HEAD"), cancellable = true)
    private void getModel(ItemStack stack, World world, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> cir){
        BakedModel model = stack.isOf(ModItems.BOOMBOX) && stack.getSubNbt("MusicUUID") != null ? this.models.getModelManager().getModel(BOOMBOX_ON) : this.models.getModel(stack);

        ClientWorld clientWorld = world instanceof ClientWorld ? (ClientWorld)world : null;
        BakedModel bakedModel2 = model.getOverrides().apply(model, stack, clientWorld, entity, seed);
        cir.setReturnValue(bakedModel2 == null ? this.models.getModelManager().getMissingModel() : bakedModel2);
    }
}
