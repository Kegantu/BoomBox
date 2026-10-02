package me.kegantu.boombox.client.entity.renderstate;

import me.kegantu.boombox.entity.BoomBoxEntity;
import net.minecraft.client.render.entity.state.EntityRenderState;

public class BoomBoxEntityRenderState extends EntityRenderState {
    public float pitch;
    public float yaw;
    public BoomBoxEntity entity;
    public float tickDelta;
}
