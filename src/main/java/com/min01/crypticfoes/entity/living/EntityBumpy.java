package com.min01.crypticfoes.entity.living;

import com.min01.crypticfoes.entity.AbstractAnimatableCreature;
import com.min01.crypticfoes.misc.SmoothAnimationState;
import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class EntityBumpy extends AbstractAnimatableCreature
{
	public static final EntityDataAccessor<Integer> HIT_TIME = SynchedEntityData.defineId(EntityBumpy.class, EntityDataSerializers.INT);
	
	public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState blockingAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState bumpAnimationState = new SmoothAnimationState();
	
	public int blockTime;
	
	public EntityBumpy(EntityType<? extends PathfinderMob> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
	}
	
    public static AttributeSupplier.Builder createAttributes()
    {
        return Mob.createMobAttributes()
    			.add(Attributes.MAX_HEALTH, 20.0F)
    			.add(Attributes.ARMOR, 15.0F)
    			.add(Attributes.KNOCKBACK_RESISTANCE, 1.0F)
    			.add(Attributes.MOVEMENT_SPEED, 0.2F);
    }
    
    @Override
    protected void registerGoals()
    {
    	super.registerGoals();
    	this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }
    
    @Override
    public boolean canAttack(LivingEntity pTarget)
    {
    	return super.canAttack(pTarget) && (CrypticUtil.isNight(this.level) || this.getHitTime() >= 3);
    }
    
    @Override
    protected void defineSynchedData() 
    {
    	super.defineSynchedData();
    	this.entityData.define(HIT_TIME, 0);
    }
    
    @Override
    public void tick() 
    {
    	super.tick();
    	if(this.level.isClientSide)
    	{
    		this.idleAnimationState.updateWhen(this.getAnimationState() == 0, this.tickCount);
    		this.blockingAnimationState.updateWhen(this.blockTime > 0, this.tickCount);
    		this.bumpAnimationState.updateWhen(this.isAnimationPlaying(1), this.tickCount);
    	}
    	if(this.blockTime > 0)
    	{
    		this.blockTime--;
    	}
    }
    
    @Override
    public boolean hurt(DamageSource pSource, float pAmount)
    {
    	if(!pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
    	{
    		if(this.blockTime <= 0)
    		{
        		this.blockTime = 10;
    		}
    		this.setHitTime(this.getHitTime() + 1);
    		return false;
    	}
    	return super.hurt(pSource, pAmount);
    }
    
    @Override
    protected void doPush(Entity pEntity)
    {
    	super.doPush(pEntity);
    	if(pEntity instanceof EntityBumpy && this.getAnimationState() == 0)
    	{
    		this.setAnimationState(1);
    		this.setAnimationTick(34);
    		this.setStopMoveTick(this.getAnimationTick());
    		this.setStopLookTick(this.getAnimationTick());
    	}
    }
    
    @Override
    public boolean canBeCollidedWith()
    {
    	return true;
    }
    
    public static boolean checkBumpySpawnRules(EntityType<? extends PathfinderMob> pType, ServerLevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom)
    {
    	return CrypticUtil.isNight(pLevel) && checkMobSpawnRules(pType, pLevel, pSpawnType, pPos, pRandom);
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) 
    {
    	super.addAdditionalSaveData(pCompound);
    	pCompound.putInt("HitTime", this.getHitTime());
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag pCompound)
    {
    	super.readAdditionalSaveData(pCompound);
    	this.setHitTime(pCompound.getInt("HitTime"));
    }
    
    public void setHitTime(int value)
    {
    	this.entityData.set(HIT_TIME, value);
    }
    
    public int getHitTime()
    {
    	return this.entityData.get(HIT_TIME);
    }
}
