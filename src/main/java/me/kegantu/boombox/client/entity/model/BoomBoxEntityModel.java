// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

package me.kegantu.boombox.client.entity.model;

import me.kegantu.boombox.entity.BoomBoxEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class BoomBoxEntityModel extends EntityModel<BoomBoxEntity> {
	private final ModelPart root;
	public BoomBoxEntityModel(ModelPart root) {
		this.root = root.getChild("root");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create().uv(0, 0).cuboid(-6.0F, -5.0F, -1.0F, 12.0F, 5.0F, 3.0F, new Dilation(0.0F))
		.uv(0, 10).cuboid(2.0F, -4.0F, -1.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(8, 10).cuboid(-5.0F, -4.0F, -1.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(0, 14).cuboid(-4.0F, -7.0F, 0.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(0, 8).cuboid(-3.0F, -7.0F, 0.0F, 6.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(4, 14).cuboid(3.0F, -7.0F, 0.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}
	@Override
	public void setAngles(BoomBoxEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		root.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}