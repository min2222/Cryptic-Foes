package com.min01.crypticfoes.misc;

import com.min01.crypticfoes.CrypticFoes;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

public class CrypticTags
{
	public static class CrypticEntity
	{
		public static final TagKey<EntityType<?>> RESIST_TO_STUN = create("resist_to_stun");
		
		private static TagKey<EntityType<?>> create(String name) 
		{
			return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, name));
		}
	}
	
	public static class CrypticBlocks
	{
		public static final TagKey<Block> BREAKABLE_BY_SCREAM = create("breakable_by_scream");
		
		private static TagKey<Block> create(String name) 
		{
			return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, name));
		}
	}
}
