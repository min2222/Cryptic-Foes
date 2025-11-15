package com.min01.crypticfoes.event;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.block.FallenLeavesBlock;
import com.min01.crypticfoes.effect.CrypticEffects;
import com.min01.crypticfoes.entity.living.EntityHowler;
import com.min01.crypticfoes.network.CrypticNetwork;
import com.min01.crypticfoes.network.UpdateSilencedBlocksPacket;
import com.min01.crypticfoes.network.UpdateStunnedEffectPacket;
import com.min01.crypticfoes.sound.CrypticSounds;
import com.min01.crypticfoes.util.CrypticUtil;
import com.min01.crypticfoes.world.CrypticSavedData;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.PlayLevelSoundEvent;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.Type;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrypticFoes.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandlerForge 
{
	public static final Map<UUID, BlockPos> LEAVES_POS = new WeakHashMap<>();

	@SubscribeEvent
	public static void onPlayLevelSoundAtPosition(PlayLevelSoundEvent.AtPosition event)
	{
		Holder<SoundEvent> sound = event.getSound();
		Level level = event.getLevel();
		BlockPos blockPos = BlockPos.containing(event.getPosition());
		if(CrypticUtil.isBlockSilenced(level, blockPos))
		{
			event.setCanceled(true);
		}
		else if(sound != null);
		{
			for(Entity entity : CrypticUtil.getAllEntities(level))
			{
				if(sound.get() != SoundEvents.BELL_BLOCK || !blockPos.closerToCenterThan(entity.position(), 40) || !(entity instanceof EntityHowler howler) || !howler.isHowlerSleeping() || howler.getAnimationState() != 1 || howler.level.dimension() != level.dimension())
				{
					continue;
				}
				if(!CrypticUtil.isBlockSilenced(level, blockPos))
				{
		    		howler.awake();
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onPlayLevelSoundAtEntity(PlayLevelSoundEvent.AtEntity event)
	{
		Holder<SoundEvent> sound = event.getSound();
		Level level = event.getLevel();
		BlockPos blockPos = event.getEntity().blockPosition();
		if(sound != null);
		{
			for(Entity entity : CrypticUtil.getAllEntities(level))
			{
				if(!sound.get().getLocation().toString().contains("goat_horn") || !blockPos.closerToCenterThan(entity.position(), 40) || !(entity instanceof EntityHowler howler) || !howler.isHowlerSleeping() || howler.getAnimationState() != 1 || howler.level.dimension() != level.dimension())
				{
					continue;
				}
	    		howler.awake();
			}
		}
	}
	
	@SubscribeEvent
	public static void onLevelTick(LevelTickEvent event)
	{
		if(event.phase == Phase.END && event.type == Type.LEVEL)
		{
			CrypticUtil.removeSilencedBlocks(event.level);
		}
	}
	
	@SubscribeEvent
	public static void onLevelLoad(LevelEvent.Load event)
	{
		Level level = (Level) event.getLevel();
		CrypticSavedData data = CrypticSavedData.get(level);
		if(data != null)
		{
			CrypticNetwork.sendToAll(new UpdateSilencedBlocksPacket(data.getSilencedBlocks()));
		}
	}
	
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
    public static void onMobEffectAdded(MobEffectEvent.Added event) 
    {
    	LivingEntity living = event.getEntity();
    	MobEffectInstance instance = event.getEffectInstance();
    	if(instance.getEffect() == CrypticEffects.STUNNED.get() && !living.level.isClientSide)
    	{
    		CrypticNetwork.sendToAll(new UpdateStunnedEffectPacket(living.getUUID(), instance.getAmplifier(), instance.getDuration(), false));
    	}
    }
    
    @SubscribeEvent
    public static void onMobEffectRemove(MobEffectEvent.Remove event) 
    {
    	LivingEntity living = event.getEntity();
    	MobEffectInstance instance = event.getEffectInstance();
    	if(instance.getEffect() == CrypticEffects.STUNNED.get() && !living.level.isClientSide)
    	{
    		CrypticNetwork.sendToAll(new UpdateStunnedEffectPacket(living.getUUID(), instance.getAmplifier(), instance.getDuration(), true));
    	}
    }
    
    @SubscribeEvent
    public static void onMobEffectExpired(MobEffectEvent.Expired event) 
    {
    	LivingEntity living = event.getEntity();
    	MobEffectInstance instance = event.getEffectInstance();
    	if(instance.getEffect() == CrypticEffects.STUNNED.get() && !living.level.isClientSide)
    	{
    		CrypticNetwork.sendToAll(new UpdateStunnedEffectPacket(living.getUUID(), instance.getAmplifier(), instance.getDuration(), true));
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
		Player player = event.getEntity();
		Level level = event.getLevel();
		BlockPos pos = event.getPos();
		ItemStack stack = event.getItemStack();
		if(player.hasEffect(CrypticEffects.STUNNED.get()))
		{
			event.setCanceled(true);
		}
		if(CrypticUtil.isBlockSilenced(level, pos) && stack.canPerformAction(ToolActions.AXE_WAX_OFF))
		{
			CrypticUtil.removeSilencedBlock(level, pos);
			BlockState state = level.getBlockState(pos);
			if(player instanceof ServerPlayer)
			{
				CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, pos, stack);
			}
			level.playSound(player, pos, CrypticSounds.SILENCING_BLEND_OFF.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
			level.levelEvent(player, 3004, pos, 0);
			level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state));
			stack.hurtAndBreak(1, player, (p_150686_) ->
			{
				p_150686_.broadcastBreakEvent(event.getHand());
			});
			player.swing(event.getHand());
			event.setCanceled(true);
			event.setCancellationResult(InteractionResult.sidedSuccess(player.level.isClientSide));
		}
	}
}