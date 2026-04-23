package com.min01.crypticfoes.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class WeightBlock extends HorizontalDirectionalBlock implements Fallable
{
	public WeightBlock(Properties pProperties) 
	{
		super(pProperties);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) 
	{
		return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder)
	{
		pBuilder.add(FACING);
	}
	
	@Override
	public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) 
	{
		pLevel.scheduleTick(pPos, this, 2);
	}

	@Override
	public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos)
	{
		pLevel.scheduleTick(pCurrentPos, this, 2);
		return pState;
	}

	@Override
	public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) 
	{
		if(FallingBlock.isFree(pLevel.getBlockState(pPos.below())) && pPos.getY() >= pLevel.getMinBuildHeight())
		{
			FallingBlockEntity entity = FallingBlockEntity.fall(pLevel, pPos, pState);
			entity.setHurtsEntities(2.0F, 40);
		}
	}
	
	@Override
	public void onLand(Level level, BlockPos pos, BlockState state, BlockState replaceableState, FallingBlockEntity fallingBlock)
	{
		if(level.isClientSide)
		{
			return;
		}
		float fallDistance = Math.max(0.0F, fallingBlock.fallDistance - 1.0F);
		float breakPower = Mth.clamp(1.5F + (fallDistance * 0.2F), 1.5F, 50.0F);
		int radius = Mth.clamp(1 + (int)(fallDistance / 10.0F), 1, 3);
		BlockPos center = pos.below();
		for(int y = -1; y <= 0; ++y)
		{
			for(int x = -radius; x <= radius; ++x)
			{
				for(int z = -radius; z <= radius; ++z)
				{
					BlockPos targetPos = center.offset(x, y, z);
					BlockState targetState = level.getBlockState(targetPos);
					if(targetState.isAir())
					{
						continue;
					}
					float destroySpeed = targetState.getDestroySpeed(level, targetPos);
					if(destroySpeed >= 0.0F && destroySpeed <= breakPower)
					{
						level.destroyBlock(targetPos, true);
					}
				}
			}
		}
	}
}
