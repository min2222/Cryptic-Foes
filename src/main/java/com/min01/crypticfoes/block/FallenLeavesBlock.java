package com.min01.crypticfoes.block;

import java.util.List;

import com.min01.crypticfoes.entity.living.EntityBrancher;
import com.min01.crypticfoes.sound.CrypticSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FallenLeavesBlock extends BushBlock
{
	protected static final VoxelShape AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 0.1D, 16.0D);
	
	public FallenLeavesBlock() 
	{
		super(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_LEAVES).sound(CrypticSounds.FALLEN_LEAVES).instabreak());
	}
	
	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext)
	{
		return AABB;
	}
	
	@Override
	public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity)
	{
		if(!pEntity.isSteppingCarefully() && pEntity instanceof LivingEntity living)
		{
			if(!(living instanceof EntityBrancher))
			{
				living.playSound(CrypticSounds.FALLEN_LEAVES_STEP.get());
				if(EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(living))
				{
					List<EntityBrancher> list = pLevel.getEntitiesOfClass(EntityBrancher.class, living.getBoundingBox().inflate(12.5F), t -> !t.isAngry() && t.getAnimationState() != 1 && t.getAnimationTick() <= 0);
					list.forEach(t -> 
					{
						t.setTarget(living);
						t.setAngerCount(t.getAngerCount() + 1);
						t.setAnimationState(1);
						t.setAnimationTick(20);
					});
				}
			}
		}
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) 
	{
		return super.mayPlaceOn(pState, pLevel, pPos) || pState.is(Blocks.GRASS_BLOCK);
	}
}
