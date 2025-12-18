package com.min01.crypticfoes.event;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.advancements.CrypticCriteriaTriggers;
import com.min01.crypticfoes.block.CrypticBlocks;
import com.min01.crypticfoes.effect.CrypticEffects;
import com.min01.crypticfoes.entity.CrypticEntities;
import com.min01.crypticfoes.entity.living.EntityBrancher;
import com.min01.crypticfoes.entity.living.EntityHowler;
import com.min01.crypticfoes.entity.living.EntityPetrified;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent.Operation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = CrypticFoes.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventHandler 
{
	@SubscribeEvent
	public static void onFMLCommonSetup(FMLCommonSetupEvent event)
	{
		CrypticEffects.init();
		CrypticCriteriaTriggers.init();
		
		BiMap<Block, Block> waxables = HoneycombItem.WAXABLES.get();
		HoneycombItem.WAXABLES = Suppliers.memoize(() -> ImmutableBiMap.<Block, Block>builder().putAll(waxables).put(CrypticBlocks.PILE_OF_FRAGILE_BONES.get(), CrypticBlocks.WAXED_PILE_OF_FRAGILE_BONES.get()).put(CrypticBlocks.POLISHED_PILE_OF_FRAGILE_BONES.get(), CrypticBlocks.WAXED_POLISHED_PILE_OF_FRAGILE_BONES.get()).build());
	}
	
    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) 
    {
    	event.put(CrypticEntities.PETRIFIED.get(), EntityPetrified.createAttributes().build());
    	event.put(CrypticEntities.BRANCHER.get(), EntityBrancher.createAttributes().build());
    	event.put(CrypticEntities.HOWLER.get(), EntityHowler.createAttributes().build());
    }
    
    @SubscribeEvent
    public static void onSpawnPlacementRegister(SpawnPlacementRegisterEvent event)
    {
    	event.register(CrypticEntities.PETRIFIED.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EntityPetrified::checkPetrifiedSpawnRules, Operation.AND);
    	event.register(CrypticEntities.BRANCHER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, Operation.AND);
    	event.register(CrypticEntities.HOWLER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EntityHowler::checkHowlerSpawnRules, Operation.AND);
    }
}
