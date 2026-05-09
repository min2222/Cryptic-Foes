package com.min01.crypticfoes.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class MobilityEnchantment extends Enchantment
{
	public MobilityEnchantment() 
	{
		super(Rarity.RARE, CrypticEnchantments.ROLLING_CHESTPLATE, new EquipmentSlot[] {EquipmentSlot.CHEST});
	}
	
	@Override
	public int getMaxLevel() 
	{
		return 1;
	}
}
