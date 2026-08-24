package com.min01.crypticfoes.api.ai.control;

import org.joml.Vector2f;

import com.min01.crypticfoes.api.entity.IAnimatable;
import com.min01.crypticfoes.api.entity.MoveProperty;
import com.min01.crypticfoes.api.entity.MoveProperty.MoveState;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;

public class AnimationFlyingMoveControl<T extends Mob & IAnimatable> extends FlyingMoveControl 
{
	protected final T mob;
	protected final MoveProperty property;
	protected final boolean hoversInPlace;

	public AnimationFlyingMoveControl(T pMob)
	{
		this(pMob, 10, false);
	}

	public AnimationFlyingMoveControl(T pMob, int pMaxTurn, boolean pHoversInPlace) 
	{
		super(pMob, pMaxTurn, pHoversInPlace);
		this.hoversInPlace = pHoversInPlace;
		this.mob = pMob;
		this.property = pMob.getMoveProperty();
	}

	//copy of SmoothSwimmingMoveControl
	@Override
	public void tick() 
	{
		Vector2f turn = this.property.turn(MoveState.FLY);
		if(this.operation == MoveControl.Operation.MOVE_TO && !this.mob.getNavigation().isDone()) 
		{
			double d0 = this.wantedX - this.mob.getX();
			double d1 = this.wantedY - this.mob.getY();
			double d2 = this.wantedZ - this.mob.getZ();
			double d3 = d0 * d0 + d1 * d1 + d2 * d2;
			if(d3 < (double) 2.5000003E-7F) 
			{
				this.mob.setZza(0.0F);
			}
			else 
			{
				float f = (float) (Mth.atan2(d2, d0) * (180.0F / (float) Math.PI)) - 90.0F;
				this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f, turn.y));
				this.mob.yBodyRot = this.mob.getYRot();
				this.mob.yHeadRot = this.mob.getYRot();
				float f1 = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.FLYING_SPEED));
				this.mob.setSpeed((float) (f1 * this.speedModifier));
				double d4 = Math.sqrt(d0 * d0 + d2 * d2);
				if(Math.abs(d1) > 1.0E-5F || Math.abs(d4) > 1.0E-5F) 
				{
					float f3 = -((float) (Mth.atan2(d1, d4) * (180.0F / Math.PI)));
					f3 = Mth.clamp(Mth.wrapDegrees(f3), -turn.x, turn.x);
					this.mob.setXRot(this.rotlerp(this.mob.getXRot(), f3, 5.0F));
				}
				float f6 = Mth.cos(this.mob.getXRot() * ((float) Math.PI / 180.0F));
				float f4 = Mth.sin(this.mob.getXRot() * ((float) Math.PI / 180.0F));
				this.mob.zza = f6 * f1;
				this.mob.yya = -f4 * f1;
			}
		} 
		else 
		{
			this.mob.setSpeed(0.0F);
			this.mob.setXxa(0.0F);
			this.mob.setYya(0.0F);
			this.mob.setZza(0.0F);
		}
	}
}