package com.min01.crypticfoes.enchantment;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.item.CrypticItems;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CrypticEnchantments 
{
	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, CrypticFoes.MODID);
	
	public static final EnchantmentCategory ROLLING_CHESTPLATE = EnchantmentCategory.create("rolling_chestplate", (item -> item == CrypticItems.ROLLING_CHESTPLATE.get()));
	
	public static final RegistryObject<Enchantment> PATIENCE = ENCHANTMENTS.register("patience", () -> new PatienceEnchantment());
	public static final RegistryObject<Enchantment> ACCELERATE = ENCHANTMENTS.register("accelerate", () -> new AccelerateEnchantment());
	public static final RegistryObject<Enchantment> MOBILITY = ENCHANTMENTS.register("mobility", () -> new MobilityEnchantment());
	
	public static void addAllEnchantsToCreativeTab(CreativeModeTab.Output output, EnchantmentCategory enchantmentCategory)
	{
		for(RegistryObject<Enchantment> enchantObject : ENCHANTMENTS.getEntries())
		{
			if(enchantObject.isPresent())
			{
				Enchantment enchant = enchantObject.get();
				if(enchant.category == enchantmentCategory)
				{
					EnchantmentInstance instance = new EnchantmentInstance(enchant, enchant.getMaxLevel());
					output.accept(EnchantedBookItem.createForEnchantment(instance));
				}
			}
		}
	}
}
