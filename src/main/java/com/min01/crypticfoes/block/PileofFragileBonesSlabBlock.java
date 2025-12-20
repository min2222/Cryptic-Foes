package com.min01.crypticfoes.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class PileofFragileBonesSlabBlock extends SlabBlock
{
	public PileofFragileBonesSlabBlock() 
	{
		super(BlockBehaviour.Properties.copy(Blocks.BONE_BLOCK));
	}
	
	@Override
	public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity)
	{
		if(!pEntity.isSteppingCarefully() && pEntity instanceof LivingEntity)
		{
			int tick = pState.is(CrypticBlocks.POLISHED_PILE_OF_FRAGILE_BONES_SLAB.get()) ? 5 : 2;
			pLevel.scheduleTick(pPos, this, tick);
		}
	}
	
	@Override
	public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) 
	{
		pLevel.destroyBlock(pPos, false);
		pLevel.gameEvent(GameEvent.BLOCK_DESTROY, pPos, GameEvent.Context.of(pState));
		pLevel.levelEvent(2001, pPos, Block.getId(pState));
	}
}
