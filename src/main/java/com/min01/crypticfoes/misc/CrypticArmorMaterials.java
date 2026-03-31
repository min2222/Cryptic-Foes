package com.min01.crypticfoes.misc;

import com.min01.crypticfoes.item.CrypticItems;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public class CrypticArmorMaterials
{
	public static final ArmorMaterial ROLLING = new CrypticArmorMaterial("rolling", new int[]{77, 112, 105, 91}, new int[]{2, 5, 3, 1}, 25, SoundEvents.ARMOR_EQUIP_GOLD, 0.0F, 0.0F, () -> Ingredient.of(CrypticItems.STURDY_SHELL.get()));
}
