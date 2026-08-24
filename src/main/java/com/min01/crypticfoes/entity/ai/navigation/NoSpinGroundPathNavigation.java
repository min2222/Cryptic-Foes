package com.min01.crypticfoes.entity.ai.navigation;

import com.min01.crypticfoes.api.ai.navigation.NoSpinPathNavigation;
import com.min01.crypticfoes.api.ai.navigation.PatchedPathFinder;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;

public class NoSpinGroundPathNavigation extends GroundPathNavigation
{
	public static final float EPSILON = 1.0E-8F;
    public float distanceModifier = 0.5F;
    
    public NoSpinGroundPathNavigation(Mob entity, Level world) 
    {
        super(entity, world);
    }
    
    public NoSpinGroundPathNavigation(Mob entity, Level world, float distanceModifier) 
    {
        super(entity, world);
        this.distanceModifier = distanceModifier;
    }

    @Override
    protected PathFinder createPathFinder(int maxVisitedNodes)
    {
        this.nodeEvaluator = new WalkNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        return new PatchedPathFinder(this.nodeEvaluator, maxVisitedNodes);
    }

    @Override
    protected void followThePath() 
    {
        Vec3 entityPos = this.getTempMobPos();
        NoSpinPathNavigation.followThePath(this.mob, this.nodeEvaluator, this.path, entityPos, PathComputationType.LAND, this.distanceModifier);
        this.doStuckDetection(entityPos);
    }
}