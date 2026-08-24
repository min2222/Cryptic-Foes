package com.min01.crypticfoes.api.animation;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.joml.Vector3f;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class SnappingKeyframeAnimations
{
	public static void animate(HierarchicalModel<?> pModel, AnimationDefinition pAnimationDefinition, long pAccumulatedTime, float pScale, boolean snap, Vector3f pAnimationVecCache)
	{
		for(Map.Entry<String, List<AnimationChannel>> entry : pAnimationDefinition.boneAnimations().entrySet())
		{
			Optional<ModelPart> optional = pModel.getAnyDescendantWithName(entry.getKey());
			animate(entry, optional, pAnimationDefinition, pAccumulatedTime, pScale, snap, pAnimationVecCache);
		}
	}
	
	public static void animate(Map.Entry<String, List<AnimationChannel>> entry, Optional<ModelPart> optional, AnimationDefinition pAnimationDefinition, long pAccumulatedTime, float pScale, boolean snap, Vector3f pAnimationVecCache)
	{
		float elapsed = getElapsedSeconds(pAnimationDefinition, pAccumulatedTime);
		List<AnimationChannel> list = entry.getValue();
		optional.ifPresent(part ->
		{
			list.forEach(channel ->
			{
				Keyframe[] akeyframe = channel.keyframes();
				int i = Math.max(0, Mth.binarySearch(0, akeyframe.length, (index) ->
				{
					return elapsed <= akeyframe[index].timestamp();
				}) - 1);
				int j = Math.min(akeyframe.length - 1, i + 1);
				Keyframe keyframe = akeyframe[i];
				Keyframe keyframe1 = akeyframe[j];
				float f1 = elapsed - keyframe.timestamp();
				float f2 = 0.0F;
				if(j != i) 
				{
					f2 = Mth.clamp(f1 / (keyframe1.timestamp() - keyframe.timestamp()), 0.0F, 1.0F);
				}
				keyframe1.interpolation().apply(pAnimationVecCache, f2, akeyframe, i, j, snap ? snap(pAnimationDefinition, channel, pScale, elapsed) : pScale);
				channel.target().apply(part, pAnimationVecCache);
			});
		});
	}

	public static float getElapsedSeconds(AnimationDefinition definition, long accumulatedTime)
	{
		float seconds = accumulatedTime / 1000.0F;
		return definition.looping() ? seconds % definition.lengthInSeconds() : seconds;
	}
	
	private static float snap(AnimationDefinition definition, AnimationChannel channel, float pScale, float elapsedSeconds)
	{
		if(channel.target() == AnimationChannel.Targets.ROTATION && shouldSnap(channel))
		{
			return 0.0F;
		}
		return pScale;
	}
	
	private static boolean shouldSnap(AnimationChannel channel)
	{
		Keyframe[] keyframes = channel.keyframes();
		if(keyframes.length < 2)
		{
			return false;
		}
		Vector3f first = keyframes[0].target();
		Vector3f last = keyframes[keyframes.length - 1].target();
		float dx = Math.abs(last.x - first.x);
		float dy = Math.abs(last.y - first.y);
		float dz = Math.abs(last.z - first.z);
		float maxDelta = Math.max(dx, Math.max(dy, dz));
		return maxDelta >= (Math.PI * 2.0) - 0.18F;
	}
}
