package com.min01.crypticfoes.entity.ai.navigation;

import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class NoSpinGroundPathNavigation extends GroundPathNavigation
{
	public float distanceModifier = 0.75F;
	
	public NoSpinGroundPathNavigation(Mob pMob, Level pLevel)
	{
		super(pMob, pLevel);
	}
	
	public NoSpinGroundPathNavigation(Mob pMob, Level pLevel, float distanceModifier)
	{
		super(pMob, pLevel);
		this.distanceModifier = distanceModifier;
	}
	
	@Override
    protected void followThePath() 
    {
        Vec3 vec3 = this.getTempMobPos();
        this.maxDistanceToWaypoint = this.mob.getBbWidth() * this.distanceModifier;
        Vec3i nodePos = this.path.getNextNodePos();
        double d0 = Math.abs(this.mob.getX() - ((double) nodePos.getX() + 0.5D));
        //double d1 = Math.abs(this.mob.getY() - (double) nodePos.getY());
        double d2 = Math.abs(this.mob.getZ() - ((double) nodePos.getZ() + 0.5D));
        boolean flag = d0 < this.maxDistanceToWaypoint && d2 < this.maxDistanceToWaypoint;
        if(flag || this.canCutCorner(this.path.getNextNode().type) && this.shouldTargetNextNodeInDirection(vec3))
        {
            this.path.advance();
        }
        this.doStuckDetection(vec3);
    }

    public boolean shouldTargetNextNodeInDirection(Vec3 currentPosition) 
    {
        if(this.path.getNextNodeIndex() + 1 >= this.path.getNodeCount()) 
        {
            return false;
        } 
        else
        {
            Vec3 vec3 = Vec3.atBottomCenterOf(this.path.getNextNodePos());
            if(!currentPosition.closerThan(vec3, 2.0D)) 
            {
                return false;
            }
            else 
            {
                Vec3 vec31 = Vec3.atBottomCenterOf(this.path.getNodePos(this.path.getNextNodeIndex() + 1));
                Vec3 vec32 = vec31.subtract(vec3);
                Vec3 vec33 = currentPosition.subtract(vec3);
                return vec32.dot(vec33) > 0.0D;
            }
        }
    }
}
