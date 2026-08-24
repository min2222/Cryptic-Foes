package com.min01.crypticfoes.api.ai.goal;

import org.joml.Vector2i;

import com.min01.crypticfoes.api.entity.IAnimatable;
import com.min01.crypticfoes.api.entity.MoveProperty;
import com.min01.crypticfoes.api.entity.MoveProperty.MoveState;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

public class AnimationRandomStrollGoal<T extends PathfinderMob & IAnimatable> extends RandomStrollGoal
{
	protected final T mob;
	protected final MoveProperty property;
	
	public AnimationRandomStrollGoal(T pMob, double pSpeedModifier, int pInterval, boolean pCheckNoActionTime)
	{
		super(pMob, pSpeedModifier, pInterval, pCheckNoActionTime);
		this.mob = pMob;
		this.property = this.mob.getMoveProperty();
	}
	
	@Override
	public boolean canUse() 
	{
		return super.canUse() && this.mob.canMoveAround();
	}
	
	@Override
	protected Vec3 getPosition()
	{
		MoveState state = this.mob.getMoveState();
		Vector2i radius = this.property.radius(state);
		if(state == MoveState.SWIM)
		{
			//copied from RandomSwimmingGoal;
			return BehaviorUtils.getRandomSwimmablePos(this.mob, radius.x, radius.y);
		}
		if(state == MoveState.FLY)
		{
			//copied from WaterAvoidingRandomFlyingGoal;
			Vec3 view = this.mob.getViewVector(0.0F);
			Vec3 randomPos = HoverRandomPos.getPos(this.mob, radius.x, radius.y, view.x, view.z, (float) (Math.PI / 2.0F), 3, 1);
			return randomPos != null ? randomPos : AirAndWaterRandomPos.getPos(this.mob, radius.x, radius.y, -2, view.x, view.z, (Math.PI / 2.0F));
		}
		//copied from WaterAvoidingRandomStrollGoal;
		if(this.mob.isInWaterOrBubble())
		{
			Vec3 vec3 = LandRandomPos.getPos(this.mob, radius.x, radius.y);
			return vec3 == null ? super.getPosition() : vec3;
		} 
		else
		{
			return this.mob.getRandom().nextFloat() >= 0.001F ? LandRandomPos.getPos(this.mob, radius.x, radius.y) : super.getPosition();
		}
	}
}
