package com.min01.crypticfoes.block.model;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.blockentity.CrypticSkullBlockEntity;
import com.min01.crypticfoes.entity.animation.HowlerAnimation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class ModelHowlerHead extends CrypticSkullModelBase
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "howler_head"), "main");
	private final ModelPart root;
	private final ModelPart head;

	public ModelHowlerHead(ModelPart root) 
	{
		this.root = root;
		this.head = root.getChild("head");
	}

	public static LayerDefinition createHeadModel() 
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(1, 2).addBox(-6.5F, -10.0F, -6.0F, 13.0F, 10.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(50, 47).addBox(-2.5F, -3.0F, 0.0F, 5.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, 1.0F, 0.0F, -0.7854F, 0.0F));

		head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(50, 47).addBox(-2.5F, -3.0F, 0.0F, 5.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, 1.0F, 0.0F, 0.7854F, 0.0F));

		head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(55, 50).addBox(-3.5F, -4.0F, 0.0F, 7.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, -6.5F));

		head.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(46, 76).addBox(0.0F, -3.0F, 0.0F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, -4.5F, -4.0F, 0.0F, 0.0F, 0.2182F));

		head.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(52, 76).addBox(-3.0F, -3.0F, 0.0F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.5F, -4.5F, -4.0F, 0.0F, 0.0F, -0.2182F));

		head.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(45, 63).addBox(-2.8672F, -10.0844F, 0.0F, 5.0F, 13.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.5F, -8.0F, -1.0F, 0.0F, 0.0F, -0.0436F));

		head.addOrReplaceChild("ear1", CubeListBuilder.create().texOffs(45, 50).addBox(-1.1309F, -9.9971F, -1.5F, 5.0F, 13.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, -8.0F, 0.5F, 0.0F, 0.0F, 0.0436F));

		PartDefinition eyesdown = head.addOrReplaceChild("eyesdown", CubeListBuilder.create(), PartPose.offset(0.0F, -4.1F, -6.0F));

		eyesdown.addOrReplaceChild("leftdowneye", CubeListBuilder.create().texOffs(39, 0).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 0.1F, -0.005F));

		eyesdown.addOrReplaceChild("rightdowneye", CubeListBuilder.create().texOffs(45, 0).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 0.1F, -0.005F));

		PartDefinition eyesup = head.addOrReplaceChild("eyesup", CubeListBuilder.create(), PartPose.offset(0.0F, -6.5F, -6.0F));

		eyesup.addOrReplaceChild("leftupeye", CubeListBuilder.create().texOffs(45, 2).addBox(-1.5F, -0.5F, 0.0F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, -0.005F));

		eyesup.addOrReplaceChild("rightupeye", CubeListBuilder.create().texOffs(39, 2).addBox(-1.5F, -0.5F, 0.0F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, -0.005F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(float pMouthAnimation, float pYRot, float pXRot) 
	{
		this.head.y = 1.0F / 16.0F;
		this.head.yRot = pYRot * ((float)Math.PI / 180.0F);
		this.head.xRot = pXRot * ((float)Math.PI / 180.0F);
	}
	
	@Override
	public void setupAnim(CrypticSkullBlockEntity blockEntity, float ageInTicks) 
	{
		this.root().getAllParts().forEach(ModelPart::resetPose);
		blockEntity.blinkAnimationState.animateSkullBlock(this, HowlerAnimation.HOWLER_BLINK, ageInTicks);
	}
	
	@Override
	public ModelPart root()
	{
		return this.root;
	}
	
	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) 
	{
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}