package com.min01.crypticfoes.blockentity;

import com.min01.crypticfoes.api.animation.LerpingAnimationState;
import com.min01.crypticfoes.block.CrypticBlocks;
import com.min01.crypticfoes.sound.CrypticSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CrypticSkullBlockEntity extends SkullBlockEntity
{
	public int tickCount;
	public final LerpingAnimationState blinkAnimationState = new LerpingAnimationState();
	
	public CrypticSkullBlockEntity(BlockPos pPos, BlockState pBlockState)
	{
		super(pPos, pBlockState);
	}

	public static void update(Level level, BlockPos pos, BlockState state, CrypticSkullBlockEntity block)
	{
		block.tickCount++;
		block.blinkAnimationState.updateWhen(level.hasNeighborSignal(pos), block.tickCount);
	}
	
	@Override
	public ResourceLocation getNoteBlockSound()
	{
		return CrypticSounds.HOWLER_SLEEP.getId();
	}

	@Override
	public BlockEntityType<?> getType()
	{
		return CrypticBlocks.CRYPTIC_SKULL_BLOCK_ENTITY.get();
	}
}
