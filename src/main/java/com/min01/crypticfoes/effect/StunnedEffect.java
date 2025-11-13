package com.min01.crypticfoes.effect;

import com.min01.crypticfoes.misc.CrypticTags;
import com.min01.crypticfoes.particle.CrypticParticles;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
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
        if(p_19467_ instanceof Mob mob && !mob.level.isClientSide)
        {
			mob.getNavigation().stop();
			mob.setTarget(null);
			for(WrappedGoal goal : mob.goalSelector.getAvailableGoals())
			{
				goal.stop();
			}
            mob.goalSelector.setControlFlag(Goal.Flag.MOVE, false);
            mob.goalSelector.setControlFlag(Goal.Flag.JUMP, false);
            mob.goalSelector.setControlFlag(Goal.Flag.LOOK, false);
        }
		p_19467_.stopUsingItem();
		p_19467_.xxa = 0.0F;
		p_19467_.yya = 0.0F;
		p_19467_.zza = 0.0F;
		if(p_19467_.tickCount % 5 == 0)
		{
			p_19467_.level.addParticle(CrypticParticles.STUNNED.get(), p_19467_.getRandomX(1.0F), p_19467_.getEyeY(), p_19467_.getRandomZ(1.0F), 0.0F, 0.0F, 0.0F);
		}
	}
	
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) 
	{
		return duration > 0;
	}
}
