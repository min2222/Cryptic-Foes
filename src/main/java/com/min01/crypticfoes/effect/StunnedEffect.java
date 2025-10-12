package com.min01.crypticfoes.effect;

import com.min01.crypticfoes.misc.CrypticTags;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WrappedGoal;

public class StunnedEffect extends MobEffect
{
	public StunnedEffect()
	{
		super(MobEffectCategory.HARMFUL, 16777215);
	}
	
	@Override
	public void applyEffectTick(LivingEntity p_19467_, int p_19468_) 
	{
		if(p_19467_.getType().is(CrypticTags.CrypticEntity.RESIST_TO_STUN))
		{
			return;
		}
		if(p_19467_ instanceof PathfinderMob mob)
		{
			mob.getNavigation().stop();
			mob.setTarget(null);
			for(WrappedGoal goal : mob.goalSelector.getAvailableGoals())
			{
				goal.stop();
			}
		}
		p_19467_.stopUsingItem();
		p_19467_.xxa = 0.0F;
		p_19467_.yya = 0.0F;
		p_19467_.zza = 0.0F;
		p_19467_.setXRot(p_19467_.xRotO);
		p_19467_.setYRot(p_19467_.yRotO);
		p_19467_.setYHeadRot(p_19467_.yHeadRotO);
		p_19467_.setYBodyRot(p_19467_.yBodyRotO);
	}
	
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) 
	{
		return duration > 0;
	}
}
