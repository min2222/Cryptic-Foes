package com.min01.crypticfoes.api.animation;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.geom.ModelPart;

public interface IHierarchicalModel<T>
{
	ModelPart root();
	
	default void setupAnim(T object, float pAgeInTicks)
	{
		this.setupAnim(object, 0, 0, pAgeInTicks, 0, 0);
	}
	
	default void setupAnim(T object, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch)
	{
		
	}
	
	default void animate(LerpingAnimationState state, AnimationDefinition definition, float ageInTicks) 
	{
		state.updateTime(ageInTicks, 1.0F);
		for(Map.Entry<String, List<AnimationChannel>> entry : definition.boneAnimations().entrySet())
		{
			Optional<ModelPart> optional = this.getAnyDescendantWithName(entry.getKey());
			SnappingKeyframeAnimations.animate(entry, optional, definition, state.getAccumulatedTime(), state.factor(), false, LerpingAnimationState.ANIMATION_VECTOR_CACHE);
		}
	}
	
	default Optional<ModelPart> getAnyDescendantWithName(String pName) 
	{
		return this.root().getAllParts().filter(t -> 
		{
			return t.hasChild(pName);
		}).findFirst().map(t ->
		{
			return t.getChild(pName);
		});
	}
}
