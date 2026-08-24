package com.min01.crypticfoes.api.ai.goal;

import com.min01.crypticfoes.api.entity.IAnimatable;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public abstract class AbstractAnimationGoal<T extends Mob & IAnimatable> extends Goal
{
	protected int delay;
	protected int cooldown;
	
	protected final T mob;
	
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
    	return this.mob.isAnimationPlaying();
    }
    
    @Override
    public void start()
    {
    	this.mob.setAggressive(true);
    	this.mob.setAnimationTick(this.getDuration());
    	this.delay = this.adjustedTickDelay(this.getDelay());
    	if(this.mob.canMove())
    	{
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
		this.mob.setAggressive(false);
    	this.cooldown = this.mob.tickCount + this.getInterval();
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
