package com.min01.crypticfoes.entity.living;

import com.min01.crypticfoes.entity.AbstractAnimatableMonster;
import com.min01.crypticfoes.entity.ai.goal.PetrifiedShootStoneGoal;
import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityPetrified extends AbstractAnimatableMonster
{
	public static final EntityDataAccessor<Boolean> HAS_STONE = SynchedEntityData.defineId(EntityPetrified.class, EntityDataSerializers.BOOLEAN);
	
	public final AnimationState idleAnimationState = new AnimationState();
	public final AnimationState idleNoneAnimationState = new AnimationState();
	public final AnimationState throwAnimationState = new AnimationState();
	public final AnimationState reloadingAnimationState = new AnimationState();
	
	public EntityPetrified(EntityType<? extends Monster> p_33002_, Level p_33003_) 
	{
		super(p_33002_, p_33003_);
		this.posArray = new Vec3[1];
	}
	
    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
    			.add(Attributes.MAX_HEALTH, 20.0F)
    			.add(Attributes.MOVEMENT_SPEED, 0.25F)
    			.add(Attributes.FOLLOW_RANGE, 32.0F);
    }
    
    @Override
    protected void defineSynchedData()
    {
    	super.defineSynchedData();
    	this.entityData.define(HAS_STONE, true);
    }
    
    @Override
    protected void registerGoals() 
    {
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 6.0F, 1.5D, 1.7D)
        {
        	@Override
        	public boolean canUse()
        	{
        		return super.canUse() && !EntityPetrified.this.hasStone();
        	}
        });
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D)
        {
        	@Override
        	public boolean canUse() 
        	{
        		return super.canUse() && EntityPetrified.this.getTarget() == null;
        	}
        });
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F)
        {
        	@Override
        	public boolean canUse() 
        	{
        		return super.canUse() && EntityPetrified.this.getTarget() == null;
        	}
        });
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this)
        {
        	@Override
        	public boolean canUse() 
        	{
        		return super.canUse() && EntityPetrified.this.getTarget() == null;
        	}
        });
        this.goalSelector.addGoal(4, new PetrifiedShootStoneGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }
    
	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> p_219422_) 
	{
        if(ANIMATION_STATE.equals(p_219422_) && this.level.isClientSide) 
        {
            switch(this.getAnimationState()) 
            {
        		case 0: 
        		{
        			this.stopAllAnimationStates();
        			break;
        		}
        		case 1: 
        		{
        			this.stopAllAnimationStates();
        			this.throwAnimationState.start(this.tickCount);
        			break;
        		}
        		case 2: 
        		{
        			this.stopAllAnimationStates();
        			this.reloadingAnimationState.start(this.tickCount);
        			break;
        		}
            }
        }
	}
	
	@Override
	public void stopAllAnimationStates()
	{
		this.throwAnimationState.stop();
		this.reloadingAnimationState.stop();
	}
    
    @Override
    public void tick()
    {
    	super.tick();
    	if(this.level.isClientSide)
    	{
    		this.idleAnimationState.animateWhen(this.hasStone() && !CrypticUtil.isMoving(this) && this.getAnimationState() == 0, this.tickCount);
    		this.idleNoneAnimationState.animateWhen(!this.hasStone() && !CrypticUtil.isMoving(this) && this.getAnimationState() == 0, this.tickCount);
    	}
    	
    	if(this.getTarget() != null)
    	{
    		if(this.canLook())
    		{
    			if(this.hasStone())
    			{
        			this.getLookControl().setLookAt(this.getTarget(), 30.0F, 30.0F);
        			this.lookAt(this.getTarget(), 30.0F, 30.0F);
    			}
    		}
			if(this.canMove())
			{
				if(this.hasStone() && this.distanceTo(this.getTarget()) >= 12.0F)
				{
					this.getNavigation().moveTo(this.getTarget(), 1.0F);
				}
			}
    	}
    	
    	if(!this.hasStone())
    	{
			if(this.getAnimationState() == 0)
			{
	    		if(this.getAnimationTick() <= 0)
	    		{	  			
	    			this.setAnimationState(2);
	    			this.setAnimationTick(34);
	    		}
			}
			if(this.getAnimationState() == 2)
			{
	    		if(this.getAnimationTick() <= 24)
	    		{
					this.setHasStone(true);
	    		}
			}
    	}
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource p_33034_) 
    {
    	return SoundEvents.SKELETON_HURT;
    }
    
    @Override
    protected SoundEvent getAmbientSound() 
    {
    	return SoundEvents.SKELETON_AMBIENT;
    }
    
    @Override
    protected SoundEvent getDeathSound()
    {
    	return SoundEvents.SKELETON_DEATH;
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) 
    {
    	super.addAdditionalSaveData(p_21484_);
    	p_21484_.putBoolean("HasStone", this.hasStone());
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_) 
    {
    	super.readAdditionalSaveData(p_21450_);
    	if(p_21450_.contains("HasStone"))
    	{
    		this.setHasStone(p_21450_.getBoolean("HasStone"));
    	}
    }
    
    public void setHasStone(boolean value)
    {
    	this.entityData.set(HAS_STONE, value);
    }
    
    public boolean hasStone()
    {
    	return this.entityData.get(HAS_STONE);
    }
}
