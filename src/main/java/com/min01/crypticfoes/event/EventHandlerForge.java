package com.min01.crypticfoes.event;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.block.FallenLeavesBlock;
import com.min01.crypticfoes.effect.CrypticEffects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrypticFoes.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandlerForge 
{
	public static final Map<UUID, BlockPos> LEAVES_POS = new WeakHashMap<>();

	@SubscribeEvent
	public static void onLivingTick(LivingTickEvent event)
	{
	    LivingEntity entity = event.getEntity();
	    if(entity instanceof Player)
	    {
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
	
	@SubscribeEvent
	public static void onLivingDamage(LivingDamageEvent event)
	{
		LivingEntity entity = event.getEntity();
		if(entity.hasEffect(CrypticEffects.STUNNED.get()))
		{
			entity.removeEffect(CrypticEffects.STUNNED.get());
		}
	}
	
	@SubscribeEvent
	public static void onRightClickItem(PlayerInteractEvent.RightClickItem event)
	{
		LivingEntity entity = event.getEntity();
		if(entity.hasEffect(CrypticEffects.STUNNED.get()))
		{
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
	{
		LivingEntity entity = event.getEntity();
		if(entity.hasEffect(CrypticEffects.STUNNED.get()))
		{
			event.setCanceled(true);
		}
	}
}