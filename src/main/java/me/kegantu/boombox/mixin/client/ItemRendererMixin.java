package me.kegantu.boombox.mixin.client;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.init.ModItems;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Shadow public abstract ItemModels getModels();

    private final ModelIdentifier BOOMBOX_ON = new ModelIdentifier(BoomBox.MOD_ID, "boombox_on", "inventory");

    @ModifyVariable(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At("HEAD"), argsOnly = true)
    private BakedModel renderBoombox(BakedModel value, ItemStack stack,
                                     ModelTransformationMode renderMode,
                                     boolean leftHanded,
                                     MatrixStack matrices,
                                     VertexConsumerProvider vertexConsumers,
                                     int light,
                                     int overlay){
        if (stack.isOf(ModItems.BOOMBOX) && stack.getSubNbt("MusicUUID") != null){
            return this.getModels().getModelManager().getModel(BOOMBOX_ON);
        }
        return value;
    }
}
