package com.min01.crypticfoes.entity.ai.goal;

import com.min01.crypticfoes.entity.living.EntityHowler;
import com.min01.crypticfoes.sound.CrypticSounds;
import com.min01.crypticfoes.util.CrypticUtil;

public class HowlerPunchGoal extends AbstractAnimationGoal<EntityHowler>
{
	public HowlerPunchGoal(EntityHowler mob)
	{
		super(mob);
	}
	
	@Override
	public void start() 
	{
		super.start();
		this.mob.setAnimationState(5);
		this.mob.playSound(CrypticSounds.HOWLER_PUNCH.get(), 1.2F, 1.0F);
	}
	
	@Override
	public boolean canUse() 
	{
		return super.canUse() && CrypticUtil.isWithinMeleeAttackRange(this.mob, this.mob.getTarget(), 5.0F) && !this.mob.isHowlerSleeping() && !this.mob.isFalling();
	}
	
	@Override
	public void performSkill() 
	{
		if(this.mob.getTarget() != null)
		{
			if(CrypticUtil.isWithinMeleeAttackRange(this.mob, this.mob.getTarget(), 5.0F))
			{
				this.mob.doHurtTarget(this.mob.getTarget());
			}
		}
	}

	@Override
	public int getSkillUsingTime()
	{
		return 50;
	}
	
	@Override
	public int getSkillWarmupTime() 
	{
		return 24;
	}

	@Override
	public int getSkillUsingInterval() 
	{
		return 10;
	}
}
