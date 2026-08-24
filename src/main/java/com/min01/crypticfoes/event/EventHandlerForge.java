package com.min01.crypticfoes.event;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.api.entity.AbstractAnimatableMonster;
import com.min01.crypticfoes.capabilties.IRollingCapability;
import com.min01.crypticfoes.capabilties.RollingCapabilityImpl;
import com.min01.crypticfoes.effect.CrypticEffects;
import com.min01.crypticfoes.entity.living.BumpyEntity;
import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.PlayLevelSoundEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrypticFoes.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandlerForge 
{
	@SubscribeEvent
	public static void onPlayLevelSoundAtPosition(PlayLevelSoundEvent.AtPosition event)
	{
		Holder<SoundEvent> sound = event.getSound();
		Level level = event.getLevel();
		BlockPos blockPos = BlockPos.containing(event.getPosition());
		//TODO silencing blend and howler awake;
	}
	
	@SubscribeEvent
	public static void onPlayLevelSoundAtEntity(PlayLevelSoundEvent.AtEntity event)
	{
		Holder<SoundEvent> sound = event.getSound();
		Level level = event.getLevel();
		BlockPos blockPos = event.getEntity().blockPosition();
		//TODO howler awake;
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onLivingDeath(LivingDeathEvent event)
	{
		LivingEntity living = event.getEntity();
		DamageSource source = event.getSource();
		Entity entity = source.getEntity();
		if(entity instanceof ServerPlayer player && living instanceof AbstractAnimatableMonster)
		{
			CrypticUtil.awardAdvancement(player, "minecraft:adventure/kill_a_mob");
		}
	}
	
	@SubscribeEvent
	public static void onShieldBlock(ShieldBlockEvent event)
	{
		DamageSource source = event.getDamageSource();
		Entity entity = source.getDirectEntity();
		if(entity instanceof BumpyEntity bumpy)
		{
			bumpy.stun();
		}
	}
	
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent event)
	{
		Player player = event.player;
		IRollingCapability cap = player.getCapability(RollingCapabilityImpl.ROLLING).orElse(new RollingCapabilityImpl());
		cap.tick(player);
	}
	
	@SubscribeEvent
	public static void onLivingKnockback(LivingKnockBackEvent event)
	{
		LivingEntity entity = event.getEntity();
		//TODO prevent in rolling state;
	}
	
	@SubscribeEvent
	public static void onLivingDamage(LivingDamageEvent event)
	{
		LivingEntity entity = event.getEntity();
		//TODO decrease amount in rolling state;
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
		//TODO prevent in rolling state;
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
		//TODO prevent in rolling state;
		//TODO silencing blend scrapping with axe;
	}
}