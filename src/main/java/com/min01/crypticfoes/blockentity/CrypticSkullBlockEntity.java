package com.min01.crypticfoes.blockentity;

import com.min01.crypticfoes.block.CrypticBlocks;
import com.min01.crypticfoes.misc.SmoothAnimationState;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CrypticSkullBlockEntity extends SkullBlockEntity
{
	public int tickCount;
	public final SmoothAnimationState blinkAnimationState = new SmoothAnimationState();
	
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
	public BlockEntityType<?> getType()
	{
		return CrypticBlocks.CRYPTIC_SKULL_BLOCK_ENTITY.get();
	}
}
