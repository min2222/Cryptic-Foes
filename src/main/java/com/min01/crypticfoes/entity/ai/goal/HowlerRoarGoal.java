package com.min01.crypticfoes.entity.ai.goal;

import com.min01.crypticfoes.entity.CrypticEntities;
import com.min01.crypticfoes.entity.living.EntityHowler;
import com.min01.crypticfoes.entity.projectile.EntityHowlerScream;
import com.min01.crypticfoes.sound.CrypticSounds;

public class HowlerRoarGoal extends BasicAnimationSkillGoal<EntityHowler>
{
	public HowlerRoarGoal(EntityHowler mob)
	{
		super(mob);
	}
	
	@Override
	public void start() 
	{
		super.start();
		this.mob.setAnimationState(4);
	}
	
	@Override
	public boolean canUse() 
	{
		return super.canUse() && !this.mob.isHowlerSleeping() && !this.mob.isFalling();
	}
	
	@Override
	public void tick() 
	{
		super.tick();
		if(this.mob.getAnimationTick() <= this.getSkillUsingTime() - this.getSkillWarmupTime() && this.mob.getAnimationTick() >= this.getSkillUsingTime() - 70) 
    	{
			if(this.mob.posArray[0] != null)
			{
				EntityHowlerScream scream = new EntityHowlerScream(CrypticEntities.HOWLER_SCREAM.get(), this.mob.level);
				scream.setOwner(this.mob);
				scream.setPos(this.mob.posArray[0]);
				scream.shootFromRotation(this.mob, this.mob.getXRot(), this.mob.getYHeadRot(), 0.0F, 0.8F, 1.0F);
				scream.setNoGravity(true);
				this.mob.level.addFreshEntity(scream);
			}
    	}
	}
	
	@Override
	public void performSkill() 
	{
		this.mob.playSound(CrypticSounds.HOWLER_SCREAM.get(), 0.75F, 1.0F);
	}
	
	@Override
	public void stop() 
	{
		super.stop();
		this.mob.setAnimationState(0);
	}

	@Override
	public int getSkillUsingTime()
	{
		return 93;
	}

	@Override
	public int getSkillUsingInterval() 
	{
		return 360;
	}
	
	@Override
	public int getSkillWarmupTime() 
	{
		return 45;
	}
}