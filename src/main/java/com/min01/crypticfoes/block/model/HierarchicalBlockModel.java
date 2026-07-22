package com.min01.crypticfoes.block.model;

import java.util.Optional;

import com.min01.crypticfoes.block.animation.KeyframeBlockAnimations;
import com.min01.crypticfoes.misc.SmoothAnimationState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class HierarchicalBlockModel<T extends BlockEntity> extends Model
{
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

	public void animate(SmoothAnimationState state, AnimationDefinition definition, float ageInTicks) 
	{
		state.updateTime(ageInTicks, 1.0F);
		KeyframeBlockAnimations.animate(this, definition, state.getAccumulatedTime(), state.factor(), SmoothAnimationState.ANIMATION_VECTOR_CACHE);
	}
}
