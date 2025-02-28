package com.min01.crypticfoes.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractAnimatableMonster extends Monster implements IAnimatable, IPosArray
{
	public static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(AbstractAnimatableMonster.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> ANIMATION_TICK = SynchedEntityData.defineId(AbstractAnimatableMonster.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Boolean> CAN_LOOK = SynchedEntityData.defineId(AbstractAnimatableMonster.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> CAN_MOVE = SynchedEntityData.defineId(AbstractAnimatableMonster.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> IS_USING_SKILL = SynchedEntityData.defineId(AbstractAnimatableMonster.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> HAS_TARGET = SynchedEntityData.defineId(AbstractAnimatableMonster.class, EntityDataSerializers.BOOLEAN);

	public Vec3[] posArray;
	
	public AbstractAnimatableMonster(EntityType<? extends Monster> p_33002_, Level p_33003_) 
	{
		super(p_33002_, p_33003_);
		this.noCulling = true;
	}
	
	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(ANIMATION_STATE, 0);
		this.entityData.define(ANIMATION_TICK, 0);
		this.entityData.define(CAN_LOOK, true);
		this.entityData.define(CAN_MOVE, true);
		this.entityData.define(IS_USING_SKILL, false);
		this.entityData.define(HAS_TARGET, false);
	}
    
    @Override
    public void tick()
    {
    	super.tick();

		if(!this.level.isClientSide)
		{
			this.setHasTarget(this.getTarget() != null);
		}
		
		if(this.getAnimationTick() > 0)
		{
			this.setAnimationTick(this.getAnimationTick() - 1);
		}
    }
    
	public void stopAllAnimationStates()
	{
		
	}
    
    @Override
    public Vec3[] getPosArray()
    {
    	return this.posArray;
    }
	
	public void setHasTarget(boolean value)
	{
		this.entityData.set(HAS_TARGET, value);
	}
	
	public boolean hasTarget()
	{
		return this.entityData.get(HAS_TARGET);
	}
	
	@Override
	public void setUsingSkill(boolean value) 
	{
		this.entityData.set(IS_USING_SKILL, value);
	}
	
	@Override
	public boolean isUsingSkill() 
	{
		return this.getAnimationTick() > 0 || this.entityData.get(IS_USING_SKILL);
	}
	
    public void setCanLook(boolean value)
    {
    	this.entityData.set(CAN_LOOK, value);
    }
    
    public boolean canLook()
    {
    	return this.entityData.get(CAN_LOOK);
    }
    
    @Override
    public void setCanMove(boolean value)
    {
    	this.entityData.set(CAN_MOVE, value);
    }
    
    @Override
    public boolean canMove()
    {
    	return this.entityData.get(CAN_MOVE);
    }
    
    @Override
    public void setAnimationTick(int value)
    {
        this.entityData.set(ANIMATION_TICK, value);
    }
    
    @Override
    public int getAnimationTick()
    {
        return this.entityData.get(ANIMATION_TICK);
    }
    
    public void setAnimationState(int value)
    {
        this.entityData.set(ANIMATION_STATE, value);
    }
    
    public int getAnimationState()
    {
        return this.entityData.get(ANIMATION_STATE);
    }
}
