package com.min01.crypticfoes.entity.model;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.entity.animation.BumpyAnimation;
import com.min01.crypticfoes.entity.living.EntityBumpy;
import com.min01.crypticfoes.misc.SmoothAnimationState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class ModelBumpy extends HierarchicalModel<EntityBumpy>
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "bumpy"), "main");
	private final ModelPart root;

	public ModelBumpy(ModelPart root)
	{
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition bumpy = root.addOrReplaceChild("bumpy", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0F, 0.0F));

		PartDefinition headPivot = bumpy.addOrReplaceChild("headPivot", CubeListBuilder.create(), PartPose.offset(0.0F, -8.0F, 0.0F));

		PartDefinition head = headPivot.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, 0.0F));

		head.addOrReplaceChild("shell", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 32).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		head.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(48, 0).addBox(-1.0F, 0.0F, -7.5F, 2.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.1F, -2.5F));

		head.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(48, 0).addBox(-1.0F, 0.0F, -7.5F, 2.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.1F, -2.5F));

		PartDefinition leftLegBase = bumpy.addOrReplaceChild("leftLegBase", CubeListBuilder.create(), PartPose.offset(4.0F, 5.0F, 3.5F));

		leftLegBase.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -0.25F, -1.5F, 2.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.75F, 0.0F));

		PartDefinition rightLegBase = bumpy.addOrReplaceChild("rightLegBase", CubeListBuilder.create(), PartPose.offset(-4.0F, 5.0F, 3.5F));

		rightLegBase.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -0.25F, -1.5F, 2.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.75F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(EntityBumpy entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) 
	{
		this.root().getAllParts().forEach(ModelPart::resetPose);
		entity.idleAnimationState.animateIdle(this, BumpyAnimation.IDLE, ageInTicks, limbSwingAmount, 2.5F);
		entity.blockingAnimationState.animate(this, BumpyAnimation.BLOCKING, ageInTicks);
		entity.bumpAnimationState.animate(this, BumpyAnimation.BUMP, ageInTicks);
		SmoothAnimationState.animateWalk(this, BumpyAnimation.WALK, limbSwing, limbSwingAmount, 2.5F, 2.5F);
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