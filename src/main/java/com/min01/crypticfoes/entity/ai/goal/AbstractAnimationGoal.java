package com.min01.crypticfoes.entity.ai.goal;

import com.min01.crypticfoes.entity.IAnimatable;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public abstract class AbstractAnimationGoal<T extends Mob & IAnimatable> extends Goal
{
	public int delay;
	public int cooldown;
	public final T mob;
	
	public AbstractAnimationGoal(T mob) 
	{
		this.mob = mob;
	}
	
    @Override
    public boolean canUse() 
    {
    	LivingEntity target = this.mob.getTarget();
    	if(target != null && target.isAlive()) 
    	{
    		if(this.mob.isAnimationPlaying())
    		{
    			return false;
    		}
			return this.mob.tickCount >= this.cooldown;
    	}
		return false;
    }
    
    @Override
    public boolean canContinueToUse() 
    {
    	return this.mob.getAnimationTick() > 0;
    }
    
    @Override
    public void start()
    {
    	this.mob.setAggressive(true);
    	this.mob.setAnimationPlaying(true);
    	this.mob.setAnimationTick(this.getDuration());
    	this.delay = this.adjustedTickDelay(this.getDelay());
    	if(this.stopOnStart())
    	{
        	this.mob.setStopMoveTick(this.getDuration());
        	this.mob.getNavigation().stop();
    	}
    }
	
    @Override
    public void tick() 
    {
    	if(--this.delay == 0) 
    	{
    		this.run();
    	}
    }
    
	@Override
	public void stop()
	{
		this.mob.setStopMoveTick(0);
		this.mob.setAggressive(false);
    	this.mob.setAnimationPlaying(false);
    	this.cooldown = this.mob.tickCount + this.getInterval();
	}
    
    public boolean stopOnStart()
    {
    	return true;
    }

    public void run()
    {
    	
    }

    public int getDelay()
    {
    	return 20;
    }
    
    public abstract int getDuration();

    public abstract int getInterval();
}
