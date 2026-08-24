package com.min01.crypticfoes.api.ai.control;

import com.min01.crypticfoes.api.entity.IAnimatable;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;

public class AnimationLookControl<T extends Mob & IAnimatable> extends LookControl
{
	protected final T mob;
	
	public AnimationLookControl(T pMob) 
	{
		super(pMob);
		this.mob = pMob;
	}

	@Override
	protected boolean resetXRotOnTick() 
	{
		return this.mob.resetXRotOnTick();
	}
}
