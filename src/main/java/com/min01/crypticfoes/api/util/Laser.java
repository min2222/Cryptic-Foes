package com.min01.crypticfoes.api.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

//https://github.com/BobMowzie/MowziesMobs-Public/blob/1.20/src/main/java/com/bobmowzie/mowziesmobs/server/entity/effects/EntitySolarBeam.java
public class Laser
{
    public static LaserHitResult raytrace(Level world, Vec3 from, Vec3 to, double radius, Predicate<? super Entity> predicate, Entity entity) 
    {
    	return raytrace(world, from, to, radius, predicate, false, entity);
    }
    
    public static LaserHitResult raytrace(Level world, Vec3 from, Vec3 to, double radius, Predicate<? super Entity> predicate, boolean ignoreBlocks, Entity entity) 
    {
    	BlockHitResult blockHit = world.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
    	Vec3 collidePos = !ignoreBlocks ? blockHit.getLocation() : to;
    	LaserHitResult result = new LaserHitResult(blockHit, collidePos);
        List<LivingEntity> list = world.getEntitiesOfClass(LivingEntity.class, new AABB(from, result.collidePos).inflate(radius), predicate);
        for(LivingEntity living : list)
        {
            AABB aabb = living.getBoundingBox().inflate(living.getPickRadius() + radius);
            Optional<Vec3> hit = aabb.clip(from, to);
            if(aabb.contains(from))
            {
                result.addEntityHit(living);
            }
            else if(hit.isPresent())
            {
                result.addEntityHit(living);
            }
        }
        return result;
    }
	
    public static class LaserHitResult
    {
    	public final List<LivingEntity> entities = new ArrayList<>();
    	public final BlockHitResult blockHit;
    	public final Vec3 collidePos;
    	
    	public LaserHitResult(BlockHitResult blockHit, Vec3 collidePos)
    	{
    		this.blockHit = blockHit;
    		this.collidePos = collidePos;
		}

        public static float getLaserLength(Vec3 pos)
        {
            return (float) Math.sqrt(Math.pow(pos.x, 2) + Math.pow(pos.y, 2) + Math.pow(pos.z, 2));
        }
        
        public float getLaserLength()
        {
            return getLaserLength(this.collidePos);
        }

        public void addEntityHit(LivingEntity entity) 
        {
            this.entities.add(entity);
        }
    }
}
