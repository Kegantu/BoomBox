package me.kegantu.boombox.client;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.client.entity.model.BoomBoxEntityModel;
import me.kegantu.boombox.client.entity.renderer.BoomBoxEntityRenderer;
import me.kegantu.boombox.init.ModEntities;
import me.kegantu.boombox.init.ModPackets;
import me.kegantu.boombox.init.ModParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class BoomBoxClient implements ClientModInitializer {

    public static final EntityModelLayer BOOMBOX_ENTITY_MODEL_LAYER = new EntityModelLayer(Identifier.of(BoomBox.MOD_ID, "boombox_entity"), "main");

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.BOOMBOX_ENTITY, BoomBoxEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(BOOMBOX_ENTITY_MODEL_LAYER, BoomBoxEntityModel::getTexturedModelData);

        ModParticles.registerClient();
    }
}
