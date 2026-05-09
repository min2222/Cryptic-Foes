package com.min01.crypticfoes.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class AccelerateEnchantment extends Enchantment
{
	public AccelerateEnchantment() 
	{
		super(Rarity.RARE, CrypticEnchantments.ROLLING_CHESTPLATE, new EquipmentSlot[] {EquipmentSlot.CHEST});
	}
	
	@Override
	public int getMaxLevel() 
	{
		return 3;
	}
	
	@Override
	protected boolean checkCompatibility(Enchantment pOther) 
	{
		if(pOther == CrypticEnchantments.PATIENCE.get())
		{
			return false;
		}
		return super.checkCompatibility(pOther);
	}
}
