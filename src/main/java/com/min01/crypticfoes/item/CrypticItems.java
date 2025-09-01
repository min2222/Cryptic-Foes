package com.min01.crypticfoes.item;

import java.util.function.Supplier;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.block.CrypticBlocks;
import com.min01.crypticfoes.entity.CrypticEntities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CrypticItems
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CrypticFoes.MODID);
	
	public static final RegistryObject<Item> PETRIFIED_SPAWN_EGG = registerSpawnEgg("petrified_spawn_egg", () -> CrypticEntities.PETRIFIED.get(), 13812100, 12029529);
	public static final RegistryObject<Item> BRANCHER_SPAWN_EGG = registerSpawnEgg("brancher_spawn_egg", () -> CrypticEntities.BRANCHER.get(), 4864030, 2364933);
	public static final RegistryObject<Item> HOWLER_SPAWN_EGG = registerSpawnEgg("howler_spawn_egg", () -> CrypticEntities.HOWLER.get(), 5126973, 8214643);
	
	public static final RegistryObject<Item> FRAGILE_BONE = ITEMS.register("fragile_bone", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> UNSTABLE_RESIN = ITEMS.register("unstable_resin", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> HOWLER_MEMBRANE = ITEMS.register("howler_membrane", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> MONSTROUS_HORN = ITEMS.register("monstrous_horn", () -> new MonstrousHornItem());
	public static final RegistryObject<Item> SILENCING_BLEND = ITEMS.register("silencing_blend", () -> new SilencingBlendItem());
	
	public static final RegistryObject<Item> FALLEN_LEAVES = registerBlockItem("fallen_leaves", () -> CrypticBlocks.FALLEN_LEAVES.get(), new Item.Properties());
	public static final RegistryObject<Item> BLOCK_OF_FRAGILE_BONES = registerBlockItem("block_of_fragile_bones", () -> CrypticBlocks.BLOCK_OF_FRAGILE_BONES.get(), new Item.Properties());
	public static final RegistryObject<Item> PILE_OF_FRAGILE_BONES = registerBlockItem("pile_of_fragile_bones", () -> CrypticBlocks.PILE_OF_FRAGILE_BONES.get(), new Item.Properties());
	public static final RegistryObject<Item> POLISHED_PILE_OF_FRAGILE_BONES = registerBlockItem("polished_pile_of_fragile_bones", () -> CrypticBlocks.POLISHED_PILE_OF_FRAGILE_BONES.get(), new Item.Properties());
	public static final RegistryObject<Item> WAXED_PILE_OF_FRAGILE_BONES = registerBlockItem("waxed_pile_of_fragile_bones", () -> CrypticBlocks.WAXED_PILE_OF_FRAGILE_BONES.get(), new Item.Properties());
	public static final RegistryObject<Item> WAXED_POLISHED_PILE_OF_FRAGILE_BONES = registerBlockItem("waxed_polished_pile_of_fragile_bones", () -> CrypticBlocks.WAXED_POLISHED_PILE_OF_FRAGILE_BONES.get(), new Item.Properties());
	public static final RegistryObject<Item> PILE_OF_FRAGILE_BONES_SLAB = registerBlockItem("pile_of_fragile_bones_slab", () -> CrypticBlocks.PILE_OF_FRAGILE_BONES_SLAB.get(), new Item.Properties());
	public static final RegistryObject<Item> POLISHED_PILE_OF_FRAGILE_BONES_SLAB = registerBlockItem("polished_pile_of_fragile_bones_slab", () -> CrypticBlocks.POLISHED_PILE_OF_FRAGILE_BONES_SLAB.get(), new Item.Properties());
	
	public static RegistryObject<Item> registerBlockItem(String name, Supplier<Block> block, Item.Properties properties)
	{
		return ITEMS.register(name, () -> new BlockItem(block.get(), properties));
	}
	
	public static RegistryObject<Item> registerSpawnEgg(String name, Supplier<EntityType<? extends Mob>> type, int color1, int color2)
	{
		return ITEMS.register(name, () -> new ForgeSpawnEggItem(type, color1, color2, new Item.Properties()));
	}
}
