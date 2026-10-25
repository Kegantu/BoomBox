package me.kegantu.boombox.client.entity.renderer;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.client.BoomBoxClient;
import me.kegantu.boombox.client.entity.model.BoomBoxEntityModel;
import me.kegantu.boombox.client.entity.renderstate.BoomBoxEntityRenderState;
import me.kegantu.boombox.entity.BoomBoxEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

public class BoomBoxEntityRenderer extends EntityRenderer<BoomBoxEntity, BoomBoxEntityRenderState> {

    private final BoomBoxEntityModel model;

    public BoomBoxEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new BoomBoxEntityModel(ctx.getPart(BoomBoxClient.BOOMBOX_ENTITY_MODEL_LAYER));
    }

    @Override
    public BoomBoxEntityRenderState createRenderState() {
        return new BoomBoxEntityRenderState();
    }

    @Override
    public void render(BoomBoxEntityRenderState renderState, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        super.render(renderState, matrices, queue, cameraState);
        matrices.push();
        matrices.scale(-1f,-1f,1f);
        matrices.translate(0f, -1.5f,0f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(renderState.tickDelta, renderState.entity.lastYaw, renderState.yaw)));
        this.model.setAngles(renderState);
        queue.submitModel(this.model, renderState, matrices, this.model.getLayer(getTexture(renderState.entity)),
                renderState.light, OverlayTexture.DEFAULT_UV, -1, null, renderState.outlineColor, null);
        //this.model.render(matrices, vertexConsumers.getBuffer(this.model.getLayer(this.getTexture(state.entity))), light, OverlayTexture.DEFAULT_UV);
        matrices.pop();
    }

    @Override
    public boolean shouldRender(BoomBoxEntity entity, Frustum frustum, double x, double y, double z) {
        return true;
    }

    @Override
    public void updateRenderState(BoomBoxEntity entity, BoomBoxEntityRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.entity = entity;
        state.tickDelta = tickDelta;
        state.yaw = entity.getLerpedYaw(tickDelta);
        state.pitch = entity.getLerpedPitch(tickDelta);
    }

    public Identifier getTexture(BoomBoxEntity entity) {
        return entity.isPlaying() ? Identifier.of("boombox","textures/entity/boombox_entity_on.png") : Identifier.of("boombox","textures/entity/boombox_entity.png");
    }
}
