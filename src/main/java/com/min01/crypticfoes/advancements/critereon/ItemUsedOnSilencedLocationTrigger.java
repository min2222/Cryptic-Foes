package com.min01.crypticfoes.advancements.critereon;

import com.min01.crypticfoes.CrypticFoes;

import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger;
import net.minecraft.resources.ResourceLocation;

public class ItemUsedOnSilencedLocationTrigger extends ItemUsedOnLocationTrigger
{
	public ItemUsedOnSilencedLocationTrigger()
	{
		super(ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "item_used_on_silenced_block"));
	}
}