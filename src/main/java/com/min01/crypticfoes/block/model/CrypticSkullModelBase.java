package com.min01.crypticfoes.block.model;

import java.util.Optional;

import org.joml.Vector3f;

import com.min01.crypticfoes.block.animation.KeyframeBlockAnimations;
import com.min01.crypticfoes.blockentity.CrypticSkullBlockEntity;

import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.AnimationState;

public abstract class CrypticSkullModelBase extends SkullModelBase
{
	private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();
	
	public abstract void setupAnim(CrypticSkullBlockEntity blockEntity, float ageInTicks);

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

	protected void animate(AnimationState pAnimationState, AnimationDefinition pAnimationDefinition, float pAgeInTicks)
	{
		this.animate(pAnimationState, pAnimationDefinition, pAgeInTicks, 1.0F);
	}

	protected void animate(AnimationState pAnimationState, AnimationDefinition pAnimationDefinition, float pAgeInTicks, float pSpeed) 
	{
		pAnimationState.updateTime(pAgeInTicks, pSpeed);
		pAnimationState.ifStarted(t -> 
		{
			KeyframeBlockAnimations.animate(this, pAnimationDefinition, t.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE);
		});
	}
}
