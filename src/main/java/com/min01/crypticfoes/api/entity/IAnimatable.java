package com.min01.crypticfoes.api.entity;

import com.min01.crypticfoes.api.client.ModelPartPos;
import com.min01.crypticfoes.api.entity.MoveProperty.MoveState;

import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.ForgeMod;

public interface IAnimatable
{
	default Mob mob()
	{
		return (Mob) this;
	}
	
	default boolean resetXRotOnTick()
	{
		if(this.getMoveState() == MoveState.SWIM)
		{
			return !this.mob().isEyeInFluidType(ForgeMod.EMPTY_TYPE.get());
		}
		if(this.getMoveState() == MoveState.FLY)
		{
			return false;
		}
		return true;
	}
	
	default boolean onAnimationEnd(int animationState)
    {
    	return true;
    }
	
	default boolean isAnimationPlaying()
	{
		return this.getAnimationTick() > 0;
	}
	
	default boolean canMove()
	{
		return !this.isAnimationPlaying();
	}
	
	default boolean canMoveAround()
	{
		return this.canMove() && !this.isAnimationPlaying() && (this.mob().getTarget() == null || !this.mob().getTarget().isAlive());
	}
	
	default boolean canLook()
	{
		return true;
	}
	
	default boolean canLookAround()
	{
		return this.canLook() && !this.isAnimationPlaying() && (this.mob().getTarget() == null || !this.mob().getTarget().isAlive());
	}

	void setAnimationTick(int tick);

	void setMoveState(MoveState state);
	
	int getAnimationTick();
	
	MoveState getMoveState();
	
	MoveProperty getMoveProperty();
	
	ModelPartPos getModelPartPos();
	
	default void moveToTarget()
	{
		this.mob().getNavigation().moveTo(this.mob().getTarget(), 1.0F);
	}

	default void lookAtTarget()
	{
		this.mob().getLookControl().setLookAt(this.mob().getTarget(), 30.0F, 30.0F);
	}
	
	default MoveProperty createMoveProperty()
	{
		return new MoveProperty.Builder().build();
	}
	
	default MobClass getMobClass()
	{
		return MobClass.LAND;
	}
}
