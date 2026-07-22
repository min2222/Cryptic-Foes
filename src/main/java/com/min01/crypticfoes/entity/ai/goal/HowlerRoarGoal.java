package com.min01.crypticfoes.entity.ai.goal;

import com.min01.crypticfoes.entity.CrypticEntities;
import com.min01.crypticfoes.entity.living.HowlerEntity;
import com.min01.crypticfoes.entity.projectile.HowlerScreamEntity;
import com.min01.crypticfoes.sound.CrypticSounds;

public class HowlerRoarGoal extends AbstractAnimationGoal<HowlerEntity>
{
	public HowlerRoarGoal(HowlerEntity mob)
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
		if(this.mob.getAnimationTick() <= this.getDuration() - this.getDelay() && this.mob.getAnimationTick() >= this.getDuration() - 70) 
    	{
			HowlerScreamEntity scream = new HowlerScreamEntity(CrypticEntities.HOWLER_SCREAM.get(), this.mob.level);
			scream.setOwner(this.mob);
			scream.setPos(this.mob.modelPositions.getModelPos("head"));
			scream.shootFromRotation(this.mob, this.mob.getXRot(), this.mob.getYHeadRot(), 0.0F, 0.8F, 1.0F);
			scream.setNoGravity(true);
			this.mob.level.addFreshEntity(scream);
    	}
	}
	
	@Override
	public void run() 
	{
		this.mob.playSound(CrypticSounds.HOWLER_SCREAM.get(), 4.0F, 1.0F);
	}
	
	@Override
	public void stop() 
	{
		super.stop();
		this.mob.setAnimationState(0);
	}

	@Override
	public int getDuration()
	{
		return 93;
	}
	
	@Override
	public int getDelay() 
	{
		return 45;
	}

	@Override
	public int getInterval() 
	{
		return 267;
	}
}