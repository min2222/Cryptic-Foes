package com.min01.crypticfoes.entity.ai.goal;

import com.min01.crypticfoes.entity.living.HowlerEntity;
import com.min01.crypticfoes.sound.CrypticSounds;

public class HowlerPunchGoal extends AbstractAnimationGoal<HowlerEntity>
{
	public HowlerPunchGoal(HowlerEntity mob)
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
		return super.canUse() && this.mob.isWithinMeleeAttackRange(this.mob.getTarget()) && !this.mob.isHowlerSleeping() && !this.mob.isFalling();
	}
	
	@Override
	public void run() 
	{
		if(this.mob.getTarget() != null)
		{
			if(this.mob.isWithinMeleeAttackRange(this.mob.getTarget()))
			{
				this.mob.doHurtTarget(this.mob.getTarget());
			}
		}
	}

	@Override
	public int getDuration()
	{
		return 50;
	}
	
	@Override
	public int getDelay() 
	{
		return 24;
	}

	@Override
	public int getInterval() 
	{
		return 10;
	}
}
