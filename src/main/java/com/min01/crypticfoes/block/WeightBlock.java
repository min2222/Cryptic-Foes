package com.min01.crypticfoes.block;

import java.util.HashSet;
import java.util.Set;

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
		int startY = fallingBlock.getStartPos().getY();
		int landY = pos.getY();
		int fallBlocks = Math.max(0, startY - landY);
		if(fallBlocks <= 3)
		{
			return;
		}
		float breakPower = Mth.clamp((fallBlocks - 3) * 0.25F, 0.0F, 40.0F);
		float blastRadius = Mth.clamp(0.8F + (fallBlocks - 3) * 0.12F, 0.8F, 4.5F);
		Set<BlockPos> toBreak = this.collectBlocksToBreak(level, pos.below(), breakPower, blastRadius, level.random);
		toBreak.forEach(targetPos ->
		{
			BlockState targetState = level.getBlockState(targetPos);
			if(!targetState.isAir() && targetState != state)
			{
				level.destroyBlock(targetPos, true);
			}
		});
	}
	
	private Set<BlockPos> collectBlocksToBreak(Level level, BlockPos center, float breakPower, float blastRadius, RandomSource random)
	{
		Set<BlockPos> result = new HashSet<>();
		int grid = 16;
		float step = 0.3F;
		for(int x = 0; x < grid; ++x)
		{
			for(int y = 0; y < grid; ++y)
			{
				for(int z = 0; z < grid; ++z)
				{
					if(x != 0 && x != grid - 1 && y != 0 && y != grid - 1 && z != 0 && z != grid - 1)
					{
						continue;
					}
					double dx = (float)x / 15.0F * 2.0F - 1.0F;
					double dy = (float)y / 15.0F * 2.0F - 1.0F;
					double dz = (float)z / 15.0F * 2.0F - 1.0F;
					double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
					dx /= len;
					dy /= len;
					dz /= len;
					float strength = blastRadius * (0.7F + random.nextFloat() * 0.6F);
					double px = center.getX() + 0.5D;
					double py = center.getY() + 0.5D;
					double pz = center.getZ() + 0.5D;
					while(strength > 0.0F)
					{
						BlockPos targetPos = BlockPos.containing(px, py, pz);
						if(!level.isInWorldBounds(targetPos))
						{
							break;
						}
						BlockState targetState = level.getBlockState(targetPos);
						if(!targetState.isAir())
						{
							float destroySpeed = targetState.getDestroySpeed(level, targetPos);
							if(destroySpeed >= 0.0F)
							{
								strength -= (destroySpeed + 0.3F) * 0.3F;
								if(destroySpeed <= breakPower && strength > 0.0F)
								{
									result.add(targetPos.immutable());
								}
							}
						}
						px += dx * (double)step;
						py += dy * (double)step;
						pz += dz * (double)step;
						strength -= 0.225F;
					}
				}
			}
		}
		return result;
	}
}
