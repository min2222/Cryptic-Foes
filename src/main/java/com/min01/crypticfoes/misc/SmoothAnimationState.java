package com.min01.crypticfoes.misc;

import org.joml.Vector3f;

import com.min01.crypticfoes.block.animation.KeyframeBlockAnimations;
import com.min01.crypticfoes.block.model.CrypticSkullModelBase;
import com.min01.crypticfoes.block.model.HierarchicalBlockModel;
import com.min01.crypticfoes.util.CrypticClientUtil;

import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SmoothAnimationState extends AnimationState
{
	public static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();
	
	public float factorOld;
	public float factor;
	
	@OnlyIn(Dist.CLIENT)
	public void updateWhen(boolean updateWhen, int tickCount)
	{
	    this.factorOld = this.factor;
	    this.factor = Mth.lerp(CrypticClientUtil.MC.getPartialTick(), this.factor, updateWhen ? 1.0F : 0.0F);
	    this.factor = Mth.clamp(this.factor, 0.0F, 1.0F);
	    this.animateWhen(this.factor > 0.0F, tickCount);
	}

	@OnlyIn(Dist.CLIENT)
	public float factor()
	{
		return Mth.lerp(CrypticClientUtil.MC.getPartialTick(), this.factorOld, this.factor);
	}
	
	@OnlyIn(Dist.CLIENT)
	public void animateBlock(HierarchicalBlockModel<?> model, AnimationDefinition definition, float ageInTicks) 
	{
		this.updateTime(ageInTicks, 1.0F);
		this.ifStarted(t -> 
		{
			KeyframeBlockAnimations.animate(model, definition, t.getAccumulatedTime(), this.factor(), ANIMATION_VECTOR_CACHE);
		});
	}
	
	@OnlyIn(Dist.CLIENT)
	public void animateSkullBlock(CrypticSkullModelBase model, AnimationDefinition definition, float ageInTicks) 
	{
		this.updateTime(ageInTicks, 1.0F);
		this.ifStarted(t -> 
		{
			KeyframeBlockAnimations.animate(model, definition, t.getAccumulatedTime(), this.factor(), ANIMATION_VECTOR_CACHE);
		});
	}
	
	@OnlyIn(Dist.CLIENT)
	public void animate(HierarchicalModel<?> model, AnimationDefinition definition, float ageInTicks) 
	{
		this.animate(model, definition, ageInTicks, this.factor(), 1.0F);
	}
	
	@OnlyIn(Dist.CLIENT)
	public void animateWithSpeed(HierarchicalModel<?> model, AnimationDefinition definition, float ageInTicks, float speed) 
	{
		this.animate(model, definition, ageInTicks, this.factor(), speed);
	}
	
	@OnlyIn(Dist.CLIENT)
	public void animateIdle(HierarchicalModel<?> model, AnimationDefinition definition, float ageInTicks, float limbSwingAmount, float animationScaleFactor, SmoothAnimationState... states) 
	{
		float totalFactor = 1.0F;
		for(SmoothAnimationState state : states)
		{
			float factor = state.factor();
			totalFactor *= 1.0F - factor;
		}
		float scale = totalFactor * animationScaleFactor;
		float limb = Math.min(limbSwingAmount * scale, 1.0F);
		this.animate(model, definition, ageInTicks, Math.max(this.factor() * (1.0F - limb), 0.01F), 1.0F);
	}
	
	@OnlyIn(Dist.CLIENT)
	public static void animateWalk(HierarchicalModel<?> model, AnimationDefinition definition, float ageInTicks, float limbSwing, float limbSwingAmount, float maxAnimationSpeed, float animationScaleFactor, SmoothAnimationState... states)
	{
		float totalFactor = 1.0F;
		for(SmoothAnimationState state : states)
		{
			float factor = state.factor();
			totalFactor *= 1.0F - factor;
		}
		animateWalk(model, definition, limbSwing, limbSwingAmount, maxAnimationSpeed, totalFactor * animationScaleFactor);
	}

	@OnlyIn(Dist.CLIENT)
	public void animateWalk(HierarchicalModel<?> model, AnimationDefinition definition, float ageInTicks, float limbSwing, float limbSwingAmount, float maxAnimationSpeed, float animationScaleFactor)
	{
		animateWalk(model, definition, limbSwing, limbSwingAmount, maxAnimationSpeed, this.factor() * animationScaleFactor);
	}
	
	@OnlyIn(Dist.CLIENT)
	public static void animateWalk(HierarchicalModel<?> model, AnimationDefinition definition, float limbSwing, float limbSwingAmount, float maxAnimationSpeed, float animationScaleFactor)
	{
		long i = (long)(limbSwing * 50.0F * maxAnimationSpeed);
		float f = Math.min(limbSwingAmount * animationScaleFactor, 1.0F);
		KeyframeAnimations.animate(model, definition, i, f, ANIMATION_VECTOR_CACHE);
	}
	
	@OnlyIn(Dist.CLIENT)
	public void animate(HierarchicalModel<?> model, AnimationDefinition definition, float ageInTicks, float factor, float speed) 
	{
		this.updateTime(ageInTicks, speed);
		this.ifStarted(t -> 
		{
			KeyframeAnimations.animate(model, definition, t.getAccumulatedTime(), factor, ANIMATION_VECTOR_CACHE);
		});
	}
}