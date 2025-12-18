package com.min01.crypticfoes.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import com.min01.crypticfoes.network.AddSilencingParticlePacket;
import com.min01.crypticfoes.network.CrypticNetwork;
import com.min01.crypticfoes.world.CrypticSavedData;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class CrypticUtil 
{
	public static final Method GET_ENTITY = ObfuscationReflectionHelper.findMethod(Level.class, "m_142646_");
	
	public static final List<BlockPos> SILENCED_BLOCKS = new ArrayList<>();
	
	public static boolean isDone(ServerPlayer serverPlayer, String name)
	{
		Advancement adv = serverPlayer.server.getAdvancements().getAdvancement(new ResourceLocation(name));
		AdvancementProgress progress = serverPlayer.getAdvancements().getOrStartProgress(adv);
		return progress.isDone();
	}
	
	public static void awardAdvancement(ServerPlayer serverPlayer, String name)
	{
		Advancement adv = serverPlayer.server.getAdvancements().getAdvancement(new ResourceLocation(name));
		AdvancementProgress progress = serverPlayer.getAdvancements().getOrStartProgress(adv);
		if(!progress.isDone())
		{
			progress.getRemainingCriteria().forEach(t ->
			{
				serverPlayer.getAdvancements().award(adv, t);
			});
		}
	}
	
    public static float distanceToY(BlockPos blockPos, BlockPos targetPos)
    {
        float f = (float)(blockPos.getY() - targetPos.getY());
        return Mth.sqrt(f * f);
    }
    
    public static float distanceToY(Entity entity, BlockPos pos)
    {
        float f = (float)(entity.getY() - pos.getY());
        return Mth.sqrt(f * f);
    }
    
	public static void setBlockSilence(Level level, BlockPos pos)
	{
		CrypticSavedData data = CrypticSavedData.get(level);
		if(data != null)
		{
			data.setBlockSilence(pos);
			CrypticNetwork.sendToAll(new AddSilencingParticlePacket(pos));
		}
		else
		{
			SILENCED_BLOCKS.add(pos);
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
			return data.isBlockSilenced(level, pos);
		}
		return SILENCED_BLOCKS.contains(pos);
	}
	
	@SuppressWarnings("deprecation")
	public static BlockPos getCeilingPos(BlockGetter pLevel, double pX, double startY, double pZ, int aboveY)
    {
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos(pX, startY, pZ);
        do
        {
        	blockpos$mutable.move(Direction.UP);
        }
        while((pLevel.getBlockState(blockpos$mutable).isAir() || pLevel.getBlockState(blockpos$mutable).liquid() || !pLevel.getBlockState(blockpos$mutable).isCollisionShapeFullBlock(pLevel, blockpos$mutable)) && blockpos$mutable.getY() < pLevel.getMaxBuildHeight());
        BlockPos pos = blockpos$mutable.above().above(aboveY);
        return pos;
    }
	
	@SuppressWarnings("deprecation")
	public static BlockPos getGroundPos(BlockGetter pLevel, double pX, double startY, double pZ, int belowY)
    {
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos(pX, startY, pZ);
        do
        {
        	blockpos$mutable.move(Direction.DOWN);
        }
        while((pLevel.getBlockState(blockpos$mutable).isAir() || pLevel.getBlockState(blockpos$mutable).liquid() || !pLevel.getBlockState(blockpos$mutable).isCollisionShapeFullBlock(pLevel, blockpos$mutable)) && blockpos$mutable.getY() > pLevel.getMinBuildHeight());
        BlockPos pos = blockpos$mutable.below().below(belowY);
        return pos;
    }
	
	public static void getClientLevel(Consumer<Level> consumer)
	{
		LogicalSidedProvider.CLIENTWORLD.get(LogicalSide.CLIENT).filter(ClientLevel.class::isInstance).ifPresent(level -> 
		{
			consumer.accept(level);
		});
	}
	
    public static double getMeleeAttackRangeSqr(float width, LivingEntity target, float multiplier)
    {
    	return (double)(width * multiplier * width * multiplier + target.getBbWidth());
    }
    
    public static double getMeleeAttackRangeSqr(Entity owner, LivingEntity target, float multiplier)
    {
    	return (double)(owner.getBbWidth() * multiplier * owner.getBbWidth() * multiplier + target.getBbWidth());
    }
    
    public static boolean isWithinMeleeAttackRange(Vec3 pos, float width, LivingEntity target, float multiplier)
    {
    	double d0 = pos.distanceToSqr(target.getX(), target.getY(), target.getZ());
    	return d0 <= getMeleeAttackRangeSqr(width, target, multiplier);
    }

    public static boolean isWithinMeleeAttackRange(Entity owner, LivingEntity target, float multiplier)
    {
    	double d0 = owner.distanceToSqr(target.getX(), target.getY(), target.getZ());
    	return d0 <= getMeleeAttackRangeSqr(owner, target, multiplier);
    }
	
	public static Vec3 fromToVector(Vec3 from, Vec3 to, float scale)
	{
		Vec3 motion = to.subtract(from).normalize();
		return motion.scale(scale);
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
}
