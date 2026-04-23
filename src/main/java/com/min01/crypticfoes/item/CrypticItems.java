package com.min01.crypticfoes.item;

import java.util.function.Supplier;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.block.CrypticBlocks;
import com.min01.crypticfoes.entity.CrypticEntities;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CrypticItems
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CrypticFoes.MODID);
	
	public static final RegistryObject<Item> HOWLER_SPAWN_EGG = registerSpawnEgg("howler_spawn_egg", () -> CrypticEntities.HOWLER.get(), 5126973, 8214643);
	public static final RegistryObject<Item> BUMPY_SPAWN_EGG = registerSpawnEgg("bumpy_spawn_egg", () -> CrypticEntities.BUMPY.get(), 11037495, 16706733);
	
	public static final RegistryObject<Item> HOWLER_MEMBRANE = ITEMS.register("howler_membrane", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> MONSTROUS_HORN = ITEMS.register("monstrous_horn", () -> new MonstrousHornItem());
	public static final RegistryObject<Item> SILENCING_BLEND = ITEMS.register("silencing_blend", () -> new SilencingBlendItem());
	public static final RegistryObject<Item> CAVE_SALAD = ITEMS.register("cave_salad", () -> new CaveSaladItem());
	public static final RegistryObject<Item> STURDY_SHELL = ITEMS.register("sturdy_shell", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> ROLLING_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("rolling_upgrade_smithing_template", () -> new RollingSmithingTemplateItem());
	public static final RegistryObject<Item> ROLLING_CHESTPLATE = ITEMS.register("rolling_chestplate", () -> new RollingChestplateItem(new Item.Properties()));
	public static final RegistryObject<Item> WEIGHT_BLOCK = registerBlockItem("weight_block", () -> CrypticBlocks.WEIGHT_BLOCK.get(), new Item.Properties());
	public static final RegistryObject<Item> BALL = ITEMS.register("ball", () -> new BallItem());

	public static final RegistryObject<Item> HOWLER_HEAD = ITEMS.register("howler_head", () -> new StandingAndWallBlockItem(CrypticBlocks.HOWLER_HEAD.get(), CrypticBlocks.HOWLER_WALL_HEAD.get(), (new Item.Properties()).rarity(Rarity.UNCOMMON), Direction.DOWN));
	
	public static final RegistryObject<Item> SCREAMER = ITEMS.register("screamer", () -> new ScreamerBlockItem(CrypticBlocks.SCREAMER.get(), new Item.Properties()));
	   
	public static RegistryObject<Item> registerBlockItem(String name, Supplier<Block> block, Item.Properties properties)
	{
		return ITEMS.register(name, () -> new BlockItem(block.get(), properties));
	}
	
	public static RegistryObject<Item> registerSpawnEgg(String name, Supplier<EntityType<? extends Mob>> type, int color1, int color2)
	{
		return ITEMS.register(name, () -> new ForgeSpawnEggItem(type, color1, color2, new Item.Properties()));
	}
}
