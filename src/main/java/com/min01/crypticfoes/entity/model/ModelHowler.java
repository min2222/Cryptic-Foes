package com.min01.crypticfoes.entity.model;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.entity.animation.HowlerAnimation;
import com.min01.crypticfoes.entity.living.EntityHowler;
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

public class ModelHowler extends HierarchicalModel<EntityHowler>
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "howler"), "main");
	private final ModelPart root;
	private final ModelPart howlersleeppivot;
	private final ModelPart howler;
	private final ModelPart body;
	private final ModelPart body_no_hands;
	private final ModelPart head;

	public ModelHowler(ModelPart root) 
	{
		this.root = root.getChild("root");
		this.howlersleeppivot = this.root.getChild("howlersleeppivot");
		this.howler = this.howlersleeppivot.getChild("howler");
		this.body = this.howler.getChild("body");
		this.body_no_hands = this.body.getChild("body_no_hands");
		this.head = this.body_no_hands.getChild("head");
	}

	public static LayerDefinition createBodyLayer() 
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition howlersleeppivot = root.addOrReplaceChild("howlersleeppivot", CubeListBuilder.create(), PartPose.offset(0.0F, -50.0F, -7.0F));

		PartDefinition howler = howlersleeppivot.addOrReplaceChild("howler", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 7.0F));

		PartDefinition body = howler.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 5.0F, 0.0F));

		PartDefinition body_no_hands = body.addOrReplaceChild("body_no_hands", CubeListBuilder.create().texOffs(1, 25).addBox(-7.5F, -11.0F, -4.5F, 15.0F, 10.0F, 9.0F, new CubeDeformation(0.5F))
		.texOffs(0, 87).addBox(-7.5F, -11.0F, -4.5F, 15.0F, 16.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head = body_no_hands.addOrReplaceChild("head", CubeListBuilder.create().texOffs(1, 2).addBox(-6.5F, -10.0F, -6.5F, 13.0F, 10.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, 0.5F));

		head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(50, 47).addBox(-2.5F, -3.0F, 0.0F, 5.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, 0.5F, 0.0F, -0.7854F, 0.0F));

		head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(50, 47).addBox(-2.5F, -3.0F, 0.0F, 5.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, 0.5F, 0.0F, 0.7854F, 0.0F));

		head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(55, 50).addBox(-3.5F, -4.0F, 0.0F, 7.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, -7.0F));

		head.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(46, 76).addBox(0.0F, -3.0F, 0.0F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, -4.5F, -4.5F, 0.0F, 0.0F, 0.2182F));

		head.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(52, 76).addBox(-3.0F, -3.0F, 0.0F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.5F, -4.5F, -4.5F, 0.0F, 0.0F, -0.2182F));

		head.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(45, 63).addBox(-2.8672F, -10.0844F, 0.0F, 5.0F, 13.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.5F, -8.0F, -1.5F, 0.0F, 0.0F, -0.0436F));

		head.addOrReplaceChild("ear1", CubeListBuilder.create().texOffs(45, 50).addBox(-1.1309F, -9.9971F, -1.5F, 5.0F, 13.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, -8.0F, 0.0F, 0.0F, 0.0F, 0.0436F));

		PartDefinition eyesdown = head.addOrReplaceChild("eyesdown", CubeListBuilder.create(), PartPose.offset(0.0F, -4.1F, -6.5F));

		eyesdown.addOrReplaceChild("leftdowneye", CubeListBuilder.create().texOffs(39, 0).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 0.1F, -0.005F));

		eyesdown.addOrReplaceChild("rightdowneye", CubeListBuilder.create().texOffs(45, 0).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 0.1F, -0.005F));

		PartDefinition eyesup = head.addOrReplaceChild("eyesup", CubeListBuilder.create(), PartPose.offset(0.0F, -6.5F, -6.5F));

		eyesup.addOrReplaceChild("leftupeye", CubeListBuilder.create().texOffs(45, 2).addBox(-1.5F, -0.5F, 0.0F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, -0.005F));

		eyesup.addOrReplaceChild("rightupeye", CubeListBuilder.create().texOffs(39, 2).addBox(-1.5F, -0.5F, 0.0F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, -0.005F));

		PartDefinition arm1 = body.addOrReplaceChild("arm1", CubeListBuilder.create().texOffs(1, 51).addBox(-1.0F, -4.0F, -3.0F, 4.0F, 31.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(8.5F, -6.0F, 0.5F));

		arm1.addOrReplaceChild("wing1", CubeListBuilder.create().texOffs(50, 24).addBox(0.0F, -8.0F, 0.0F, 8.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 3.0F, -0.5F));

		PartDefinition arm2 = body.addOrReplaceChild("arm2", CubeListBuilder.create().texOffs(1, 51).mirror().addBox(-3.0F, -4.0F, -3.0F, 4.0F, 31.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-8.5F, -6.0F, 0.5F));

		arm2.addOrReplaceChild("wing2", CubeListBuilder.create().texOffs(50, 24).mirror().addBox(-8.0F, -8.0F, 0.0F, 8.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, 3.0F, -0.5F));

		PartDefinition legg1 = howler.addOrReplaceChild("legg1", CubeListBuilder.create(), PartPose.offset(4.0F, 26.0F, 0.5F));

		legg1.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(21, 51).addBox(-2.5F, -2.0F, -2.5F, 5.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -16.0F, 0.0F));

		PartDefinition legg2 = howler.addOrReplaceChild("legg2", CubeListBuilder.create(), PartPose.offset(-4.0F, 26.0F, 0.5F));

		legg2.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(21, 51).mirror().addBox(-2.5F, -2.0F, -2.5F, 5.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -16.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(EntityHowler entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) 
	{
		this.root().getAllParts().forEach(ModelPart::resetPose);
		
		if(!entity.isHowlerSleeping())
		{
			CrypticClientUtil.animateHead(this.head, netHeadYaw, headPitch);
		}

		this.animateWalk(HowlerAnimation.HOWLER_WALK, limbSwing, limbSwingAmount, 2.5F, 2.5F);
		entity.idleAnimationState.animate(this, HowlerAnimation.HOWLER_IDLE, ageInTicks, limbSwingAmount);
		entity.sleepAnimationState.animate(this, HowlerAnimation.HOWLER_SLEEP, ageInTicks);
		entity.awakeAnimationState.animate(this, HowlerAnimation.HOWLER_AWAKE, ageInTicks);
		entity.fallAnimationState.animate(this, HowlerAnimation.HOWLER_FALL, ageInTicks);
		entity.landAnimationState.animate(this, HowlerAnimation.HOWLER_LAND, ageInTicks);
		entity.roarAnimationState.animate(this, HowlerAnimation.HOWLER_ROAR, ageInTicks);
		this.animate(entity.blinkAnimationState, HowlerAnimation.HOWLER_BLINK, ageInTicks);
		entity.punchAnimationState.animate(this, HowlerAnimation.HOWLER_PUNCH, ageInTicks);
		entity.flyAnimationState.animate(this, HowlerAnimation.HOWLER_FLY, ageInTicks);
		entity.flyStartAnimationState.animate(this, HowlerAnimation.HOWLER_FLY_START, ageInTicks);
		entity.flyEndAnimationState.animate(this, HowlerAnimation.HOWLER_FLY_END, ageInTicks);
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