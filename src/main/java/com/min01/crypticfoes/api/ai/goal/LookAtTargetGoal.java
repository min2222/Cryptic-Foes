package com.min01.crypticfoes.api.ai.goal;

import com.min01.crypticfoes.api.entity.IAnimatable;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class LookAtTargetGoal<T extends Mob & IAnimatable> extends Goal
{
	protected final T mob;
	
	public LookAtTargetGoal(T mob) 
	{
		this.mob = mob;
	}
	
	@Override
	public boolean canUse() 
	{
		return this.mob.canLook() && this.mob.getTarget() != null && this.mob.getTarget().isAlive();
	}
	
	@Override
	public void tick() 
	{
		if(this.mob.getTarget() != null)
		{
			this.mob.lookAtTarget();
		}
	}
	
	@Override
	public boolean requiresUpdateEveryTick() 
	{
		return true;
	}
}
