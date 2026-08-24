package com.min01.crypticfoes.api.entity;

import com.min01.crypticfoes.api.ai.control.AnimationFlyingMoveControl;
import com.min01.crypticfoes.api.ai.control.AnimationLookControl;
import com.min01.crypticfoes.api.ai.control.AnimationMoveControl;
import com.min01.crypticfoes.api.ai.control.AnimationSwimmingMoveControl;
import com.min01.crypticfoes.api.ai.goal.AnimationLookAtPlayerGoal;
import com.min01.crypticfoes.api.ai.goal.AnimationRandomLookAroundGoal;
import com.min01.crypticfoes.api.ai.goal.AnimationRandomStrollGoal;
import com.min01.crypticfoes.api.animation.AnimationEntries;
import com.min01.crypticfoes.api.client.ModelPartPos;
import com.min01.crypticfoes.api.entity.MoveProperty.MoveState;
import com.min01.crypticfoes.entity.ai.navigation.NoSpinFlyingPathNavigation;
import com.min01.crypticfoes.entity.ai.navigation.NoSpinGroundPathNavigation;
import com.min01.crypticfoes.entity.ai.navigation.NoSpinWaterBoundPathNavigation;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;

public abstract class AbstractAnimatableCreature extends PathfinderMob implements IAnimatable
{
	private static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(AbstractAnimatableCreature.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> ANIMATION_TICK = SynchedEntityData.defineId(AbstractAnimatableCreature.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> MOVEMENT_STATE = SynchedEntityData.defineId(AbstractAnimatableCreature.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> IS_TARGET_VALID = SynchedEntityData.defineId(AbstractAnimatableCreature.class, EntityDataSerializers.BOOLEAN);

	public final ModelPartPos modelPartPos = new ModelPartPos();
	public final AnimationEntries animationEntries = new AnimationEntries();
	public MoveProperty property;
	
	public AnimationRandomStrollGoal<AbstractAnimatableCreature> randomStrollGoal;
	
	public AbstractAnimatableCreature(EntityType<? extends PathfinderMob> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
		this.init(this.getMobClass());
	}
	
	@Override
	protected void defineSynchedData() 
	{
		super.defineSynchedData();
		this.entityData.define(ANIMATION_STATE, 0);
		this.entityData.define(ANIMATION_TICK, 0);
		this.entityData.define(MOVEMENT_STATE, 0);
		this.entityData.define(IS_TARGET_VALID, false);
	}
	
	@Override
	protected void registerGoals()
	{
		this.property = this.createMoveProperty();
		this.randomStrollGoal = new AnimationRandomStrollGoal<>(this, 1.0F, 120, false);
		
		this.goalSelector.addGoal(0, this.randomStrollGoal);
		this.goalSelector.addGoal(0, new AnimationRandomLookAroundGoal<>(this));
		this.goalSelector.addGoal(0, new AnimationLookAtPlayerGoal<>(this, Player.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(0, new AnimationLookAtPlayerGoal<>(this, Mob.class, 8.0F));
	}
	
    public void init(MobClass mobClass)
    {
    	switch(mobClass)
    	{
		case LAND:
			this.setMoveState(MoveState.WALK);
			break;
		case WATER:
			this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
			this.setMoveState(MoveState.SWIM);
			break;
		case AIR:
			this.setMoveState(MoveState.FLY);
			break;
    	}
    }
    
    public void switchControl(MoveState state)
    {
		if(state == MoveState.WALK)
		{
			if(!(this.navigation instanceof NoSpinGroundPathNavigation))
			{
				this.moveControl = new AnimationMoveControl<>(this);
				this.lookControl = new AnimationLookControl<>(this);
				this.navigation = new NoSpinGroundPathNavigation(this, this.level);
				this.setNoGravity(false);
			}
		}
		if(state == MoveState.SWIM)
    	{
			if(!(this.navigation instanceof NoSpinWaterBoundPathNavigation))
			{
				this.moveControl = new AnimationSwimmingMoveControl<>(this);
				this.lookControl = new AnimationLookControl<>(this);
				this.navigation = new NoSpinWaterBoundPathNavigation(this, this.level);
			}
    	}
		if(state == MoveState.FLY)
    	{
			if(!(this.navigation instanceof NoSpinFlyingPathNavigation))
			{
				this.moveControl = new AnimationFlyingMoveControl<>(this);
				this.lookControl = new AnimationLookControl<>(this);
				this.navigation = new NoSpinFlyingPathNavigation(this, this.level);
				this.setNoGravity(true);
			}
    	}
    }
	
    @Override
    public void travel(Vec3 pTravelVector) 
    {
    	MoveState state = this.getMoveState();
		if(this.isInWater() && state == MoveState.SWIM)
		{
			if(this.travel(pTravelVector, ForgeMod.SWIM_SPEED.get()))
			{
				return;
			}
		}
		if(state == MoveState.FLY)
		{
			if(this.travel(pTravelVector, Attributes.FLYING_SPEED))
			{
				return;
			}
		}
		super.travel(pTravelVector);
    }
    
    public boolean travel(Vec3 pTravelVector, Attribute speedAttribute)
    {
    	if(this.isEffectiveAi())
    	{
    		this.moveRelative((float) (this.getSpeed() * this.getAttributeValue(speedAttribute)), pTravelVector);
    		this.move(MoverType.SELF, this.getDeltaMovement());
    		this.setDeltaMovement(this.getDeltaMovement().scale(0.9F));
    	}
    	this.calculateEntityAnimation(true);
    	return true;
    }
    
    @Override
    public void tick()
    {
    	super.tick();
    	
		this.setAnimationTick(this.getAnimationTick() - 1);
		
		if(this.getAnimationState() != 0 && this.getAnimationTick() <= 0)
		{
			if(this.onAnimationEnd(this.getAnimationState()))
			{
				this.setAnimationState(0);
			}
		}
		
		if(!this.level.isClientSide)
		{
			this.setTargetValid(this.getTarget() != null && this.getTarget().isAlive());
		}
    }
    
    @Override
    protected void customServerAiStep() 
    {
    	super.customServerAiStep();
		if(this.randomStrollGoal != null)
		{
			this.randomStrollGoal.setInterval(this.property.interval(this.getMoveState()));
		}
    }
    
	@Override
	public void baseTick() 
	{
		int i = this.getAirSupply();
		super.baseTick();
		if(this.getMobClass() == MobClass.WATER)
		{
			this.handleAirSupply(i);
		}
	}
	
	public void handleAirSupply(int pAirSupply) 
	{
		if(this.isAlive() && !this.isInWaterOrBubble())
		{
			this.setAirSupply(pAirSupply - 1);
			if(this.getAirSupply() == -20)
			{
				this.setAirSupply(0);
				this.hurt(this.damageSources().drown(), 2.0F);
			}
		} 
		else 
		{
			this.setAirSupply(300);
		}
	}
	
	@Override
	public boolean canBreatheUnderwater() 
	{
		return this.getMobClass() == MobClass.WATER;
	}

	@Override
	public MobType getMobType()
	{
		return this.getMobClass() == MobClass.WATER ? MobType.WATER : super.getMobType();
	}

	@Override
	public boolean checkSpawnObstruction(LevelReader pLevel)
	{
		if(this.getMobClass() == MobClass.WATER)
		{
			return pLevel.isUnobstructed(this);
		}
		return super.checkSpawnObstruction(pLevel);
	}
	
	@Override
	protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos)
	{
		if(this.getMobClass() != MobClass.AIR)
		{
			super.checkFallDamage(pY, pOnGround, pState, pPos);
		}
	}
	
	@Override
	protected void playStepSound(BlockPos pPos, BlockState pState) 
	{
		if(this.getMoveState() != MoveState.SWIM)
		{
			super.playStepSound(pPos, pState);
		}
	}
    
    @Override
    public boolean isPushedByFluid(FluidType type)
    {
		if(this.getMobClass() == MobClass.WATER)
		{
			return false;
		}
    	return super.isPushedByFluid(type);
    }
    
	@Override
	public boolean onClimbable()
	{
		if(this.getMobClass() == MobClass.AIR)
		{
			return false;
		}
		return super.onClimbable();
	}
	
    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) 
    {
    	super.readAdditionalSaveData(pCompound);
    	this.setMoveStateId(pCompound.getInt("MoveState"));
    	this.setAnimationTick(pCompound.getInt("AnimationTick"));
    	this.setAnimationState(pCompound.getInt("AnimationState"));
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) 
    {
    	super.addAdditionalSaveData(pCompound);
    	pCompound.putInt("MoveState", this.getMoveStateId());
    	pCompound.putInt("AnimationTick", this.getAnimationTick());
    	pCompound.putInt("AnimationState", this.getAnimationState());
    }
	
	/*@Override
	public int getMaxHeadYRot() 
	{
		return this.property.bodyTurnY();
	}*/
	
	public void setTargetValid(boolean value)
	{
		this.entityData.set(IS_TARGET_VALID, value);
	}
	
	public boolean isTargetValid()
	{
		return this.entityData.get(IS_TARGET_VALID);
	}
	
	public boolean isAnimationPlaying(int state)
	{
		return this.getAnimationState() == state && this.isAnimationPlaying();
	}

	public void setAnimationState(int state)
	{
		this.entityData.set(ANIMATION_STATE, state);
	}
	
	public int getAnimationState()
	{
		return this.entityData.get(ANIMATION_STATE);
	}
	
	@Override
	public void setAnimationTick(int tick) 
	{
		this.entityData.set(ANIMATION_TICK, tick);
	}
	
	@Override
	public int getAnimationTick() 
	{
		return this.entityData.get(ANIMATION_TICK);
	}
	
	public void setMoveStateId(int id)
	{
		this.entityData.set(MOVEMENT_STATE, id);
	}
	
	public void setMoveState(MoveState state)
	{
		this.setMoveStateId(state.ordinal());
		this.switchControl(state);
	}
	
	public int getMoveStateId()
	{
		return this.entityData.get(MOVEMENT_STATE);
	}

	@Override
	public MoveState getMoveState()
	{
		return MoveState.values()[this.getMoveStateId()];
	}
	
	@Override
	public MoveProperty getMoveProperty() 
	{
		return this.property;
	}
	
	@Override
	public ModelPartPos getModelPartPos()
	{
		return this.modelPartPos;
	}
}