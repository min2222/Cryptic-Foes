package com.min01.crypticfoes.entity.ai.goal;

import com.min01.crypticfoes.entity.CrypticEntities;
import com.min01.crypticfoes.entity.living.EntityPetrified;
import com.min01.crypticfoes.entity.projectile.EntityPetrifiedStone;

public class PetrifiedShootStoneGoal extends BasicAnimationSkillGoal<EntityPetrified>
{
	public PetrifiedShootStoneGoal(EntityPetrified mob)
	{
		super(mob);
	}
	
	@Override
	public void start() 
	{
		super.start();
		this.mob.setAnimationState(1);
	}
	
	@Override
	public boolean canUse() 
	{
		return super.canUse() && this.mob.distanceTo(this.mob.getTarget()) <= 40.0F && this.mob.distanceTo(this.mob.getTarget()) >= 6.0F && this.mob.hasStone();
	}
	
	@Override
	public void performSkill() 
	{
		if(this.mob.posArray[0] != null && this.mob.getTarget() != null)
		{
			EntityPetrifiedStone stone = new EntityPetrifiedStone(CrypticEntities.PETRIFIED_STONE.get(), this.mob.level);
			stone.setPos(this.mob.posArray[0]);
			stone.setOwner(this.mob);
			stone.setDeltaMovement((this.mob.getTarget().getX() - stone.getX()) * 0.035D, 0.5D, (this.mob.getTarget().getZ() - stone.getZ()) * 0.035D);
			this.mob.level.addFreshEntity(stone);
			this.mob.setHasStone(false);
		}
	}
	
	@Override
	public void stop() 
	{
		super.stop();
		this.mob.setAnimationState(0);
		this.mob.setAnimationTick(100);
	}

	@Override
	public int getSkillUsingTime()
	{
		return 17;
	}

	@Override
	public int getSkillUsingInterval() 
	{
		return 135;
	}
	
	@Override
	public int getSkillWarmupTime() 
	{
		return 6;
	}
}
