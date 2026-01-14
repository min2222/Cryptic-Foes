package com.min01.crypticfoes.block;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.blockentity.CrypticSkullBlockEntity;
import com.min01.crypticfoes.blockentity.ScreamerBlockEntity;
import com.min01.crypticfoes.misc.CrypticSkullTypes;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CrypticBlocks 
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CrypticFoes.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CrypticFoes.MODID);
	
	public static final RegistryObject<Block> FALLEN_LEAVES = BLOCKS.register("fallen_leaves", () -> new FallenLeavesBlock());
	public static final RegistryObject<Block> BLOCK_OF_FRAGILE_BONES = BLOCKS.register("block_of_fragile_bones", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.BONE_BLOCK)));
	public static final RegistryObject<Block> PILE_OF_FRAGILE_BONES = BLOCKS.register("pile_of_fragile_bones", () -> new PileofFragileBonesBlock());
	public static final RegistryObject<Block> POLISHED_PILE_OF_FRAGILE_BONES = BLOCKS.register("polished_pile_of_fragile_bones", () -> new PileofFragileBonesBlock());
	public static final RegistryObject<Block> WAXED_PILE_OF_FRAGILE_BONES = BLOCKS.register("waxed_pile_of_fragile_bones", () -> new Block(BlockBehaviour.Properties.copy(Blocks.BONE_BLOCK)));
	public static final RegistryObject<Block> WAXED_POLISHED_PILE_OF_FRAGILE_BONES = BLOCKS.register("waxed_polished_pile_of_fragile_bones", () -> new Block(BlockBehaviour.Properties.copy(Blocks.BONE_BLOCK)));
    public static final RegistryObject<Block> PILE_OF_FRAGILE_BONES_SLAB = BLOCKS.register("pile_of_fragile_bones_slab", () -> new PileofFragileBonesSlabBlock());
    public static final RegistryObject<Block> POLISHED_PILE_OF_FRAGILE_BONES_SLAB = BLOCKS.register("polished_pile_of_fragile_bones_slab", () -> new PileofFragileBonesSlabBlock());
    public static final RegistryObject<Block> SCREAMER = BLOCKS.register("screamer", () -> new ScreamerBlock());
    
    public static final RegistryObject<Block> HOWLER_HEAD = BLOCKS.register("howler_head", () -> new CrypticSkullBlock(CrypticSkullTypes.HOWLER, BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.CUSTOM_HEAD).strength(1.0F).pushReaction(PushReaction.DESTROY)));
    public static final RegistryObject<Block> HOWLER_WALL_HEAD = BLOCKS.register("howler_wall_head", () -> new CrypticWallSkullBlock(CrypticSkullTypes.HOWLER, BlockBehaviour.Properties.of().strength(1.0F).lootFrom(() -> HOWLER_HEAD.get()).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<BlockEntityType<CrypticSkullBlockEntity>> CRYPTIC_SKULL_BLOCK_ENTITY = BLOCK_ENTITIES.register("cryptic_skull", () -> BlockEntityType.Builder.of(CrypticSkullBlockEntity::new, CrypticBlocks.HOWLER_HEAD.get(), CrypticBlocks.HOWLER_WALL_HEAD.get()).build(null));
    public static final RegistryObject<BlockEntityType<ScreamerBlockEntity>> SCREAMER_BLOCK_ENTITY = BLOCK_ENTITIES.register("screamer", () -> BlockEntityType.Builder.of(ScreamerBlockEntity::new, CrypticBlocks.SCREAMER.get()).build(null));
}
