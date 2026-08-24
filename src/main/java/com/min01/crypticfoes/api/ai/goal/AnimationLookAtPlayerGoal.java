package com.min01.crypticfoes.api.ai.goal;

import com.min01.crypticfoes.api.entity.IAnimatable;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;

public class AnimationLookAtPlayerGoal<T extends Mob & IAnimatable> extends LookAtPlayerGoal
{
	protected final T mob;
	
	public AnimationLookAtPlayerGoal(T pMob, Class<? extends LivingEntity> pLookAtType, float pLookDistance) 
	{
		super(pMob, pLookAtType, pLookDistance);
		this.mob = pMob;
	}
	
	public AnimationLookAtPlayerGoal(T pMob, Class<? extends LivingEntity> pLookAtType, float pLookDistance, float pProbability)
	{
		super(pMob, pLookAtType, pLookDistance, pProbability);
		this.mob = pMob;
	}
	
	@Override
	public boolean canUse() 
	{
		return super.canUse() && this.mob.canLookAround();
	}
}
