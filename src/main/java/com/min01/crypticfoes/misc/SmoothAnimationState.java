package com.min01.crypticfoes.misc;

import java.util.List;

import org.joml.Vector3f;

import com.min01.crypticfoes.misc.AnimationEntries.WalkAnimationEntry;
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
	public final float lerpSpeed;
	private final boolean snapRotation;

	public SmoothAnimationState(float lerpSpeed, boolean snapRotation)
	{
		this.lerpSpeed = lerpSpeed;
		this.snapRotation = snapRotation;
	}
	
	public SmoothAnimationState(float lerpSpeed)
	{
		this(lerpSpeed, false);
	}
	
	public SmoothAnimationState() 
	{
		this(0.5F);
	}
	
	public void snapFactor()
	{
    	this.factorOld = 1.0F;
    	this.factor = 1.0F;
	}
	
	public void updateWhen(boolean updateWhen, int tickCount)
	{
    	float target = updateWhen ? 1.0F : 0.0F;
	    this.factorOld = this.factor;
	    this.factor += (target - this.factor) * this.lerpSpeed;
	    this.factor = Mth.clamp(this.factor, 0.0F, 1.0F);
	    this.animateWhen(updateWhen, tickCount);
	}

	@OnlyIn(Dist.CLIENT)
	public float factor()
	{
		return Mth.lerp(CrypticClientUtil.MC.getPartialTick(), this.factorOld, this.factor);
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
	public void animateIdle(HierarchicalModel<?> model, AnimationDefinition definition, float ageInTicks, float limbSwingAmount)
	{
		this.animateIdle(model, definition, ageInTicks, limbSwingAmount, List.of());
	}
	
	@OnlyIn(Dist.CLIENT)
	public void animateIdle(HierarchicalModel<?> model, AnimationDefinition definition, float ageInTicks, float limbSwingAmount, List<WalkAnimationEntry> states)
	{
		float factor = this.factor();
		for(WalkAnimationEntry walkState : states)
		{
			SmoothAnimationState state = walkState.state();
			float scale = Math.min(limbSwingAmount * walkState.scale(), 1.0F) * state.factor();
			factor *= 1.0F - scale;
		}
		this.animate(model, definition, ageInTicks, factor, 1.0F);
	}
	
	@OnlyIn(Dist.CLIENT)
	public void animateWalk(HierarchicalModel<?> model, AnimationDefinition pAnimationDefinition, float pLimbSwing, float pLimbSwingAmount, float pMaxAnimationSpeed, float pAnimationScaleFactor)
	{
		this.animateWalk(model, pAnimationDefinition, pLimbSwing, pLimbSwingAmount, pMaxAnimationSpeed, pAnimationScaleFactor, List.of());
	}

	@OnlyIn(Dist.CLIENT)
	public void animateWalk(HierarchicalModel<?> model, AnimationDefinition pAnimationDefinition, float pLimbSwing, float pLimbSwingAmount, float pMaxAnimationSpeed, float pAnimationScaleFactor, List<SmoothAnimationState> states)
	{
		float factor = this.factor();
		for(SmoothAnimationState state : states)
		{
			factor *= 1.0F - state.factor();
		}
		long i = (long)(pLimbSwing * 50.0F * pMaxAnimationSpeed);
		float f = Math.min(pLimbSwingAmount * pAnimationScaleFactor, 1.0F) * factor;
		KeyframeAnimations.animate(model, pAnimationDefinition, i, f, ANIMATION_VECTOR_CACHE);
	}
	
	@OnlyIn(Dist.CLIENT)
	public void animate(HierarchicalModel<?> model, AnimationDefinition definition, float ageInTicks, float factor, float speed) 
	{
		this.updateTime(ageInTicks, speed);
		boolean snap = this.snapRotation && this.factor < this.factorOld - 1.0e-4F;
		CrypticKeyframeAnimations.animate(model, definition, this.getAccumulatedTime(), factor, factor, snap, ANIMATION_VECTOR_CACHE);
	}
}