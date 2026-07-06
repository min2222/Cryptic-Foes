package com.min01.crypticfoes.entity.living;

import java.util.Comparator;
import java.util.List;

import com.min01.crypticfoes.entity.AbstractAnimatableCreature;
import com.min01.crypticfoes.entity.ai.control.AnimationBodyRotationControl;
import com.min01.crypticfoes.entity.ai.goal.LookAtTargetGoal;
import com.min01.crypticfoes.entity.ai.goal.MoveToTargetGoal;
import com.min01.crypticfoes.misc.CrypticEntityDataSerializers;
import com.min01.crypticfoes.misc.SmoothAnimationState;
import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class BumpyEntity extends AbstractAnimatableCreature
{
	public static final EntityDataAccessor<Integer> HIT_TIME = SynchedEntityData.defineId(BumpyEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Boolean> IS_RAMMING = SynchedEntityData.defineId(BumpyEntity.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Vec3> RAM_POS = SynchedEntityData.defineId(BumpyEntity.class, CrypticEntityDataSerializers.VEC3.get());
	
	public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState walkAnimationState = new SmoothAnimationState();
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
	public final SmoothAnimationState stunnedEndAnimationState = new SmoothAnimationState(0.5F, true);

	public int bumpCooldown;
	public int blockTime;
	public double rollRotation;
	
	public BumpyEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
		this.setMaxUpStep(1.0F);
		this.animationEntries.addWalkEntry(this.walkAnimationState, 2.5F);
		this.animationEntries.addExtraEntry(this.rollAnimationState);
	}
	
    public static AttributeSupplier.Builder createAttributes()
    {
        return Mob.createMobAttributes()
    			.add(Attributes.MAX_HEALTH, 20.0F)
    			.add(Attributes.ARMOR, 15.0F)
    			.add(Attributes.KNOCKBACK_RESISTANCE, 1.0F)
    			.add(Attributes.ATTACK_DAMAGE, 6.0F)
    			.add(Attributes.MOVEMENT_SPEED, 0.2F)
    			.add(Attributes.FOLLOW_RANGE, 40.0F);
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
    			BumpyEntity.this.roar();
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
    	this.entityData.define(RAM_POS, Vec3.ZERO);
    }
    
    @Override
    public void tick() 
    {
    	super.tick();
    	if(this.blockTime > 0)
    	{
    		this.blockTime--;
    	}
    	if(this.bumpCooldown > 0)
    	{
    		this.bumpCooldown--;
    	}
    	if(this.level.isClientSide)
    	{
    		this.idleAnimationState.updateWhen(this.getAnimationState() == 0, this.tickCount);
    		this.walkAnimationState.updateWhen(true, this.tickCount);
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
    		this.stunnedEndAnimationState.updateWhen(this.isAnimationPlaying(10), this.tickCount);
    	}
    	else
    	{
        	if(this.getAnimationState() == 6)
        	{
        		if(this.getTarget() == null || !this.getTarget().isAlive())
        		{
        			this.rollRotation = 0;
        			this.setRamming(false);
        			this.setHitTime(0);
        			this.setAnimationState(7);
        			this.setAnimationTick(12);
        			this.setStopMoveTick(this.getAnimationState());
        			this.setStopLookTick(this.getAnimationState());
        			this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2F);
        		}
        		else
        		{
        			if(this.rollRotation == 0)
        			{
        				List<BumpyEntity> list = this.level.getEntitiesOfClass(BumpyEntity.class, this.getBoundingBox().inflate(5.0F), t -> t.isAlive() && t.getAnimationState() == 6);
        				list.sort(Comparator.comparing(Entity::getUUID));
        				this.rollRotation = Math.toRadians((360 / Math.max(list.size(), 1)) * (list.indexOf(this) + 1));
        			}
            		if(!this.isRamming())
            		{
            			if(this.tickCount % this.random.nextInt(100, 250) == 0 && this.random.nextFloat() <= 0.5F && this.distanceTo(this.getTarget()) >= 3.0F)
            			{
                			this.setRamming(true);
                			this.setAnimationTick(30);
                			this.getNavigation().stop();
            			}
            		}
            		else if(this.getAnimationTick() <= 0)
        			{
            			if(this.getRamPos().equals(Vec3.ZERO))
            			{
            				this.setRamPos(CrypticUtil.getLookPos(new Vec2(this.getXRot(), this.yHeadRot), this.position(), 0, 0, 12.0F));
            			}
            			else
            			{
            				if(this.getNavigation().isDone())
            				{
                    			this.setRamming(false);
                    			this.setRamPos(Vec3.ZERO);
            				}
            				else if(this.isWithinMeleeAttackRange(this.getTarget()))
                			{
                				this.doHurtTarget(this.getTarget());
                    			this.setRamming(false);
                    			this.setRamPos(Vec3.ZERO);
                			}
            			}
        			}
        		}
        	}
        	else
        	{
            	if(this.horizontalCollision)
            	{
            		this.bumpToBlock();
            	}
            	if(this.tickCount % 60 == 0)
            	{
            		List<BumpyEntity> list = this.level.getEntitiesOfClass(BumpyEntity.class, this.getBoundingBox().inflate(5.0F), t -> t != this && t.isAlive());
            		list.forEach(t -> 
            		{
            			float dist = this.distanceTo(t);
            			if(dist >= 6.0F)
            			{
            				this.getNavigation().moveTo(t, 0.8F);
            			}
            		});
            	}
        	}
    	}
    }

    @Override
    public double getMeleeAttackRangeSqr(LivingEntity pEntity) 
    {
        return (double)(this.getBbWidth() * 1.0F * this.getBbWidth() * 1.0F + pEntity.getBbWidth());
    }
    
    @Override
    public boolean hurt(DamageSource pSource, float pAmount)
    {
    	if(!pSource.is(DamageTypeTags.BYPASSES_ARMOR) && this.getAnimationState() != 9)
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
    
    public void stun()
    {
		if(this.getAnimationState() == 6)
		{
			this.setAnimationState(8);
			this.setAnimationTick(20);
			this.setDeltaMovement(Vec3.ZERO);
			this.setStopMoveTick(this.getAnimationTick());
			this.setStopLookTick(this.getAnimationTick());
		}
    }
    
    public void roar()
    {
		this.setAnimationState(this.random.nextBoolean() ? 2 : 3);
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25F);
		this.playSound(SoundEvents.NOTE_BLOCK_BELL.get(), 5.0F, 1.0F);
		this.setAnimationTick(40);
		this.setStopMoveTick(this.getAnimationTick());
		this.getNavigation().stop();
		if(this.getTarget() != null && this.getTarget().isAlive())
		{
			List<BumpyEntity> list = this.level.getEntitiesOfClass(BumpyEntity.class, this.getBoundingBox().inflate(10.0F), t -> t != this && t.isAlive() && t.getTarget() == null);
			list.forEach(t -> 
			{
				t.setHitTime(3);
			});
		}
    }
    
    public void bumpToBlock()
    {
    	if(!this.level.isClientSide)
    	{
        	if(this.getAnimationState() == 0 && this.bumpCooldown <= 0)
        	{
        		this.setAnimationState(1);
        		this.setAnimationTick(34);
        		this.setStopMoveTick(this.getAnimationTick());
        		this.setStopLookTick(this.getAnimationTick());
        		this.getNavigation().stop();
        		this.bumpCooldown = 100;
        	}
    	}
    }
    
    public void bump(Entity entity)
    {
    	if(!this.level.isClientSide)
    	{
        	if(entity instanceof BumpyEntity bumpy && bumpy.getAnimationState() == 0 && this.getAnimationState() == 0 && this.bumpCooldown <= 0)
        	{
        		this.setAnimationState(1);
        		this.setAnimationTick(34);
        		this.setStopMoveTick(this.getAnimationTick());
        		this.setStopLookTick(this.getAnimationTick());
        		this.getNavigation().stop();
        		this.bumpCooldown = 100;
        		
        		bumpy.setAnimationState(-1);
        		bumpy.setAnimationTick(4);
        		bumpy.setStopMoveTick(bumpy.getAnimationTick());
        		bumpy.setStopLookTick(bumpy.getAnimationTick());
        		bumpy.getNavigation().stop();
        		bumpy.bumpCooldown = 100;
        	}
    	}
    }
    
    @Override
    public void moveToTarget()
    {
    	if(this.getAnimationState() == 6 && !this.isRamming())
    	{
    		this.rollRotation += 0.05F;
    		
            double radius = 12.0;
            Vec3 targetPos = this.getTarget().position();

            double posX = targetPos.x + (radius * Math.cos(this.rollRotation));
            double posZ = targetPos.z + (radius * Math.sin(this.rollRotation));
            double posY = targetPos.y;

            this.getNavigation().moveTo(posX, posY, posZ, 1.75F);
    	}
    	else if(this.getAnimationTick() <= 0)
    	{
    		Vec3 pos = this.getRamPos();
            this.getNavigation().moveTo(pos.x, pos.y, pos.z, 2.0F);
    	}
    }
    
    @Override
    public float maxMoveTurnY()
    {
    	if(this.getAnimationState() == 6)
    	{
    		return 60.0F;
    	}
    	return super.maxMoveTurnY();
    }
    
    @Override
    public void lookAtTarget() 
    {
    	if(!this.getRamPos().equals(Vec3.ZERO))
    	{
    		Vec3 pos = this.getRamPos();
    		this.getLookControl().setLookAt(pos.x, pos.y, pos.z, 100.0F, 100.0F);
    	}
    	else if(this.getAnimationState() != 6 || (this.isRamming() && this.getAnimationTick() > 0))
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
    protected BodyRotationControl createBodyControl()
    {
    	return new BumpyBodyRotationControl(this);
    }
    
    @Override
    public boolean canCollideWith(Entity pEntity) 
    {
    	return super.canCollideWith(pEntity) && !(pEntity instanceof BumpyEntity);
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
    	pCompound.putInt("BumpCooldown", this.bumpCooldown);
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag pCompound)
    {
    	super.readAdditionalSaveData(pCompound);
    	this.setHitTime(pCompound.getInt("HitTime"));
    	this.setRamming(pCompound.getBoolean("isRamming"));
    	this.bumpCooldown = pCompound.getInt("BumpCooldown");
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
    
    public void setRamPos(Vec3 pos)
    {
    	this.entityData.set(RAM_POS, pos);
    }
    
    public Vec3 getRamPos()
    {
    	return this.entityData.get(RAM_POS);
    }
    
    public class BumpyBodyRotationControl extends AnimationBodyRotationControl<BumpyEntity>
    {
		public BumpyBodyRotationControl(BumpyEntity pMob) 
		{
			super(pMob);
		}
    	
		@Override
		public void clientTick() 
		{
			if(BumpyEntity.this.getAnimationState() != 8 && BumpyEntity.this.getAnimationState() != 9 && BumpyEntity.this.getAnimationState() != 10)
			{
				super.clientTick();
			}
		}
    }
}
