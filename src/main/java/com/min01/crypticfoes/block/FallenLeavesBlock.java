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
		super(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_LEAVES).sound(CrypticSounds.FALLEN_LEAVES).noCollission().instabreak());
	}
	
	@Override
	public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_)
	{
		return AABB;
	}
	
	@Override
	public void stepOn(Level p_152431_, BlockPos p_152432_, BlockState p_152433_, Entity p_152434_)
	{
		if(!p_152434_.isSteppingCarefully() && p_152434_ instanceof LivingEntity living)
		{
			if(!(living instanceof EntityBrancher))
			{
				living.playSound(CrypticSounds.FALLEN_LEAVES_STEP.get());
				List<EntityBrancher> list = p_152431_.getEntitiesOfClass(EntityBrancher.class, living.getBoundingBox().inflate(12.5F), t -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(t) && !t.isAngry() && t.getAnimationState() != 1 && t.getAnimationTick() <= 0);
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
	
	@Override
	protected boolean mayPlaceOn(BlockState p_51042_, BlockGetter p_51043_, BlockPos p_51044_) 
	{
		return super.mayPlaceOn(p_51042_, p_51043_, p_51044_) || p_51042_.is(Blocks.GRASS_BLOCK);
	}
}
