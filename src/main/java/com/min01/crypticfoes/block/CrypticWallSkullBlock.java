package com.min01.crypticfoes.block;

import javax.annotation.Nullable;

import com.min01.crypticfoes.blockentity.CrypticSkullBlockEntity;
import com.min01.crypticfoes.misc.CrypticSkullTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CrypticWallSkullBlock extends WallSkullBlock
{
	protected static final VoxelShape HOLWER_SHAPE_SOUTH = Block.box(1.5D, 4.0D, 0.0D, 14.5D, 14.0D, 10.0D);
	protected static final VoxelShape HOLWER_SHAPE_NORTH = Block.box(1.5D, 4.0D, 6.0D, 14.5D, 14.0D, 16.0D);
	
	protected static final VoxelShape HOLWER_SHAPE_EAST = Block.box(0.0D, 4.0D, 1.5D, 10.0D, 14.0D, 14.5D);
	protected static final VoxelShape HOLWER_SHAPE_WEST = Block.box(6.0D, 4.0D, 1.5D, 16.0D, 14.0D, 14.5D);
	
	public CrypticWallSkullBlock(SkullBlock.Type pType, BlockBehaviour.Properties pProperties) 
	{
		super(pType, pProperties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) 
	{
		return new CrypticSkullBlockEntity(pPos, pState);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) 
	{
		if(pLevel.isClientSide) 
		{
			if(pState.is(CrypticBlocks.HOWLER_WALL_HEAD.get())) 
			{
				return createTickerHelper(pBlockEntityType, CrypticBlocks.CRYPTIC_SKULL_BLOCK_ENTITY.get(), CrypticSkullBlockEntity::update);
			}
		}
		return null;
	}
	
	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) 
	{
		if(this.getType() == CrypticSkullTypes.HOWLER)
		{
			Direction direction = pState.getValue(FACING);
			switch(direction)
			{
			case EAST:
				return HOLWER_SHAPE_EAST;
			case NORTH:
				return HOLWER_SHAPE_NORTH;
			case SOUTH:
				return HOLWER_SHAPE_SOUTH;
			case WEST:
				return HOLWER_SHAPE_WEST;
			default:
				break;
			}
		}
		return super.getShape(pState, pLevel, pPos, pContext);
	}
}
