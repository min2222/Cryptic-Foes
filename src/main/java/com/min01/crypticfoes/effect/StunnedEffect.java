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
	public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) 
	{
		if(pLivingEntity.getType().is(CrypticTags.CrypticEntity.RESIST_TO_STUN) || pLivingEntity.isSpectator())
		{
			return;
		}
        if(pLivingEntity instanceof Mob mob && !mob.level.isClientSide)
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
        pLivingEntity.stopUsingItem();
        pLivingEntity.xxa = 0.0F;
        pLivingEntity.yya = 0.0F;
        pLivingEntity.zza = 0.0F;
		if(pLivingEntity.tickCount % 5 == 0)
		{
			pLivingEntity.level.addParticle(CrypticParticles.STUNNED.get(), pLivingEntity.getRandomX(1.0F), pLivingEntity.getEyeY(), pLivingEntity.getRandomZ(1.0F), 0.0F, 0.0F, 0.0F);
		}
	}
	
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) 
	{
		return duration > 0;
	}
}
