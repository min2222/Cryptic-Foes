package com.min01.crypticfoes.block.model;

import java.util.Optional;

import com.min01.crypticfoes.block.animation.KeyframeBlockAnimations;
import com.min01.crypticfoes.blockentity.CrypticSkullBlockEntity;
import com.min01.crypticfoes.misc.SmoothAnimationState;

import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.ModelPart;

public abstract class CrypticSkullModelBase extends SkullModelBase
{
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

	public void animate(SmoothAnimationState state, AnimationDefinition definition, float ageInTicks) 
	{
		state.updateTime(ageInTicks, 1.0F);
		KeyframeBlockAnimations.animate(this, definition, state.getAccumulatedTime(), state.factor(), SmoothAnimationState.ANIMATION_VECTOR_CACHE);
	}
}
