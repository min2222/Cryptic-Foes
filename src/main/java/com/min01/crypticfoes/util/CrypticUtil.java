package com.min01.crypticfoes.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import org.joml.Math;

import com.min01.crypticfoes.misc.PositionTypes;
import com.min01.crypticfoes.network.AddSilencingParticlePacket;
import com.min01.crypticfoes.network.CrypticNetwork;
import com.min01.crypticfoes.world.CrypticSavedData;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class CrypticUtil 
{
	public static final Method GET_ENTITY = ObfuscationReflectionHelper.findMethod(Level.class, "m_142646_");
	public static final List<BlockPos> SILENCED_BLOCKS = new ArrayList<>();
	
	public static void getClientLevel(Consumer<Level> consumer)
	{
		LogicalSidedProvider.CLIENTWORLD.get(LogicalSide.CLIENT).filter(ClientLevel.class::isInstance).ifPresent(level -> 
		{
			consumer.accept(level);
		});
	}
	
	@SuppressWarnings("unchecked")
	public static Iterable<Entity> getAllEntities(Level level)
	{
		try 
		{
			LevelEntityGetter<Entity> entities = (LevelEntityGetter<Entity>) GET_ENTITY.invoke(level);
			return entities.getAll();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Entity> T getEntityByUUID(Level level, UUID uuid)
	{
		try 
		{
			LevelEntityGetter<Entity> entities = (LevelEntityGetter<Entity>) GET_ENTITY.invoke(level);
			return (T) entities.get(uuid);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
    public static boolean isMoving(Entity entity) 
    {
		return entity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D;
    }

	//called in end of tick() method of entity class;
	public static void forceTick(Entity entity)
	{
		if(entity.level instanceof ServerLevel serverLevel)
		{
			serverLevel.getChunkSource().updateChunkForced(entity.chunkPosition(), true);
		}
	}
	
	public static boolean isDone(ServerPlayer serverPlayer, String name)
	{
		Advancement adv = serverPlayer.server.getAdvancements().getAdvancement(ResourceLocation.parse(name));
		AdvancementProgress progress = serverPlayer.getAdvancements().getOrStartProgress(adv);
		return progress.isDone();
	}
	
	public static void awardAdvancement(ServerPlayer serverPlayer, String name)
	{
		Advancement adv = serverPlayer.server.getAdvancements().getAdvancement(ResourceLocation.parse(name));
		AdvancementProgress progress = serverPlayer.getAdvancements().getOrStartProgress(adv);
		if(!progress.isDone())
		{
			progress.getRemainingCriteria().forEach(t ->
			{
				serverPlayer.getAdvancements().award(adv, t);
			});
		}
	}
	
    public static void disableShield(LivingEntity livingEntity, DamageSource source, int ticks)
    {
    	if(livingEntity.isDamageSourceBlocked(source))
    	{
        	if(livingEntity instanceof Player player)
        	{
        		player.disableShield(true);
        	}
        	else
        	{
        		livingEntity.stopUsingItem();
        		livingEntity.level.broadcastEntityEvent(livingEntity, (byte)30);
        	}
    	}
    }
	
    public static boolean isNight(LevelAccessor level)
    {
    	return level.dayTime() % 24000L >= 13000L;
    }
	
    public static Entity teleportEntityToDimension(Entity entity, ServerLevel serverLevel, BlockPos pos)
    {
        if(entity.level.dimension() != serverLevel.dimension())
        {
            entity.moveTo(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, entity.getYRot(), 0.0F);
        }
        
        if(entity instanceof ServerPlayer serverPlayer) 
        {
        	serverPlayer.teleportTo(serverLevel, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, entity.getYRot(), entity.getXRot());
            return serverPlayer;
        }

        entity.unRide();
        entity.changeDimension(serverLevel);
        Entity teleportedEntity = entity.getType().create(serverLevel);
        if(teleportedEntity == null)
        {
        	return entity;
        }
        teleportedEntity.restoreFrom(entity);
        teleportedEntity.moveTo(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, entity.getYRot(), entity.getXRot());
        teleportedEntity.setYHeadRot(entity.getYRot());
        serverLevel.addDuringTeleport(teleportedEntity);
    	return teleportedEntity;
    }
    
    public static float rotlerp(float start, float end, float maxStep) 
    {
        float delta = Mth.wrapDegrees(end - start);
        float clampedDelta = Mth.clamp(delta, -maxStep, maxStep);
        return Mth.wrapDegrees(start + clampedDelta);
    }
    
    public static float distanceToXZ(Entity entity, Entity target)
    {
        float x = (float)(entity.getX() - target.getX());
        float z = (float)(entity.getZ() - target.getZ());
        return Mth.sqrt(x * x + z * z);
    }
    
	public static Vec3 getSpreadPosition(RandomSource random, Vec3 startPos, Vec3 range)
	{
        double x = startPos.x + (random.nextDouble() - random.nextDouble()) * range.x + 0.5D;
        double y = startPos.y + (random.nextDouble() - random.nextDouble()) * range.y + 0.5D;
        double z = startPos.z + (random.nextDouble() - random.nextDouble()) * range.z + 0.5D;
        return new Vec3(x, y, z);
	}
	
	public static float percent(float baseValue, float percent)
	{
		return baseValue * percent / 100.0F;
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
	
	public static void dashToward(Entity entity, Vec3 scale)
	{
        float x = (float) Math.cos(Math.toRadians(entity.getYHeadRot() + 90));
        float z = (float) Math.sin(Math.toRadians(entity.getYHeadRot() + 90));
        entity.push(x * scale.x, scale.y, z * scale.z);
	}
	
	public static void dashBackward(Entity entity, Vec3 scale)
	{
        float x = (float) Math.cos(Math.toRadians(entity.getYHeadRot() - 90));
        float z = (float) Math.sin(Math.toRadians(entity.getYHeadRot() - 90));
        entity.push(x * scale.x, scale.y, z * scale.z);
	}
	
	public static float distanceTo(Entity entity, Vec3 pos)
	{
		float x = (float)(entity.getX() - pos.x);
		float y = (float)(entity.getY() - pos.y);
		float z = (float)(entity.getZ() - pos.z);
		return Mth.sqrt(x * x + y * y + z * z);
	}
	
	public static Vec2 lookAt(Vec3 startPos, Vec3 pos)
	{
		Vec3 vec3 = startPos;
		double d0 = pos.x - vec3.x;
		double d1 = pos.y - vec3.y;
		double d2 = pos.z - vec3.z;
		double d3 = Math.sqrt(d0 * d0 + d2 * d2);
		float xRot = Mth.wrapDegrees((float)(-(Mth.atan2(d1, d3) * (double)(180.0F / (float)Math.PI))));
		float yRot = Mth.wrapDegrees((float)(Mth.atan2(d2, d0) * (double)(180.0F / (float)Math.PI)) - 90.0F);
	    return new Vec2(xRot, yRot);
	}
	
	public static Vec3 getLookPos(Vec2 rotation, Vec3 position, double left, double up, double forwards) 
	{
		Vec2 vec2 = rotation;
		Vec3 vec3 = position;
		float f = Mth.cos((vec2.y + 90.0F) * ((float)Math.PI / 180.0F));
		float f1 = Mth.sin((vec2.y + 90.0F) * ((float)Math.PI / 180.0F));
		float f2 = Mth.cos(-vec2.x * ((float)Math.PI / 180.0F));
		float f3 = Mth.sin(-vec2.x * ((float)Math.PI / 180.0F));
		float f4 = Mth.cos((-vec2.x + 90.0F) * ((float)Math.PI / 180.0F));
		float f5 = Mth.sin((-vec2.x + 90.0F) * ((float)Math.PI / 180.0F));
		Vec3 vec31 = new Vec3((double)(f * f2), (double)f3, (double)(f1 * f2));
		Vec3 vec32 = new Vec3((double)(f * f4), (double)f5, (double)(f1 * f4));
		Vec3 vec33 = vec31.cross(vec32).scale(-1.0D);
		double d0 = vec31.x * forwards + vec32.x * up + vec33.x * left;
		double d1 = vec31.y * forwards + vec32.y * up + vec33.y * left;
		double d2 = vec31.z * forwards + vec32.z * up + vec33.z * left;
		return new Vec3(vec3.x + d0, vec3.y + d1, vec3.z + d2);
	}
	
	public static Vec3 getVelocityTowards(Vec3 from, Vec3 to, float speed)
	{
		Vec3 motion = to.subtract(from).normalize();
		return motion.scale(speed);
	}
	
    public static float distanceToY(BlockPos blockPos, BlockPos targetPos)
    {
        float y = (float)(blockPos.getY() - targetPos.getY());
        return Mth.sqrt(y * y);
    }
    
    public static float distanceToY(Entity entity, BlockPos pos)
    {
        float y = (float)(entity.getY() - pos.getY());
        return Mth.sqrt(y * y);
    }
    
	public static void setBlockSilence(Level level, BlockPos pos)
	{
		CrypticSavedData data = CrypticSavedData.get(level);
		if(data != null)
		{
			data.setBlockSilence(pos);
			CrypticNetwork.sendToAll(new AddSilencingParticlePacket(pos));
		}
	}
	
	public static void removeSilencedBlocks(Level level)
	{
		CrypticSavedData data = CrypticSavedData.get(level);
		if(data != null)
		{
			data.getSilencedBlocks().removeIf(t -> level.getBlockState(t).isAir());
		}
		else
		{
			SILENCED_BLOCKS.removeIf(t -> level.getBlockState(t).isAir());
		}
	}
	
	public static void removeSilencedBlock(Level level, BlockPos pos)
	{
		CrypticSavedData data = CrypticSavedData.get(level);
		if(data != null)
		{
			data.getSilencedBlocks().removeIf(t -> t.equals(pos));
		}
		else
		{
			SILENCED_BLOCKS.removeIf(t -> t.equals(pos));
		}
	}
	
	public static boolean isBlockSilenced(Level level, BlockPos pos)
	{
		CrypticSavedData data = CrypticSavedData.get(level);
		if(data != null)
		{
			return data.isBlockSilenced(pos);
		}
		return SILENCED_BLOCKS.contains(pos);
	}
}
