package com.min01.crypticfoes.event;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.effect.CrypticEffects;
import com.min01.crypticfoes.entity.CrypticEntities;
import com.min01.crypticfoes.entity.living.EntityBrancher;
import com.min01.crypticfoes.entity.living.EntityPetrified;
import com.min01.crypticfoes.item.CrypticItems;

import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
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
		ItemStack awkward = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD);
		ItemStack fragility = PotionUtils.setPotion(new ItemStack(Items.POTION), CrypticEffects.FRAGILITY_POTION.get());
		BrewingRecipeRegistry.addRecipe(Ingredient.of(awkward), Ingredient.of(CrypticItems.FRAGILE_BONE.get()), fragility);
	}
	
    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) 
    {
    	event.put(CrypticEntities.PETRIFIED.get(), EntityPetrified.createAttributes().build());
    	event.put(CrypticEntities.BRANCHER.get(), EntityBrancher.createAttributes().build());
    }
    
    @SubscribeEvent
    public static void onSpawnPlacementRegister(SpawnPlacementRegisterEvent event)
    {
    	event.register(CrypticEntities.BRANCHER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EntityBrancher::checkBrancherSpawnRules, Operation.AND);
    }
}
