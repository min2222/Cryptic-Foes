package com.min01.crypticfoes.entity.model;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.entity.animation.BrancherAnimation;
import com.min01.crypticfoes.entity.living.EntityBrancher;
import com.min01.crypticfoes.util.CrypticClientUtil;
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

public class ModelBrancher extends HierarchicalModel<EntityBrancher>
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(CrypticFoes.MODID, "brancher"), "main");
	private final ModelPart root;

	public ModelBrancher(ModelPart root)
	{
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition bone = root.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition body = bone.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -14.0F, 0.5F));

		body.addOrReplaceChild("body_main", CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.5F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, 0.5F));

		head.addOrReplaceChild("lefthair", CubeListBuilder.create().texOffs(32, 10).addBox(0.0F, -3.0F, 0.0F, 2.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -4.0F, -0.5F));

		head.addOrReplaceChild("righthair", CubeListBuilder.create().texOffs(36, 10).addBox(-2.0F, -3.0F, 0.0F, 2.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -4.0F, -0.5F));

		head.addOrReplaceChild("horns", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -2.0F, 0.0F, 8.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, -1.5F));

		PartDefinition rightfrontleg = bone.addOrReplaceChild("rightfrontleg", CubeListBuilder.create().texOffs(24, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 16.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -15.0F, -3.0F));

		rightfrontleg.addOrReplaceChild("rightfronttwigs", CubeListBuilder.create().texOffs(32, 2).addBox(-2.0F, -4.0F, 0.0F, 2.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 4.0F, 0.0F));

		PartDefinition leftfrontleg = bone.addOrReplaceChild("leftfrontleg", CubeListBuilder.create().texOffs(0, 31).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 16.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -15.0F, -3.0F));

		leftfrontleg.addOrReplaceChild("leftfronttwigs", CubeListBuilder.create().texOffs(36, 2).addBox(0.0F, -4.0F, 0.0F, 2.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 4.0F, 0.0F));

		PartDefinition rightbackleg = bone.addOrReplaceChild("rightbackleg", CubeListBuilder.create().texOffs(16, 36).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 16.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -15.0F, 4.0F, 0.0F, 3.1416F, 0.0F));

		rightbackleg.addOrReplaceChild("rightbacktwigs", CubeListBuilder.create().texOffs(40, 2).mirror().addBox(0.0F, -4.0F, 0.0F, 2.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 4.0F, 0.0F));

		PartDefinition leftbackleg = bone.addOrReplaceChild("leftbackleg", CubeListBuilder.create().texOffs(32, 36).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 16.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.0F, -15.0F, 4.0F, 0.0F, -3.1416F, 0.0F));

		leftbackleg.addOrReplaceChild("leftbacktwigs", CubeListBuilder.create().texOffs(40, 10).mirror().addBox(-2.0F, -4.0F, 0.0F, 2.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 4.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(EntityBrancher entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		this.root().getAllParts().forEach(ModelPart::resetPose);
		entity.idleAnimationState.animate(this, BrancherAnimation.BRANCHER_IDLE, ageInTicks, limbSwingAmount);
		entity.shiverAnimationState.animate(this, BrancherAnimation.BRANCHER_SHIVER, ageInTicks);
		entity.explosionAnimationState.animate(this, BrancherAnimation.BRANCHER_EXPLOSION, ageInTicks);
		
		float factor = entity.runAnimationState.factor(CrypticClientUtil.MC.getFrameTime());
		
		this.animateWalk(BrancherAnimation.BRANCHER_WALK, limbSwing, limbSwingAmount * factor, 2.5F, 2.5F);
		this.animateWalk(BrancherAnimation.BRANCHER_RUN, limbSwing, Math.max(limbSwingAmount - factor, 0.0F), 1.5F, 2.5F);
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