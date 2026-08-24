package com.min01.crypticfoes.api.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;

//https://github.com/BobMowzie/MowziesMobs-Public/blob/1.20/src/main/java/com/bobmowzie/mowziesmobs/server/ai/MMPathNavigateGround.java
public class NoSpinPathNavigation
{
	public static final float EPSILON = 1.0E-8F;
	
	public static void followThePath(Mob mob, NodeEvaluator nodeEvaluator, Path path, Vec3 entityPos, PathComputationType pathComputationType, float distanceModifier)
	{
        int nodeCount = path.getNodeCount();
        for(int i = path.getNextNodeIndex(); i < path.getNodeCount(); i++)
        {
            if(path.getNode(i).y != Math.floor(entityPos.y)) 
            {
            	nodeCount = i;
                break;
            }
        }
        Vec3 base = entityPos.add(-mob.getBbWidth() * distanceModifier, 0.0F, -mob.getBbWidth() * distanceModifier);
        Vec3 max = base.add(mob.getBbWidth(), mob.getBbHeight(), mob.getBbWidth());
        if(tryShortcut(mob, nodeEvaluator, path, mob.position(), nodeCount, base, max, pathComputationType)) 
        {
            if(isAt(mob, path, distanceModifier) || atElevationChange(mob, path, distanceModifier) && isAt(mob, path, mob.getBbWidth() * distanceModifier)) 
            {
                path.setNextNodeIndex(path.getNextNodeIndex() + 1);
            }
        }
	}
	
    public static boolean isAt(Mob mob, Path path, float threshold) 
    {
        final Vec3 pathPos = path.getNextEntityPos(mob);
        return Mth.abs((float) (mob.getX() - pathPos.x)) < threshold && Mth.abs((float) (mob.getZ() - pathPos.z)) < threshold;
    }

    public static boolean atElevationChange(Mob mob, Path path, float distanceModifier) 
    {
        int current = path.getNextNodeIndex();
        int end = Math.min(path.getNodeCount(), current + Mth.ceil(mob.getBbWidth() * distanceModifier) + 1);
        int currentY = path.getNode(current).y;
        for(int i = current + 1; i < end; i++) 
        {
            if(path.getNode(i).y != currentY) 
            {
                return true;
            }
        }
        return false;
    }

    public static boolean tryShortcut(Mob mob, NodeEvaluator nodeEvaluator, Path path, Vec3 entityPos, int pathLength, Vec3 base, Vec3 max, PathComputationType pathComputationType) 
    {
        for(int i = pathLength; --i > path.getNextNodeIndex();)
        {
            Vec3 vec3 = path.getEntityPosAtNode(mob, i).subtract(entityPos);
            if(sweep(mob, nodeEvaluator, vec3, base, max, pathComputationType))
            {
                path.setNextNodeIndex(i);
                return false;
            }
        }
        return true;
    }

    public static boolean sweep(Mob mob, NodeEvaluator nodeEvaluator, Vec3 vec3, Vec3 base, Vec3 max, PathComputationType pathComputationType)
    {
        float t = 0.0F;
        float max_t = (float) vec3.length();
        if(max_t < EPSILON) 
        {
        	return true;
        }
        float[] tr = new float[3];
        int[] ldi = new int[3];
        int[] tri = new int[3];
        int[] step = new int[3];
        float[] tDelta = new float[3];
        float[] tNext = new float[3];
        float[] normed = new float[3];
        for(int i = 0; i < 3; i++) 
        {
            float value = element(vec3, i);
            boolean dir = value >= 0.0F;
            step[i] = dir ? 1 : -1;
            float lead = element(dir ? max : base, i);
            tr[i] = element(dir ? base : max, i);
            ldi[i] = leadEdgeToInt(lead, step[i]);
            tri[i] = trailEdgeToInt(tr[i], step[i]);
            normed[i] = value / max_t;
            tDelta[i] = Mth.abs(max_t / value);
            float dist = dir ? (ldi[i] + 1 - lead) : (lead - ldi[i]);
            tNext[i] = tDelta[i] < Float.POSITIVE_INFINITY ? tDelta[i] * dist : Float.POSITIVE_INFINITY;
        }
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        do 
        {
            int axis = (tNext[0] < tNext[1]) ? ((tNext[0] < tNext[2]) ? 0 : 2) : ((tNext[1] < tNext[2]) ? 1 : 2);
            float dt = tNext[axis] - t;
            t = tNext[axis];
            ldi[axis] += step[axis];
            tNext[axis] += tDelta[axis];
            for(int i = 0; i < 3; i++)
            {
                tr[i] += dt * normed[i];
                tri[i] = trailEdgeToInt(tr[i], step[i]);
            }
            int stepx = step[0];
            int x0 = (axis == 0) ? ldi[0] : tri[0];
            int x1 = ldi[0] + stepx;
            int stepy = step[1];
            int y0 = (axis == 1) ? ldi[1] : tri[1];
            int y1 = ldi[1] + stepy;
            int stepz = step[2];
            int z0 = (axis == 2) ? ldi[2] : tri[2];
            int z1 = ldi[2] + stepz;
            for(int x = x0; x != x1; x += stepx) 
            {
                for(int z = z0; z != z1; z += stepz)
                {
                    for(int y = y0; y != y1; y += stepy) 
                    {
                        BlockState block = mob.level.getBlockState(pos.set(x, y, z));
                        if(!block.isPathfindable(mob.level, pos, pathComputationType))
                        {
                        	return false;
                        }
                    }
                    BlockPathTypes below = nodeEvaluator.getBlockPathType(mob.level, x, y0 - 1, z);
                    BlockPathTypes in = nodeEvaluator.getBlockPathType(mob.level, x, y0, z, mob);
                    float priority = mob.getPathfindingMalus(in);
                    if(priority < 0.0F || priority >= 8.0F)
                    {
                    	return false;
                    }
                    if(!mob.getType().fireImmune())
                    {
                        if(in == BlockPathTypes.DAMAGE_FIRE || in == BlockPathTypes.DANGER_FIRE || in == BlockPathTypes.DAMAGE_OTHER || below == BlockPathTypes.LAVA) 
                        {
                        	return false;
                        }
                    }
                }
            }
        } 
        while(t <= max_t);
        return true;
    }

    public static int leadEdgeToInt(float coord, int step) 
    {
        return Mth.floor(coord - step * EPSILON);
    }

    public static int trailEdgeToInt(float coord, int step) 
    {
        return Mth.floor(coord + step * EPSILON);
    }

    public static float element(Vec3 vec3, int i)
    {
        switch(i) 
        {
            case 0:
            	return (float) vec3.x;
            case 1:
            	return (float) vec3.y;
            case 2:
            	return (float) vec3.z;
            default:
            	 return 0.0F;
        }
    }
}
