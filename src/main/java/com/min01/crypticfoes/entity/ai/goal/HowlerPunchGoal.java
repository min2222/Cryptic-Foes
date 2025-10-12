package com.min01.crypticfoes.entity.ai.goal;

import com.min01.crypticfoes.entity.living.EntityHowler;
import com.min01.crypticfoes.util.CrypticUtil;

public class HowlerPunchGoal extends BasicAnimationSkillGoal<EntityHowler>
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
	}
	
	@Override
	public boolean canUse() 
	{
		return super.canUse() && CrypticUtil.isWithinMeleeAttackRange(this.mob, this.mob.getTarget(), 5.0F) && !this.mob.isHowlerSleeping() && !this.mob.isFalling();
	}
	
	@Override
	protected void performSkill() 
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
	public void stop() 
	{
		super.stop();
		this.mob.setAnimationState(0);
	}

	@Override
	protected int getSkillUsingTime()
	{
		return 50;
	}

	@Override
	protected int getSkillUsingInterval() 
	{
		return 60;
	}
	
	@Override
	protected int getSkillWarmupTime() 
	{
		return 24;
	}
}
