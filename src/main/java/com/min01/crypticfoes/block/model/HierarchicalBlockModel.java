package com.min01.crypticfoes.block.model;

import java.util.Optional;

import org.joml.Vector3f;

import com.min01.crypticfoes.block.animation.KeyframeBlockAnimations;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class HierarchicalBlockModel<T extends BlockEntity> extends Model
{
	private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();
	
	public HierarchicalBlockModel()
	{
		super(RenderType::entityCutoutNoCull);
	}

	public abstract void setupAnim(T blockEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch);

	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha)
	{
		this.root().render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
	}

	public abstract ModelPart root();

	public Optional<ModelPart> getAnyDescendantWithName(String pName) 
	{
		return this.root().getAllParts().filter(t -> 
		{
			return t.hasChild(pName);
		}).findFirst().map(t ->
		{
			return t.getChild(pName);
		});
	}

	public void animate(AnimationState pAnimationState, AnimationDefinition pAnimationDefinition, float pAgeInTicks)
	{
		this.animate(pAnimationState, pAnimationDefinition, pAgeInTicks, 1.0F);
	}

	public void animate(AnimationState pAnimationState, AnimationDefinition pAnimationDefinition, float pAgeInTicks, float pSpeed) 
	{
		pAnimationState.updateTime(pAgeInTicks, pSpeed);
		pAnimationState.ifStarted(t -> 
		{
			KeyframeBlockAnimations.animate(this, pAnimationDefinition, t.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE);
		});
	}
}
