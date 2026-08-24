package com.min01.crypticfoes.api.ai.control;

import com.min01.crypticfoes.api.entity.IAnimatable;
import com.min01.crypticfoes.api.entity.MoveProperty;
import com.min01.crypticfoes.api.entity.MoveProperty.MoveState;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;

public class AnimationMoveControl<T extends Mob & IAnimatable> extends MoveControl
{
	protected final T mob;
	protected final MoveProperty property;
	
	public AnimationMoveControl(T pMob) 
	{
		super(pMob);
		this.mob = pMob;
		this.property = pMob.getMoveProperty();
	}

	@Override
	protected float rotlerp(float pSourceAngle, float pTargetAngle, float pMaximumChange) 
	{
		return super.rotlerp(pSourceAngle, pTargetAngle, this.property.turn(MoveState.WALK).y);
	}
}
