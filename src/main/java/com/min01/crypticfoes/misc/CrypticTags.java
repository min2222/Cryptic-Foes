package com.min01.crypticfoes.misc;

import com.min01.crypticfoes.CrypticFoes;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CrypticTags 
{
	public static final TagKey<Item> BURP_FOODS = createItem("burp_foods");
	public static final TagKey<Block> BREAKABLE_BY_SCREAM = createBlock("breakable_by_scream");
	public static final TagKey<EntityType<?>> RESIST_TO_STUN = createEntityType("resist_to_stun");
	
	public static TagKey<Item> createItem(String name) 
	{
		return ItemTags.create(ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, name));
	}
	
	public static TagKey<Block> createBlock(String name) 
	{
		return BlockTags.create(ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, name));
	}
	
	public static TagKey<EntityType<?>> createEntityType(String name)
	{
		return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, name));
	}
}
