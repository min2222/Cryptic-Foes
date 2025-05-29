package com.min01.crypticfoes.event;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.block.FallenLeavesBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrypticFoes.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandlerForge 
{
	public static final Map<UUID, BlockPos> LEAVES_POS = new WeakHashMap<>();
	
	@SubscribeEvent
	public static void onEntityJoinLevel(EntityJoinLevelEvent event)
	{
		
	}

	@SubscribeEvent
	public static void onLivingTick(LivingTickEvent event)
	{
	    LivingEntity entity = event.getEntity();
	    BlockPos pos = entity.blockPosition();
	    UUID uuid = entity.getUUID();
	    if(entity.level.getBlockState(pos).getBlock() instanceof FallenLeavesBlock block)
	    {
	        BlockPos leavesPos = LEAVES_POS.get(uuid);
	        if(!pos.equals(leavesPos)) 
	        {
	        	LEAVES_POS.put(uuid, pos);
	            block.stepOn(entity.level, pos, entity.level.getBlockState(pos), entity);
	        }
	    }
	    else 
	    {
	    	LEAVES_POS.remove(uuid);
	    }
	}
}
