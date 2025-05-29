package com.min01.crypticfoes.entity.living;

import com.min01.crypticfoes.entity.AbstractAnimatableCreature;
import com.min01.crypticfoes.misc.CrypticExplosion;
import com.min01.crypticfoes.particle.CrypticParticles;
import com.min01.crypticfoes.sound.CrypticSounds;
import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class EntityBrancher extends AbstractAnimatableCreature
{
	public static final EntityDataAccessor<Integer> ANGER_COUNT = SynchedEntityData.defineId(EntityBrancher.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> EXPLOSION_MAX_TICK = SynchedEntityData.defineId(EntityBrancher.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Boolean> IS_RUNNING = SynchedEntityData.defineId(EntityBrancher.class, EntityDataSerializers.BOOLEAN);
	
	public final AnimationState idleAnimationState = new AnimationState();
	public final AnimationState shiverAnimationState = new AnimationState();
	public final AnimationState explosionAnimationState = new AnimationState();
	
    public float brightness;
    public float brightnessOld;
    public int glowingTicks;
    
    public int explosionTick;
    public int explosionMaxTick;
    
	public EntityBrancher(EntityType<? extends PathfinderMob> p_33002_, Level p_33003_) 
	{
		super(p_33002_, p_33003_);
		this.setPathfindingMalus(BlockPathTypes.LEAVES, 0.0F);
	}
	
    public static AttributeSupplier.Builder createAttributes()
    {
        return Mob.createMobAttributes()
    			.add(Attributes.MAX_HEALTH, 30.0F)
    			.add(Attributes.MOVEMENT_SPEED, 0.8F);
    }
    
    @Override
    protected void defineSynchedData() 
    {
    	super.defineSynchedData();
    	this.entityData.define(ANGER_COUNT, 0);
    	this.entityData.define(EXPLOSION_MAX_TICK, 0);
    	this.entityData.define(IS_RUNNING, false);
    }
    
    @Override
    protected void registerGoals() 
    {
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.25F));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true)
        {
        	@Override
        	public boolean canUse() 
        	{
        		return super.canUse() && EntityBrancher.this.isAngry() && EntityBrancher.this.getTarget() == null;
        	}
        });
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this)
        {
        	@Override
        	public void start() 
        	{
        		super.start();
        		EntityBrancher.this.setAngerCount(2);
        		EntityBrancher.this.playSound(CrypticSounds.BRANCHER_ANGRY.get());
        	}
        });
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
        			this.shiverAnimationState.start(this.tickCount);
        			break;
        		}
        		case 2: 
        		{
        			this.stopAllAnimationStates();
        			this.explosionAnimationState.start(this.tickCount);
        			break;
        		}
            }
        }
	}
	
	@Override
	public void stopAllAnimationStates() 
	{
		this.shiverAnimationState.stop();
	}
    
    @Override
    public void tick() 
    {
    	super.tick();
    	if(this.level.isClientSide)
    	{
    		this.idleAnimationState.animateWhen(!CrypticUtil.isMoving(this), this.tickCount);
    		
            ++this.glowingTicks;
            this.brightness += (0.0F - this.brightness) * 0.8F;
    	}
    	
    	if(this.isAngry() && this.getTarget() != null)
    	{
    		this.setRunning(true);
    		this.getNavigation().moveTo(this.getTarget(), 0.42F);
    		if(this.distanceTo(this.getTarget()) <= 2.0F)
    		{
    			if(this.getAnimationState() == 0)
    			{
        			this.setAnimationState(2);
    			}
    			this.setExplosionMaxTick(40);
    		}
    		else if(this.getAnimationState() == 2)
    		{
    			this.setExplosionMaxTick(160);
    		}
    	}
    	
    	if(this.getAnimationState() == 2)
    	{
    		if(this.explosionTick <= this.getExplosionMaxTick())
    		{
    			this.explosionTick++;
    			if(this.explosionTick % 20 == 0)
    			{
        	        this.playSound(CrypticSounds.BRANCHER_HEARTBEAT.get());
    			}
    		}
    		else
    		{
    			this.brancherExplode(this.position(), 2.0F);
    	        this.playSound(CrypticSounds.BRANCHER_HISS.get());
    			this.discard();
    		}
    	}
    	
    	if(this.getAnimationTick() <= 0)
    	{
        	if(this.getAnimationState() == 1)
        	{
        		this.setAnimationState(0);
        	}
    	}
    }
    
    public void brancherExplode(Vec3 pos, float radius)
    {
        CrypticExplosion explosion = new CrypticExplosion(this.level, this, pos.x, pos.y, pos.z, radius, CrypticParticles.BRANCHER_EXPLOSION_SEED.get());
        explosion.explode();
        explosion.finalizeExplosion(false);
        this.playSound(CrypticSounds.BRANCHER_EXPLOSION.get());
    }
    
    public static boolean checkBrancherSpawnRules(EntityType<EntityBrancher> p_219014_, ServerLevelAccessor p_219015_, MobSpawnType p_219016_, BlockPos p_219017_, RandomSource p_219018_)
    {
    	return p_219015_.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(p_219015_, p_219017_, p_219018_) && checkMobSpawnRules(p_219014_, p_219015_, p_219016_, p_219017_, p_219018_);
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) 
    {
    	super.addAdditionalSaveData(p_21484_);
    	p_21484_.putBoolean("isRunning", this.isRunning());
    	p_21484_.putInt("AngerCount", this.getAngerCount());
    	p_21484_.putInt("ExplosionMaxTick", this.getExplosionMaxTick());
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_)
    {
    	super.readAdditionalSaveData(p_21450_);
    	if(p_21450_.contains("isRunning"))
    	{
    		this.setRunning(p_21450_.getBoolean("isRunning"));
    	}
    	if(p_21450_.contains("AngerCount"))
    	{
    		this.setAngerCount(p_21450_.getInt("AngerCount"));
    	}
    	if(p_21450_.contains("ExplosionMaxTick"))
    	{
    		this.setExplosionMaxTick(p_21450_.getInt("ExplosionMaxTick"));
    	}
    }
    
    @Override
    protected SoundEvent getDeathSound()
    {
    	return CrypticSounds.BRANCHER_DEATH.get();
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource p_21239_) 
    {
    	return CrypticSounds.BRANCHER_HURT.get();
    }
    
    public void setRunning(boolean value)
    {
    	this.entityData.set(IS_RUNNING, value);
    }
    
    public boolean isRunning()
    {
    	return this.entityData.get(IS_RUNNING);
    }
    
    public void setExplosionMaxTick(int value)
    {
    	this.entityData.set(EXPLOSION_MAX_TICK, value);
    }
    
    public int getExplosionMaxTick()
    {
    	return this.entityData.get(EXPLOSION_MAX_TICK);
    }
    
    public void setAngerCount(int value)
    {
    	this.entityData.set(ANGER_COUNT, value);
    	if(value == 2)
    	{
    		this.playSound(CrypticSounds.BRANCHER_ANGRY.get());
    	}
    }
    
    public int getAngerCount()
    {
    	return this.entityData.get(ANGER_COUNT);
    }
    
    public boolean isAngry()
    {
    	return this.getAngerCount() >= 2;
    }
}
