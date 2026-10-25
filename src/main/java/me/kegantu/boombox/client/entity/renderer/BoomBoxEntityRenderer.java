package me.kegantu.boombox.client.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.client.BoomBoxClient;
import me.kegantu.boombox.client.entity.model.BoomBoxEntityModel;
import me.kegantu.boombox.client.entity.renderstate.BoomBoxEntityRenderState;
import me.kegantu.boombox.entity.BoomBoxEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;

public class BoomBoxEntityRenderer extends EntityRenderer<BoomBoxEntity, BoomBoxEntityRenderState> {

    private final BoomBoxEntityModel model;

    public BoomBoxEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new BoomBoxEntityModel(ctx.bakeLayer(BoomBoxClient.BOOMBOX_ENTITY_MODEL_LAYER));
    }

    @Override
    public BoomBoxEntityRenderState createRenderState() {
        return new BoomBoxEntityRenderState();
    }

    @Override
    public void submit(BoomBoxEntityRenderState renderState, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        super.submit(renderState, matrices, queue, cameraState);
        matrices.pushPose();
        matrices.scale(-1f,-1f,1f);
        matrices.translate(0f, -1.5f,0f);
        matrices.mulPose(Axis.YP.rotationDegrees(Mth.lerp(renderState.tickDelta, renderState.entity.yRotO, renderState.yaw)));
        this.model.setupAnim(renderState);
        queue.submitModel(this.model, renderState, matrices, this.model.renderType(getTexture(renderState.entity)),
                renderState.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, renderState.outlineColor, null);
        //this.model.render(matrices, vertexConsumers.getBuffer(this.model.getLayer(this.getTexture(state.entity))), light, OverlayTexture.DEFAULT_UV);
        matrices.popPose();
    }

    @Override
    public boolean shouldRender(BoomBoxEntity entity, Frustum frustum, double x, double y, double z) {
        return true;
    }

    @Override
    public void extractRenderState(BoomBoxEntity entity, BoomBoxEntityRenderState state, float tickDelta) {
        super.extractRenderState(entity, state, tickDelta);
        state.entity = entity;
        state.tickDelta = tickDelta;
        state.yaw = entity.getYRot(tickDelta);
        state.pitch = entity.getXRot(tickDelta);
    }

    public Identifier getTexture(BoomBoxEntity entity) {
        return entity.isPlaying() ? Identifier.fromNamespaceAndPath("boombox","textures/entity/boombox_entity_on.png") : Identifier.fromNamespaceAndPath("boombox","textures/entity/boombox_entity.png");
    }
}
