package me.kegantu.boombox.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.init.ModComponents;
import me.kegantu.boombox.init.ModItems;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Shadow
    @Final
    private BakedModelManager bakedModelManager;
    private final Identifier BOOMBOX_ON_ID = Identifier.of(BoomBox.MOD_ID, "boombox_on");
    private final ModelIdentifier BOOMBOX_ON = new ModelIdentifier(BOOMBOX_ON_ID, "inventory");

    @ModifyReturnValue(method = "getModelOrOverride", at = @At("RETURN"))
    private BakedModel renderBoombox(BakedModel original, BakedModel model, ItemStack stack, @Nullable World world, @Nullable LivingEntity entity, int seed){
        if (stack.isOf(ModItems.BOOMBOX) && stack.get(ModComponents.BOOMBOX_COMPONENT) != null){
            return this.bakedModelManager.getModel(BOOMBOX_ON);
        }
        return original;
    }
}
