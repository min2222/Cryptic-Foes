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
import com.min01.crypticfoes.item.CrypticItems;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.Instruments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.util.MutableHashedLinkedMap;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
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
    
    @SubscribeEvent
    public static void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) 
    {
    	MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> entries = event.getEntries();
    	ResourceKey<CreativeModeTab> tabKey = event.getTabKey();
    	if(tabKey == CreativeModeTabs.INGREDIENTS)
    	{
    		entries.putAfter(Items.STRING.getDefaultInstance(), CrypticItems.HOWLER_MEMBRANE.get().getDefaultInstance(), TabVisibility.PARENT_AND_SEARCH_TABS);
    		entries.putAfter(Items.HONEYCOMB.getDefaultInstance(), CrypticItems.SILENCING_BLEND.get().getDefaultInstance(), TabVisibility.PARENT_AND_SEARCH_TABS);
    	}
    	if(tabKey == CreativeModeTabs.FOOD_AND_DRINKS)
    	{
    		entries.putAfter(Items.RABBIT_STEW.getDefaultInstance(), CrypticItems.CAVE_SALAD.get().getDefaultInstance(), TabVisibility.PARENT_AND_SEARCH_TABS);
    	}
    	if(tabKey == CreativeModeTabs.TOOLS_AND_UTILITIES)
    	{
    		entries.putAfter(InstrumentItem.create(Items.GOAT_HORN, BuiltInRegistries.INSTRUMENT.getHolderOrThrow(Instruments.DREAM_GOAT_HORN)), CrypticItems.MONSTROUS_HORN.get().getDefaultInstance(), TabVisibility.PARENT_AND_SEARCH_TABS);
    	}
    	if(tabKey == CreativeModeTabs.COMBAT)
    	{
    		entries.putAfter(Items.DIAMOND_HORSE_ARMOR.getDefaultInstance(), CrypticItems.MONSTROUS_HORN.get().getDefaultInstance(), TabVisibility.PARENT_AND_SEARCH_TABS);
    	}
    	if(tabKey == CreativeModeTabs.FUNCTIONAL_BLOCKS)
    	{
    		entries.putAfter(Items.DRAGON_HEAD.getDefaultInstance(), CrypticItems.HOWLER_HEAD.get().getDefaultInstance(), TabVisibility.PARENT_AND_SEARCH_TABS);
    	}
    	if(tabKey == CreativeModeTabs.SPAWN_EGGS)
    	{
    		entries.putAfter(Items.HORSE_SPAWN_EGG.getDefaultInstance(), CrypticItems.HOWLER_SPAWN_EGG.get().getDefaultInstance(), TabVisibility.PARENT_AND_SEARCH_TABS);
    	}
    }
}
