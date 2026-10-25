// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

package me.kegantu.boombox.client.entity.model;

import me.kegantu.boombox.client.entity.renderstate.BoomBoxEntityRenderState;
import me.kegantu.boombox.entity.BoomBoxEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class BoomBoxEntityModel extends EntityModel<BoomBoxEntityRenderState> {
	private final ModelPart root;
	public BoomBoxEntityModel(ModelPart root) {
		super(root);
		this.root = root.getChild("root");
	}
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition root = modelPartData.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -5.0F, -1.0F, 12.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 10).addBox(2.0F, -4.0F, -1.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(8, 10).addBox(-5.0F, -4.0F, -1.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(-4.0F, -7.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 8).addBox(-3.0F, -7.0F, 0.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(4, 14).addBox(3.0F, -7.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		return LayerDefinition.create(modelData, 32, 32);
	}
}