package com.min01.crypticfoes.entity;

import com.min01.crypticfoes.misc.ModelPartPositions;
import com.min01.crypticfoes.misc.MovementData;

public interface IAnimatable
{
	void setAnimationPlaying(boolean value);
	
	boolean isAnimationPlaying();
	
	void setAnimationTick(int value);
	
	int getAnimationTick();
	
	void setStopMoveTick(int value);
	
	int getStopMoveTick();
	
	boolean canMove();
	
	void setStopLookTick(int value);
	
	int getStopLookTick();
	
	boolean canLook();
	
	void moveToTarget();

	void lookAtTarget();
	
	ModelPartPositions getModelPositions();
	
	MovementData getMovementData();
}
