package com.min01.crypticfoes.effect;

import com.min01.crypticfoes.CrypticFoes;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CrypticEffects 
{
	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CrypticFoes.MODID);
	public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, CrypticFoes.MODID);
	
	public static final RegistryObject<MobEffect> STUNNED = EFFECTS.register("stunned", () -> new StunnedEffect());
	
	public static void init()
	{
		
	}
}
