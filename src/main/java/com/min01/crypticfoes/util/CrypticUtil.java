package com.min01.crypticfoes.util;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.joml.Math;
import org.joml.Vector3d;

import com.google.common.base.Predicate;
import com.min01.crypticfoes.api.util.PositionTypes;
import com.min01.crypticfoes.mixin.LevelInvoker;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public class CrypticUtil 
{
	public static void getClientLevel(Consumer<Level> consumer)
	{
		LogicalSidedProvider.CLIENTWORLD.get(LogicalSide.CLIENT).filter(ClientLevel.class::isInstance).ifPresent(level -> 
		{
			consumer.accept(level);
		});
	}
	
	public static void handlePacket(Supplier<NetworkEvent.Context> supplier, LogicalSide side, Consumer<NetworkEvent.Context> consumer)
	{
		NetworkEvent.Context ctx = supplier.get();
		ctx.enqueueWork(() ->
		{
			NetworkDirection direction = ctx.getDirection();
			LogicalSide receptionSide = direction.getReceptionSide();
			if(side.isClient() && !receptionSide.isClient())
			{
				return;
			}
			if(side.isServer() && !receptionSide.isServer())
			{
				return;
			}
			consumer.accept(ctx);
		});
		ctx.setPacketHandled(true);
	}
	
	public static LevelEntityGetter<Entity> getEntityGetter(Level level)
	{
		return ((LevelInvoker) level).crypticfoes$invoke_getEntities();
	}
	
	public static Entity getEntityByUUID(Level level, UUID uuid)
	{
		LevelEntityGetter<Entity> getter = getEntityGetter(level);
		return getter.get(uuid);
	}
	
	public static void flop(LivingEntity entity)
	{
		flop(entity, SoundEvents.COD_FLOP, 1.0F, entity.getVoicePitch(), 0.5F);
	}
	
	//copied from AbstractFish
	public static void flop(LivingEntity entity, SoundEvent sound, float volume, float pitch, float yMotion)
	{
        if(!entity.isInWater() && entity.onGround() && entity.verticalCollision) 
        {
        	RandomSource random = entity.getRandom();
        	Vec3 motion = new Vec3((random.nextFloat() * 2.0F - 1.0F) * 0.05F, yMotion, (random.nextFloat() * 2.0F - 1.0F) * 0.05F);
        	entity.addDeltaMovement(motion);
        	entity.setOnGround(false);
        	entity.hasImpulse = true;
        	entity.playSound(sound, volume, pitch);
        }
	}
	
    public static void runAway(PathfinderMob mob, Vec3 pos)
    {
    	runAway(mob, pos, DefaultRandomPos.getPosAway(mob, 16, 7, pos), 2.0F, mob instanceof TamableAnimal animal ? !animal.isInSittingPose() : true);
    }
	
    public static void runAway(PathfinderMob mob, Vec3 pos, Vec3 awayPos, float speed, boolean canRun)
    {
    	if(canRun)
    	{
            mob.setTarget(null);
            mob.setLastHurtByMob(null);
            if(awayPos != null)
            {
                mob.getNavigation().moveTo(awayPos.x, awayPos.y, awayPos.z, speed);
            }
    	}
    }

    //copied from LivingEntity;
	public static boolean isDamageSourceBlocked(DamageSource source, LivingEntity living, Predicate<Entity> predicate, Predicate<Item> itemPredicate) 
	{
		Entity entity = source.getDirectEntity();
		if(entity instanceof AbstractArrow arrow && arrow.getPierceLevel() > 0) 
		{
			return false;
		}
		if(predicate.test(entity) && !source.is(DamageTypeTags.BYPASSES_SHIELD) && isBlocking(living, itemPredicate))
		{
			Vec3 sourcePosition = source.getSourcePosition();
			if(sourcePosition != null)
			{
				Vec3 view = living.getViewVector(1.0F);
				Vec3 to = sourcePosition.vectorTo(living.position()).normalize();
				to = new Vec3(to.x, 0.0D, to.z);
				if(to.dot(view) < 0.0D)
				{
					return true;
				}
			}
		}
		return false;
	}
    
    //copied from LivingEntity;
	public static boolean isBlocking(LivingEntity living, Predicate<Item> predicate)
	{
		ItemStack stack = living.getUseItem();
		if(living.isUsingItem() && !stack.isEmpty()) 
		{
			Item item = stack.getItem();
			if(!predicate.test(item)) 
			{
				return false;
			}
			else
			{
				return item.getUseDuration(stack) - living.getUseItemRemainingTicks() >= 5;
			}
		}
		else
		{
			return false;
		}
	}
	
	public static boolean checkWaterSpawnRules(EntityType<?> pType, ServerLevelAccessor pServerLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) 
    {
		return pServerLevel.getBlockState(pPos.below()).is(Blocks.WATER) && pServerLevel.getBlockState(pPos.above()).is(Blocks.WATER);
    }
	
	//call in hurt method;
	public static void cancelWalkAnim(LivingEntity entity)
	{
    	if(!isMoving(entity))
    	{
    		entity.walkAnimation.setSpeed(0.0F);
    	}
	}
	
    public static boolean isMoving(Entity entity) 
    {
		return entity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D;
    }
	
	public static boolean isDone(ServerPlayer player, String name)
	{
		Advancement advancement = player.server.getAdvancements().getAdvancement(ResourceLocation.parse(name));
		AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
		return progress.isDone();
	}
	
	public static void awardAdvancement(ServerPlayer player, String name)
	{
		Advancement advancement = player.server.getAdvancements().getAdvancement(ResourceLocation.parse(name));
		AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
		if(!progress.isDone())
		{
			progress.getRemainingCriteria().forEach(t ->
			{
				player.getAdvancements().award(advancement, t);
			});
		}
	}
	
    public static void disableShield(LivingEntity entity, DamageSource source, int ticks)
    {
    	if(entity.isDamageSourceBlocked(source))
    	{
        	if(entity instanceof Player player)
        	{
        		player.disableShield(true);
        	}
        	else
        	{
        		entity.stopUsingItem();
        		entity.level().broadcastEntityEvent(entity, (byte) 30);
        	}
    	}
    }
	
    public static boolean isNight(LevelAccessor level)
    {
    	return level.dayTime() % 24000L >= 13000L;
    }
    
    public static float rotlerp(float start, float end, float maxStep) 
    {
        float delta = Mth.wrapDegrees(end - start);
        float clampedDelta = Mth.clamp(delta, -maxStep, maxStep);
        return Mth.wrapDegrees(start + clampedDelta);
    }
    
    public static float horizontalDistanceTo(Entity entity, Entity target)
    {
    	return horizontalDistanceTo(entity, target.getX(), target.getZ());
    }
    
    public static float verticalDistanceTo(Entity entity, Entity target)
    {
    	return verticalDistanceTo(entity, target.getY());
    }
    
	public static float horizontalDistanceTo(Entity entity, double x, double z)
	{
		double relX = entity.getX() - x;
		double relZ = entity.getZ() - z;
		return Mth.sqrt((float) (relX * relX + relZ * relZ));
	}
	
	public static float verticalDistanceTo(Entity entity, double y)
	{
		double rel = entity.getY() - y;
		return Mth.sqrt((float) (rel * rel));
	}
    
	public static float distanceTo(Entity entity, Vec3 pos)
	{
		double x = entity.getX() - pos.x;
		double y = entity.getY() - pos.y;
		double z = entity.getZ() - pos.z;
		return Mth.sqrt((float) (x * x + y * y + z * z));
	}
	
	public static Vec3 getRandomPosition(RandomSource random, Vec3 start, Vec3 range)
	{
        double x = start.x + (random.nextDouble() - random.nextDouble()) * range.x + 0.5D;
        double y = start.y + (random.nextDouble() - random.nextDouble()) * range.y + 0.5D;
        double z = start.z + (random.nextDouble() - random.nextDouble()) * range.z + 0.5D;
        return new Vec3(x, y, z);
	}
	
	public static Vec3 getRandomPositionAroundAABB(AABB box, RandomSource random, double radius) 
	{
		double x = Mth.lerp(random.nextDouble(), box.minX, box.maxX) + (random.nextDouble() - 0.5) * radius;
		double y = Mth.lerp(random.nextDouble(), box.minY, box.maxY) + (random.nextDouble() - 0.5) * radius;
		double z = Mth.lerp(random.nextDouble(), box.minZ, box.maxZ) + (random.nextDouble() - 0.5) * radius;
		return new Vec3(x, y, z);
	}
	
	public static double percent(double baseValue, double percent)
	{
		return baseValue * percent / 100.0D;
	}
	
	public static boolean isModLoaded(String modid)
	{
		return ModList.get().isLoaded(modid);
	}
	
	public static BlockPos getPosition(BlockGetter level, Vec3 position, PositionTypes types)
    {
		return getPosition(level, position, types, Integer.MAX_VALUE);
    }
	
	public static BlockPos getPosition(BlockGetter level, Vec3 position, PositionTypes types, int maxStep)
    {
		int i = 0;
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(position.x, position.y, position.z);
        do
        {
        	mutablePos.move(types.getDirection());
        	i++;
        }
        while(i < maxStep && types.test(level, mutablePos));
        return mutablePos.immutable();
    }
	
	//commonly used for dash, need to call every tick;
	public static void lookAtDirection(Entity entity, Anchor anchor)
	{
		entity.lookAt(anchor, entity.position().add(entity.getDeltaMovement().scale(100.0F)));
	}
	
	public static void dashToward(Entity entity, double xz, double y)
	{
		dashToward(entity, new Vector3d(xz, y, xz));
	}
	
	public static void dashToward(Entity entity, Vector3d scale)
	{
        dash(entity.getYHeadRot(), 90.0D, scale);
        entity.push(scale.x, scale.y, scale.z);
	}
	
	public static void dashBackward(Entity entity, double xz, double y)
	{
		dashBackward(entity, new Vector3d(xz, y, xz));
	}
	
	public static void dashBackward(Entity entity, Vector3d scale)
	{
        dash(entity.getYHeadRot(), -90.0D, scale);
        entity.push(scale.x, scale.y, scale.z);
	}
	
	public static void dash(double yRot, double degrees, Vector3d scale)
	{
		double x = Math.cos(Math.toRadians(yRot + degrees));
        double z = Math.sin(Math.toRadians(yRot + degrees));
        scale.mul(x, 1.0D, z);
	}
	
	public static Vec2 lookAt(Vec3 start, Vec3 end)
	{
		double x = end.x - start.x;
		double y = end.y - start.y;
		double z = end.z - start.z;
		double root = Math.sqrt(x * x + z * z);
		double xRot = Mth.wrapDegrees((-(Mth.atan2(y, root) * (180.0F / Math.PI))));
		double yRot = Mth.wrapDegrees((Mth.atan2(z, x) * (180.0F / Math.PI)) - 90.0F);
	    return new Vec2((float) xRot, (float) yRot);
	}
	
	public static Vec3 getViewVector(Vec3 start, float pXRot, float pYRot, double left, double up, double forward) 
	{
	    float cosYawRad = Mth.cos(Math.toRadians(pYRot + 90.0F));
	    float sinYawRad = Mth.sin(Math.toRadians(pYRot + 90.0F));
	    float cosPitch = Mth.cos(Math.toRadians(-pXRot));
	    float sinPitch = Mth.sin(Math.toRadians(-pXRot));
	    float cosPitchOffset = Mth.cos(Math.toRadians(-pXRot + 90.0F));
	    float sinPitchOffset = Mth.sin(Math.toRadians(-pXRot + 90.0F));
	    
	    Vec3 forwardDir = new Vec3(cosYawRad * cosPitch, sinPitch, sinYawRad * cosPitch);
	    Vec3 upDir = new Vec3(cosYawRad * cosPitchOffset, sinPitchOffset, sinYawRad * cosPitchOffset);
	    Vec3 leftDir = forwardDir.cross(upDir).scale(-1.0D);
	    
	    double offsetX = forwardDir.x * forward + upDir.x * up + leftDir.x * left;
	    double offsetY = forwardDir.y * forward + upDir.y * up + leftDir.y * left;
	    double offsetZ = forwardDir.z * forward + upDir.z * up + leftDir.z * left;
	    
	    return start.add(offsetX, offsetY, offsetZ);
	}
	
	public static Vec3 getVectorTowards(Vec3 start, Vec3 end, double speed)
	{
		return end.subtract(start).normalize().scale(speed);
	}
	
	public static void writeVec3(FriendlyByteBuf buf, Vec3 vec3)
	{
		buf.writeDouble(vec3.x);
		buf.writeDouble(vec3.y);
		buf.writeDouble(vec3.z);
	}
	
	public static Vec3 readVec3(FriendlyByteBuf buf)
	{
		return new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
	}
}
