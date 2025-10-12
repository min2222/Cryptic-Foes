package com.min01.crypticfoes.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.extensions.IForgeBlockState;

@Mixin(IForgeBlockState.class)
public interface MixinIForgeBlockState extends IForgeBlockState
{
	@SuppressWarnings("deprecation")
	@Override
	default SoundType getSoundType(LevelReader level, BlockPos pos, @Nullable Entity entity) 
	{
		SoundType original = BlockState.class.cast(this).getBlock().getSoundType(BlockState.class.cast(this), level, pos, entity);
		if(CrypticUtil.isBlockSilenced((Level) level, pos))
		{
			return new SoundType(original.getVolume(), original.getPitch(), SoundEvents.EMPTY, SoundEvents.EMPTY, SoundEvents.EMPTY, SoundEvents.EMPTY, SoundEvents.EMPTY);
		}
		return original;
	}
}
