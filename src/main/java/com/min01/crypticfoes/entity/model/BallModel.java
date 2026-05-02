package com.min01.crypticfoes.entity.model;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.entity.projectile.BallEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class BallModel extends EntityModel<BallEntity> 
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "ball"), "main");
	private final ModelPart root;

	public BallModel(ModelPart root) 
	{
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 20).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 19.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(BallEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) 
	{
		this.root.zRot = (float) Math.toRadians(netHeadYaw);
		this.root.xRot = (float) Math.toRadians(headPitch);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}