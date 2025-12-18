package com.min01.crypticfoes.effect;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.item.CrypticItems;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CrypticEffects 
{
	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CrypticFoes.MODID);
	public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, CrypticFoes.MODID);
	
	public static final RegistryObject<MobEffect> FRAGILITY = EFFECTS.register("fragility", () -> new FragilityEffect());
	public static final RegistryObject<MobEffect> STUNNED = EFFECTS.register("stunned", () -> new StunnedEffect());
	
	public static final RegistryObject<Potion> FRAGILITY_POTION = POTIONS.register("fragility", () -> new Potion(new MobEffectInstance(FRAGILITY.get(), 3600)));
	
	public static void init()
	{
		ItemStack awkward = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD);
		ItemStack fragility = PotionUtils.setPotion(new ItemStack(Items.POTION), CrypticEffects.FRAGILITY_POTION.get());
		BrewingRecipeRegistry.addRecipe(Ingredient.of(awkward), Ingredient.of(CrypticItems.FRAGILE_BONE.get()), fragility);
	}
}
