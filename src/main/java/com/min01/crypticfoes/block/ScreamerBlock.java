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
	public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) 
	{
		return SHAPE;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_)
	{
		return new ScreamerBlockEntity(p_153215_, p_153216_);
	}
	
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) 
	{
		return createTickerHelper(p_153214_, CrypticBlocks.SCREAMER_BLOCK_ENTITY.get(), ScreamerBlockEntity::update);
	}
	
	@Override
	public void neighborChanged(BlockState p_60509_, Level p_60510_, BlockPos p_60511_, Block p_60512_, BlockPos p_60513_, boolean p_60514_) 
	{
		if(p_60510_.hasNeighborSignal(p_60511_))
		{
			p_60510_.setBlockAndUpdate(p_60511_, p_60509_.setValue(ACTIVATE, true));
			p_60510_.playSound(null, p_60511_, CrypticSounds.SCREAMER_WORK.get(), SoundSource.BLOCKS, 0.7F, 1.0F);
		}
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> p_49915_) 
	{
		p_49915_.add(CHARGED, FACING, ACTIVATE);
	}
	
	@Override
	public BlockState rotate(BlockState p_54125_, Rotation p_54126_)
	{
		return p_54125_.setValue(FACING, p_54126_.rotate(p_54125_.getValue(FACING)));
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(BlockState p_54122_, Mirror p_54123_)
	{
		return p_54122_.rotate(p_54123_.getRotation(p_54122_.getValue(FACING)));
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext p_49820_) 
	{
    	Direction direction = p_49820_.getHorizontalDirection().getOpposite();
		return this.defaultBlockState().setValue(FACING, direction);
	}
		   
	@Override
	public void onProjectileHit(Level p_60453_, BlockState p_60454_, BlockHitResult p_60455_, Projectile p_60456_) 
	{
		if(p_60456_ instanceof EntityHowlerScream && !p_60454_.getValue(CHARGED))
		{
			p_60453_.playSound(null, p_60455_.getBlockPos(), CrypticSounds.SCREAMER_SWITCH.get(), SoundSource.BLOCKS, 0.7F, 1.0F);
			p_60453_.setBlockAndUpdate(p_60455_.getBlockPos(), p_60454_.setValue(CHARGED, true));
		}
	}
}
