package com.min01.crypticfoes.entity.living;

import java.util.Comparator;
import java.util.List;

import com.min01.crypticfoes.entity.AbstractAnimatableCreature;
import com.min01.crypticfoes.entity.ai.goal.LookAtTargetGoal;
import com.min01.crypticfoes.entity.ai.goal.MoveToTargetGoal;
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
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class EntityBumpy extends AbstractAnimatableCreature
{
	public static final EntityDataAccessor<Integer> HIT_TIME = SynchedEntityData.defineId(EntityBumpy.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Boolean> IS_RAMMING = SynchedEntityData.defineId(EntityBumpy.class, EntityDataSerializers.BOOLEAN);
	
	public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState blockingAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState bumpAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState roar1AnimationState = new SmoothAnimationState();
	public final SmoothAnimationState roar2AnimationState = new SmoothAnimationState();
	public final SmoothAnimationState rollStart1AnimationState = new SmoothAnimationState();
	public final SmoothAnimationState rollStart2AnimationState = new SmoothAnimationState();
	public final SmoothAnimationState rollAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState rollEndAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState stunnedStartAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState stunnedIdleAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState stunnedEndAnimationState = new SmoothAnimationState();
	
	public int blockTime;
	public int rollRotation;
	public int rollTick;
	
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
    			.add(Attributes.ATTACK_DAMAGE, 6.0F)
    			.add(Attributes.MOVEMENT_SPEED, 0.2F);
    }
    
    @Override
    protected void registerGoals()
    {
    	super.registerGoals();
    	this.goalSelector.addGoal(0, new MoveToTargetGoal<>(this));
    	this.goalSelector.addGoal(0, new LookAtTargetGoal<>(this));
    	this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, false)
    	{
    		@Override
    		public void start() 
    		{
    			super.start();
    			EntityBumpy.this.roar();
    		}
    	});
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
    	this.entityData.define(IS_RAMMING, false);
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
    		this.roar1AnimationState.updateWhen(this.isAnimationPlaying(2), this.tickCount);
    		this.roar2AnimationState.updateWhen(this.isAnimationPlaying(3), this.tickCount);
    		this.rollStart1AnimationState.updateWhen(this.isAnimationPlaying(4), this.tickCount);
    		this.rollStart2AnimationState.updateWhen(this.isAnimationPlaying(5), this.tickCount);
    		this.rollAnimationState.updateWhen(this.getAnimationState() == 6, this.tickCount);
    		this.rollEndAnimationState.updateWhen(this.isAnimationPlaying(7), this.tickCount);
    		this.stunnedStartAnimationState.updateWhen(this.isAnimationPlaying(8), this.tickCount);
    		this.stunnedIdleAnimationState.updateWhen(this.isAnimationPlaying(9), this.tickCount);
    		this.stunnedEndAnimationState.animateWhen(this.isAnimationPlaying(10), this.tickCount);
    	}
    	else
    	{
        	if(this.tickCount % 60 == 0 && this.getNavigation().isDone() && this.getAnimationState() == 0)
        	{
        		List<EntityBumpy> list = this.level.getEntitiesOfClass(EntityBumpy.class, this.getBoundingBox().inflate(3.0F), t -> t != this && t.isAlive());
        		list.forEach(t -> 
        		{
        			float dist = this.distanceTo(t);
        			if(dist >= 5)
        			{
        				this.getNavigation().moveTo(t, 1.2F);
        			}
        			else if(dist <= 3)
        			{
        				Vec3 pos = t.position().add(CrypticUtil.getVelocityTowards(t.position(), this.position(), 0.5F));
        				this.getNavigation().moveTo(pos.x, pos.y, pos.z, 1.0F);
        			}
        		});
        	}
        	if(this.getAnimationState() == 6)
        	{
        		if(!this.horizontalCollision)
        		{
            		this.rollTick++;
        		}
        		if(this.getTarget() == null || !this.getTarget().isAlive())
        		{
        			this.rollRotation = 0;
        			this.setRamming(false);
        			this.setHitTime(0);
        			this.setAnimationState(7);
        			this.setAnimationTick(12);
        			this.setStopMoveTick(this.getAnimationState());
        			this.setStopLookTick(this.getAnimationState());
        		}
        		else
        		{
        			if(this.rollRotation == 0)
        			{
        				List<EntityBumpy> list = this.level.getEntitiesOfClass(EntityBumpy.class, this.getBoundingBox().inflate(5.0F), t -> t.isAlive() && t.getAnimationState() == 6);
        				list.sort(Comparator.comparing(Entity::getUUID));
        				this.rollRotation = (360 / Math.max(list.size(), 1)) * (list.indexOf(this) + 1);
        			}
            		this.rollRotation += 3;
            		if(!this.isRamming() && Math.random() <= 0.005F)
            		{
            			this.setRamming(true);
            		}
            		if(this.isRamming())
            		{
            			if(this.horizontalCollision)
            			{
            				if(this.rollTick % 3 == 0 && this.getAnimationState() == 6)
            				{
            					this.setAnimationState(8);
            					this.setAnimationTick(20);
            					this.setStopMoveTick(this.getAnimationTick());
            					this.setStopLookTick(this.getAnimationTick());
            				}
            			}
            			else
            			{
            				if(this.distanceTo(this.getTarget()) <= 1.5F)
            				{
                				this.doHurtTarget(this.getTarget());
                    			this.setRamming(false);
            				}
            			}
            		}
        		}
        	}
    	}
    	if(this.blockTime > 0)
    	{
    		this.blockTime--;
    	}
    	if(this.horizontalCollision && !this.isRamming())
    	{
    		this.bumpToBlock();
    	}
    }
    
    @Override
    public boolean hurt(DamageSource pSource, float pAmount)
    {
    	if(!pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && this.getAnimationState() != 9)
    	{
    		if(this.getAnimationState() == 0)
    		{
        		if(this.blockTime <= 0)
        		{
            		this.blockTime = 10;
        		}
        		this.setHitTime(this.getHitTime() + 1);
    		}
    		return false;
    	}
    	return super.hurt(pSource, pAmount);
    }
    
    @Override
    protected void doPush(Entity pEntity)
    {
    	super.doPush(pEntity);
    	this.bump(pEntity);
    }
    
    @Override
    public void push(Entity pEntity) 
    {
    	super.push(pEntity);
    	this.bump(pEntity);
    }
    
    public void roar()
    {
		this.setAnimationState(this.random.nextBoolean() ? 2 : 3);
		this.setAnimationTick(40);
		this.setStopMoveTick(this.getAnimationTick());
		this.getNavigation().stop();
		if(this.getTarget() != null && this.getTarget().isAlive())
		{
			List<EntityBumpy> list = this.level.getEntitiesOfClass(EntityBumpy.class, this.getBoundingBox().inflate(10.0F), t -> t != this && t.isAlive() && t.getTarget() == null);
			list.forEach(t -> 
			{
				t.setHitTime(3);
			});
		}
    }
    
    public void bumpToBlock()
    {
    	if(this.getAnimationState() == 0)
    	{
    		this.setAnimationState(1);
    		this.setAnimationTick(34);
    		this.setStopMoveTick(this.getAnimationTick());
    		this.setStopLookTick(this.getAnimationTick());
    		this.getNavigation().stop();
    	}
    }
    
    public void bump(Entity entity)
    {
    	if(entity instanceof EntityBumpy bumpy && bumpy.getAnimationState() == 0 && this.getAnimationState() == 0)
    	{
    		this.setAnimationState(1);
    		this.setAnimationTick(34);
    		this.setStopMoveTick(this.getAnimationTick());
    		this.setStopLookTick(this.getAnimationTick());
    		this.getNavigation().stop();
    		
    		bumpy.setAnimationState(-1);
    		bumpy.setAnimationTick(4);
    		bumpy.setStopMoveTick(bumpy.getAnimationTick());
    		bumpy.setStopLookTick(bumpy.getAnimationTick());
    		bumpy.getNavigation().stop();
    	}
    }
    
    @Override
    public void moveToTarget()
    {
    	if(this.getAnimationState() == 6 && !this.isRamming())
    	{
    		Vec2 rot = new Vec2(0.0F, this.rollRotation);
    		Vec3 pos = CrypticUtil.getLookPos(rot, this.getTarget().position(), 0, 0, 12);
    		this.getNavigation().moveTo(pos.x, pos.y, pos.z, 1.5F);
    	}
    	else if(this.isRamming())
    	{
    		this.getNavigation().moveTo(this.getTarget(), 1.5F);
    	}
    	else
    	{
    		super.moveToTarget();
    	}
    }
    
    @Override
    public void lookAtTarget() 
    {
    	if(this.getAnimationState() != 6)
    	{
        	super.lookAtTarget();
    	}
    }
    
    @Override
    public boolean onAnimationEnd(int animationState) 
    {
    	if(animationState == -1)
    	{
    		this.setAnimationState(1);
    		this.setAnimationTick(34);
    		this.setStopMoveTick(this.getAnimationTick());
    		this.setStopLookTick(this.getAnimationTick());
    		this.getNavigation().stop();
    		return false;
    	}
    	if(animationState == 2 || animationState == 3)
    	{
    		this.setAnimationState(this.random.nextBoolean() ? 4 : 5);
    		this.setAnimationTick(10);
    		return false;
    	}
    	if(animationState == 4 || animationState == 5 || animationState == 10)
    	{
    		this.setAnimationState(6);
    		return false;
    	}
    	if(animationState == 6)
    	{
    		return false;
    	}
    	if(animationState == 8)
    	{
    		this.setAnimationState(9);
    		this.setAnimationTick(100);
    		this.setStopMoveTick(this.getAnimationTick());
    		this.setStopLookTick(this.getAnimationTick());
    		return false;
    	}
    	if(animationState == 9)
    	{
    		this.setAnimationState(10);
    		this.setAnimationTick(25);
    		this.setStopMoveTick(this.getAnimationTick());
    		this.setStopLookTick(this.getAnimationTick());
    		return false;
    	}
    	return super.onAnimationEnd(animationState);
    }
    
    @Override
    public boolean canBeCollidedWith()
    {
    	return true;
    }
    
    @Override
    public boolean canCollideWith(Entity pEntity) 
    {
    	if(pEntity instanceof EntityBumpy)
    	{
    		return false;
    	}
    	return super.canCollideWith(pEntity);
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
    	pCompound.putBoolean("isRamming", this.isRamming());
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag pCompound)
    {
    	super.readAdditionalSaveData(pCompound);
    	this.setHitTime(pCompound.getInt("HitTime"));
    	this.setRamming(pCompound.getBoolean("isRamming"));
    }
    
    public void setHitTime(int value)
    {
    	this.entityData.set(HIT_TIME, value);
    }
    
    public int getHitTime()
    {
    	return this.entityData.get(HIT_TIME);
    }
    
    public void setRamming(boolean value)
    {
    	this.entityData.set(IS_RAMMING, value);
    }
    
    public boolean isRamming()
    {
    	return this.entityData.get(IS_RAMMING);
    }
}
