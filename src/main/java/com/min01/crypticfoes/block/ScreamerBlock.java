package com.min01.crypticfoes.block;

import com.min01.crypticfoes.blockentity.ScreamerBlockEntity;
import com.min01.crypticfoes.entity.projectile.EntityHowlerScream;
import com.min01.crypticfoes.sound.CrypticSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ScreamerBlock extends BaseEntityBlock
{
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty CHARGED = BooleanProperty.create("charged");
	public static final BooleanProperty ACTIVATE = BooleanProperty.create("activate");
	public static final VoxelShape SHAPE = Shapes.or(Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), Block.box(3.0D, 4.0D, 3.0D, 13.0D, 13.0D, 13.0D));
	
	public ScreamerBlock() 
	{
		super(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE).requiresCorrectToolForDrops().noOcclusion());
		this.registerDefaultState(this.stateDefinition.any().setValue(CHARGED, false).setValue(ACTIVATE, false));
	}
	
	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) 
	{
		return SHAPE;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) 
	{
		return new ScreamerBlockEntity(pPos, pState);
	}
	
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) 
	{
		return createTickerHelper(pBlockEntityType, CrypticBlocks.SCREAMER_BLOCK_ENTITY.get(), ScreamerBlockEntity::update);
	}
	
	@Override
	public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston)
	{
		BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
		if(blockEntity instanceof ScreamerBlockEntity screamer)
		{
			if(pLevel.hasNeighborSignal(pPos) && !pState.getValue(ACTIVATE))
			{
				pLevel.setBlockAndUpdate(pPos, pState.setValue(ACTIVATE, true));
			}
			else if(!pLevel.hasNeighborSignal(pPos) && pState.getValue(ACTIVATE))
			{
				if(screamer.tickCount == 0)
				{
					pLevel.setBlockAndUpdate(pPos, pState.setValue(ACTIVATE, false));
				}
			}
		}
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) 
	{
		pBuilder.add(CHARGED, FACING, ACTIVATE);
	}
	
	@Override
	public BlockState rotate(BlockState pState, Rotation pRotation)
	{
		return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(BlockState pState, Mirror pMirror)
	{
		return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) 
	{
    	Direction direction = pContext.getHorizontalDirection().getOpposite();
		return this.defaultBlockState().setValue(FACING, direction);
	}
		   
	@Override
	public void onProjectileHit(Level pLevel, BlockState pState, BlockHitResult pHit, Projectile pProjectile) 
	{
		if(pProjectile instanceof EntityHowlerScream && !pState.getValue(CHARGED))
		{
			pLevel.playSound(null, pHit.getBlockPos(), CrypticSounds.SCREAMER_SWITCH.get(), SoundSource.BLOCKS, 0.7F, 1.0F);
			pLevel.setBlockAndUpdate(pHit.getBlockPos(), pState.setValue(CHARGED, true));
		}
	}
}
