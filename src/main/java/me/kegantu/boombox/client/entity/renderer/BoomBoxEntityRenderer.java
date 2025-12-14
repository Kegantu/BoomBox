package me.kegantu.boombox.client.entity.renderer;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.client.BoomBoxClient;
import me.kegantu.boombox.client.entity.model.BoomBoxEntityModel;
import me.kegantu.boombox.entity.BoomBoxEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

public class BoomBoxEntityRenderer extends EntityRenderer<BoomBoxEntity> {

    private final BoomBoxEntityModel model;

    public BoomBoxEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new BoomBoxEntityModel(ctx.getPart(BoomBoxClient.BOOMBOX_ENTITY_MODEL_LAYER));
    }

    @Override
    public void render(BoomBoxEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        matrices.push();
        matrices.scale(-1f,-1f,1f);
        matrices.translate(0f, -1.5f,0f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw())));
        this.model.setAngles(entity, 0f, 0f, 0f, 0, 0);
        this.model.render(matrices, vertexConsumers.getBuffer(this.model.getLayer(this.getTexture(entity))), light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        matrices.pop();
    }

    @Override
    public Identifier getTexture(BoomBoxEntity entity) {
        return entity.isPlaying() ? Identifier.of("boombox","textures/entity/boombox_entity_on.png") : Identifier.of("boombox","textures/entity/boombox_entity.png");
    }
}
