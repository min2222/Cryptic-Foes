package com.min01.crypticfoes.entity.living;

import java.util.List;

import com.min01.crypticfoes.effect.CrypticEffects;
import com.min01.crypticfoes.entity.AbstractAnimatableMonster;
import com.min01.crypticfoes.entity.ai.goal.HowlerPunchGoal;
import com.min01.crypticfoes.entity.ai.goal.HowlerRoarGoal;
import com.min01.crypticfoes.misc.SmoothAnimationState;
import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EntityHowler extends AbstractAnimatableMonster
{
	public static final EntityDataAccessor<Boolean> IS_SLEEPING = SynchedEntityData.defineId(EntityHowler.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> IS_FALLING = SynchedEntityData.defineId(EntityHowler.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<BlockPos> SLEEP_POS = SynchedEntityData.defineId(EntityHowler.class, EntityDataSerializers.BLOCK_POS);

	public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState sleepAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState awakeAnimationState = new SmoothAnimationState(0.999F);
	public final SmoothAnimationState fallAnimationState = new SmoothAnimationState(0.999F);
	public final SmoothAnimationState landAnimationState = new SmoothAnimationState(0.999F);
	public final SmoothAnimationState roarAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState blinkAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState punchAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState flyAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState flyStartAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState flyEndAnimationState = new SmoothAnimationState(0.999F);
	
	public int ambientTick;
	public int targetTick = 200;
	
	public EntityHowler(EntityType<? extends Monster> p_33002_, Level p_33003_) 
	{
		super(p_33002_, p_33003_);
		this.posArray = new Vec3[1];
	}
	
    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
    			.add(Attributes.MAX_HEALTH, 40.0F)
    			.add(Attributes.ARMOR, 10.0F)
    			.add(Attributes.ATTACK_DAMAGE, 10.0F)
    			.add(Attributes.KNOCKBACK_RESISTANCE, 100.0F)
    			.add(Attributes.MOVEMENT_SPEED, 0.35F)
    			.add(Attributes.FOLLOW_RANGE, 32.0F);
    }
    
    @Override
    protected void registerGoals() 
    {
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D)
        {
        	@Override
        	public boolean canUse() 
        	{
        		return super.canUse() && !EntityHowler.this.isHowlerSleeping() && !EntityHowler.this.isUsingSkill();
        	}
        });
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F)
        {
        	@Override
        	public boolean canUse() 
        	{
        		return super.canUse() && !EntityHowler.this.isHowlerSleeping() && !EntityHowler.this.isUsingSkill();
        	}
        });
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this)
        {
        	@Override
        	public boolean canUse() 
        	{
        		return super.canUse() && !EntityHowler.this.isHowlerSleeping() && !EntityHowler.this.isUsingSkill();
        	}
        });
        this.goalSelector.addGoal(0, new HowlerPunchGoal(this));
        this.goalSelector.addGoal(0, new HowlerRoarGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true) 
        {
        	@Override
        	public boolean canUse() 
        	{
        		return super.canUse() && EntityHowler.this.isHowlerSleeping();
        	}
        	
        	@Override
        	protected void findTarget() 
        	{
        		AABB aabb = this.mob.getBoundingBox().inflate(3.0D, 64.0D, 3.0D);
        		List<Player> list = this.mob.level.getEntitiesOfClass(Player.class, aabb);
        		if(!list.isEmpty())
        		{
        			if(EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(list.get(0)))
        			{
            			this.target = list.get(0);
        			}
        		}
        	}
        });
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }
    
    @Override
    protected void defineSynchedData()
    {
    	super.defineSynchedData();
    	this.entityData.define(IS_SLEEPING, false);
    	this.entityData.define(IS_FALLING, false);
    	this.entityData.define(SLEEP_POS, BlockPos.ZERO);
    }
    
    @Override
    public void tick() 
    {
    	super.tick();
    	this.resetFallDistance();
    	
    	if(this.level.isClientSide)
    	{
    		this.idleAnimationState.updateWhen(!this.isHowlerSleeping() && this.getAnimationState() == 0, this.tickCount);
    		this.sleepAnimationState.updateWhen(this.isHowlerSleeping() && this.getAnimationState() == 1, this.tickCount);
    		this.awakeAnimationState.updateWhen(this.isHowlerSleeping() && this.getAnimationState() == 2, this.tickCount);
    		this.fallAnimationState.updateWhen(!this.isHowlerSleeping() && this.isFalling() && this.getAnimationState() == 0, this.tickCount);
    		this.landAnimationState.updateWhen(!this.isHowlerSleeping() && this.getAnimationState() == 3, this.tickCount);
    		this.roarAnimationState.updateWhen(!this.isHowlerSleeping() && this.isUsingSkill(4), this.tickCount);
    		this.blinkAnimationState.updateWhen(!this.isHowlerSleeping() && this.ambientTick > 0, this.tickCount);
    		this.punchAnimationState.updateWhen(!this.isHowlerSleeping() && this.isUsingSkill(5), this.tickCount);
    		this.flyAnimationState.updateWhen(this.isHowlerSleeping() && this.getAnimationState() == 6, this.tickCount);
    		this.flyStartAnimationState.updateWhen(this.isHowlerSleeping() && this.getAnimationState() == 7, this.tickCount);
    		this.flyEndAnimationState.updateWhen(this.isHowlerSleeping() && this.getAnimationState() == 8, this.tickCount);
    	}
    	
    	if(this.ambientTick > 0)
    	{
			this.ambientTick--;
    	}
    	if(!this.isHowlerSleeping())
    	{
        	if(!this.level.isClientSide)
        	{
        		if(this.getTarget() == null)
        		{
            		this.targetTick++;
        		}
        		else
        		{
        			this.targetTick = 0;
            		if(this.canLook())
            		{
            			this.getLookControl().setLookAt(this.getTarget(), 30.0F, 30.0F);
            		}
        			if(this.canMove())
        			{
    					this.getNavigation().moveTo(this.getTarget(), 1.0F);
        			}
        		}
        		if(this.targetTick >= 200 && !this.level.canSeeSky(this.blockPosition()) && this.getAnimationState() == 0)
        		{
        			BlockPos ceilingPos = CrypticUtil.getCeilingPos(this.level, this.getX(), this.getY(), this.getZ(), -1);
        			if(!this.level.canSeeSky(ceilingPos))
        			{
            			this.setSleepPos(ceilingPos);
            			this.setCanMove(false);
            			this.setCanLook(false);
            			this.setHowlerSleeping(true);
        			}
        		}
        	}
        	if(this.onGround())
        	{
        		if(this.isFalling())
        		{
            		this.setFalling(false);
            		this.setAnimationState(3);
            		this.setAnimationTick(43);
        		}
        		else if(this.getAnimationState() == 3 && this.getAnimationTick() <= 0)
        		{
        			this.setAnimationState(0);
        			this.setCanMove(true);
        			this.setCanLook(true);
        		}
        	}
    	}
    	else if(!this.getSleepPos().equals(BlockPos.ZERO))
    	{
    		if(!this.isFalling() && !this.hasEffect(CrypticEffects.STUNNED.get()))
    		{
        		BlockPos pos = this.getSleepPos();
        		if(this.horizontalDist(pos, this.getX(), this.getZ()) <= 2.0F)
        		{
        			if(this.getAnimationState() == 0)
        			{
            			this.setAnimationState(7);
            			this.setAnimationTick(46);
                		this.setNoGravity(true);
        			}
            		
            		if(this.getAnimationState() == 6)
            		{
            			Vec3 sleepPos = Vec3.atBottomCenterOf(pos);
            			if(sleepPos.subtract(this.getEyePosition()).length() <= 2.0F)
            			{
            				this.setAnimationState(8);
            				this.setAnimationTick(35);
            			}
            			else
            			{
                			this.addDeltaMovement(new Vec3(0.0F, 0.05F, 0.0F));
            			}
            		}
            		
            		if(this.getAnimationTick() <= 0)
            		{
            			if(this.getAnimationState() == 7)
            			{
                			this.setAnimationState(6);
                			this.setAnimationTick(45);
            			}
            			if(this.getAnimationState() == 8)
            			{
                			this.setAnimationState(1);
                			this.setAnimationTick(40);
            			}
            		}
        		}
        		else
        		{
            		this.getNavigation().moveTo(pos.getX(), this.getY(), pos.getZ(), 0.5F);
        		}
        		
        		if(this.getAnimationState() == 1)
        		{
        			this.setDeltaMovement(Vec3.ZERO);
        		}
        		
        		if(this.getAnimationState() == 2 && this.getAnimationTick() <= 0)
        		{
            		this.setHowlerSleeping(false);
        			this.setNoGravity(false);
        			this.setFalling(true);
        			this.setAnimationState(0);
        		}
    		}
    	}
    	if(this.getAnimationState() == 1)
    	{
    		this.setDeltaMovement(Vec3.ZERO);
    	}
    	if(this.isHowlerSleeping() && this.getAnimationState() == 1 && this.getTarget() != null)
    	{
			this.targetTick = 0;
			this.setAnimationState(2);
			this.setAnimationTick(60);
    	}
    }
    
    public double horizontalDist(BlockPos pos, double x, double z) 
    {
    	double xDist = (double)pos.getX() + 0.5D - x;
    	double zDist = (double)pos.getZ() + 0.5D - z;
    	return Math.sqrt(xDist * xDist + zDist * zDist);
    }
    
    @Override
    public void push(double p_20286_, double p_20287_, double p_20288_)
    {
    	if(!this.isHowlerSleeping())
    	{
        	super.push(p_20286_, p_20287_, p_20288_);
    	}
    }
    
    @Override
    public void playAmbientSound() 
    {
    	super.playAmbientSound();
    	if(!this.isHowlerSleeping() && this.ambientTick <= 0)
    	{
			this.ambientTick = 10;
    	}
    }
    
    @Override
    protected void updateWalkAnimation(float p_268283_) 
    {
        float f = Math.min(p_268283_ * 4.0F, 1.0F);
        if(this.isHowlerSleeping())
        {
        	f = 0.0F;
        }
        this.walkAnimation.update(f, 0.4F);
    }
    
    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) 
    {
    	if(this.getAnimationState() == 3 || this.getAnimationState() == 4 || this.getAnimationState() == 5)
    	{
    		p_21017_ *= 0.5F;
    	}
    	if(p_21016_.is(DamageTypeTags.IS_FALL))
    	{
    		return false;
    	}
		if(p_21016_.is(DamageTypes.IN_WALL))
		{
			if(this.isHowlerSleeping() || this.isFalling())
			{
				return false;
			}
		}
    	if(this.isHowlerSleeping() && this.getAnimationState() == 1)
    	{
    		if(p_21016_.getDirectEntity() != null)
    		{
    			this.targetTick = 0;
    			this.setAnimationState(2);
    			this.setAnimationTick(60);
    		}
    	}
    	return super.hurt(p_21016_, p_21017_);
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_)
    {
    	super.readAdditionalSaveData(p_21450_);
    	this.setHowlerSleeping(p_21450_.getBoolean("isHowlerSleeping"));
    	this.setFalling(p_21450_.getBoolean("isFalling"));
    	this.setSleepPos(NbtUtils.readBlockPos(p_21450_.getCompound("SleepPos")));
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) 
    {
    	super.addAdditionalSaveData(p_21484_);
    	p_21484_.putBoolean("isHowlerSleeping", this.isHowlerSleeping());
    	p_21484_.putBoolean("isFalling", this.isFalling());
    	p_21484_.put("SleepPos", NbtUtils.writeBlockPos(this.getSleepPos()));
    }
    
    public void setHowlerSleeping(boolean value)
    {
    	this.entityData.set(IS_SLEEPING, value);
    }
    
    public boolean isHowlerSleeping()
    {
    	return this.entityData.get(IS_SLEEPING);
    }
    
    public void setFalling(boolean value)
    {
    	this.entityData.set(IS_FALLING, value);
    }
    
    public boolean isFalling()
    {
    	return this.entityData.get(IS_FALLING);
    }
    
    public void setSleepPos(BlockPos value)
    {
    	this.entityData.set(SLEEP_POS, value);
    }
    
    public BlockPos getSleepPos()
    {
    	return this.entityData.get(SLEEP_POS);
    }
}