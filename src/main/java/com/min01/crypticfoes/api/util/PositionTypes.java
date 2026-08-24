package com.min01.crypticfoes.api.util;

import java.util.function.BiPredicate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;

public enum PositionTypes
{
	CEILING(Direction.UP, (level, mutablePos) ->
	{
		return (level.getBlockState(mutablePos).isAir() || !level.getFluidState(mutablePos).isEmpty() || !level.getBlockState(mutablePos).isCollisionShapeFullBlock(level, mutablePos)) && mutablePos.getY() < level.getMaxBuildHeight();
	}),
	GROUND(Direction.DOWN, (level, mutablePos) -> 
	{
		return (level.getBlockState(mutablePos).isAir() || !level.getFluidState(mutablePos).isEmpty() || !level.getBlockState(mutablePos).isCollisionShapeFullBlock(level, mutablePos)) && mutablePos.getY() > level.getMinBuildHeight();
	}),
	SURFACE(Direction.DOWN, (level, mutablePos) -> 
	{
		return level.getBlockState(mutablePos).isAir() && mutablePos.getY() > level.getMinBuildHeight();
	});
	
	private final Direction direction;
	private final BiPredicate<BlockGetter, BlockPos> predicate;
	
	private PositionTypes(Direction direction, BiPredicate<BlockGetter, BlockPos> predicate) 
	{
		this.direction = direction;
		this.predicate = predicate;
	}
	
	public Direction getDirection() 
	{
		return this.direction;
	}
	
	public boolean test(BlockGetter level, BlockPos pos)
	{
		return this.predicate.test(level, pos);
	}
}
