package com.min01.crypticfoes.entity.living;

import com.min01.crypticfoes.entity.AbstractAnimatableMonster;
import com.min01.crypticfoes.entity.ai.goal.LookAtTargetGoal;
import com.min01.crypticfoes.entity.ai.goal.MoveToTargetGoal;
import com.min01.crypticfoes.entity.ai.goal.PetrifiedShootStoneGoal;
import com.min01.crypticfoes.misc.SmoothAnimationState;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

public class EntityPetrified extends AbstractAnimatableMonster
{
	public static final EntityDataAccessor<Boolean> HAS_STONE = SynchedEntityData.defineId(EntityPetrified.class, EntityDataSerializers.BOOLEAN);
	
	public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState idleNoneAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState throwAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState reloadingAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState runAnimationState = new SmoothAnimationState();
	
	public EntityPetrified(EntityType<? extends Monster> pEntityType, Level pLevel) 
	{
		super(pEntityType, pLevel);
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
    	super.registerGoals();
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 6.0F, 1.5D, 1.7D)
        {
        	@Override
        	public boolean canUse()
        	{
        		return super.canUse() && !EntityPetrified.this.hasStone();
        	}
        });
        this.goalSelector.addGoal(4, new PetrifiedShootStoneGoal(this));
        this.goalSelector.addGoal(0, new MoveToTargetGoal<>(this));
        this.goalSelector.addGoal(0, new LookAtTargetGoal<>(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }
    
    @Override
    public void tick()
    {
    	super.tick();
    	if(this.level.isClientSide)
    	{
    		this.idleAnimationState.updateWhen(this.hasStone() && this.getAnimationState() == 0, this.tickCount);
    		this.idleNoneAnimationState.updateWhen(!this.hasStone() && this.getAnimationState() == 0, this.tickCount);
    		this.throwAnimationState.updateWhen(this.isUsingSkill(1), this.tickCount);
    		this.reloadingAnimationState.updateWhen(this.getAnimationState() == 2, this.tickCount);
    		this.runAnimationState.updateWhen(this.hasStone(), this.tickCount);
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
	    		if(this.getAnimationTick() <= 0)
	    		{
	    			this.setAnimationState(0);
	    		}
			}
    	}
    }
    
    @Override
    public void lookAtTarget()
    {
		if(this.hasStone())
		{
	    	super.lookAtTarget();
		}
    }
    
    @Override
    public void moveToTarget() 
    {
		if(this.hasStone() && this.distanceTo(this.getTarget()) >= 12.0F)
		{
	    	super.moveToTarget();
		}
    }

    public static boolean checkPetrifiedSpawnRules(EntityType<? extends Monster> pType, ServerLevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom)
    {
        return pLevel.getDifficulty() != Difficulty.PEACEFUL && !isDarkEnoughToSpawn(pLevel, pPos, pRandom) && checkMobSpawnRules(pType, pLevel, pSpawnType, pPos, pRandom);
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
    public void addAdditionalSaveData(CompoundTag pCompound) 
    {
    	super.addAdditionalSaveData(pCompound);
    	pCompound.putBoolean("HasStone", this.hasStone());
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) 
    {
    	super.readAdditionalSaveData(pCompound);
    	if(pCompound.contains("HasStone"))
    	{
    		this.setHasStone(pCompound.getBoolean("HasStone"));
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
