package com.min01.crypticfoes.entity.ai.navigation;

import com.min01.crypticfoes.api.ai.navigation.NoSpinPathNavigation;
import com.min01.crypticfoes.api.ai.navigation.PatchedPathFinder;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.SwimNodeEvaluator;
import net.minecraft.world.phys.Vec3;

public class NoSpinWaterBoundPathNavigation extends WaterBoundPathNavigation
{
	public boolean allowBreaching;
    public float distanceModifier = 0.5F;
    
    public NoSpinWaterBoundPathNavigation(Mob entity, Level world) 
    {
        super(entity, world);
    }
    
    public NoSpinWaterBoundPathNavigation(Mob entity, Level world, float distanceModifier) 
    {
        super(entity, world);
        this.distanceModifier = distanceModifier;
    }

    @Override
    protected PathFinder createPathFinder(int maxVisitedNodes)
    {
		this.allowBreaching = true;
		this.nodeEvaluator = new SwimNodeEvaluator(this.allowBreaching);
		return new PatchedPathFinder(this.nodeEvaluator, maxVisitedNodes);
    }
    
    @Override
    public void setCanFloat(boolean pCanSwim)
    {
    	this.nodeEvaluator.setCanFloat(pCanSwim);
    }

    @Override
    protected void followThePath() 
    {
        Vec3 entityPos = this.getTempMobPos();
        NoSpinPathNavigation.followThePath(this.mob, this.nodeEvaluator, this.path, entityPos, PathComputationType.WATER, this.distanceModifier);
        this.doStuckDetection(entityPos);
    }

}