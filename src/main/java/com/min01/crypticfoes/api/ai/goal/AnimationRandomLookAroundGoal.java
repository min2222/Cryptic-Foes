package com.min01.crypticfoes.api.ai.goal;

import com.min01.crypticfoes.api.entity.IAnimatable;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;

public class AnimationRandomLookAroundGoal<T extends Mob & IAnimatable> extends RandomLookAroundGoal
{
	protected final T mob;
	
	public AnimationRandomLookAroundGoal(T pMob) 
	{
		super(pMob);
		this.mob = pMob;
	}
	
	@Override
	public boolean canUse() 
	{
		return super.canUse() && this.mob.canLookAround();
	}
}
