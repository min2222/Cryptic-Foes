package com.min01.crypticfoes.api.ai.control;

import org.joml.Vector2f;

import com.min01.crypticfoes.api.entity.IAnimatable;
import com.min01.crypticfoes.api.entity.MoveProperty;
import com.min01.crypticfoes.api.entity.MoveProperty.MoveState;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;

public class AnimationSwimmingMoveControl<T extends Mob & IAnimatable> extends SmoothSwimmingMoveControl 
{
	protected final T mob;
	protected final MoveProperty property;
	
	public AnimationSwimmingMoveControl(T pMob)
	{
		this(pMob, 1.0F, false);
	}
	
	public AnimationSwimmingMoveControl(T pMob, float pInWaterSpeedModifier, boolean pApplyGravity)
	{
		super(pMob, 85, 10, pInWaterSpeedModifier, 1.0F, pApplyGravity);
		this.mob = pMob;
		this.property = pMob.getMoveProperty();
	}

	@Override
	public void tick()
	{
		Vector2f turn = this.property.turn(MoveState.SWIM);
		this.maxTurnX = (int) turn.x;
		this.maxTurnY = (int) turn.y;
		super.tick();
	}
}