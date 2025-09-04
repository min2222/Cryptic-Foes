package com.min01.crypticfoes.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.GameEventTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;

@Mixin(VibrationSystem.User.class)
public interface MixinVibrationSystem extends VibrationSystem.User 
{
	@Override
	default boolean isValidVibration(GameEvent p_282750_, GameEvent.Context p_283373_) 
	{
		if(!p_282750_.is(this.getListenableEvents())) 
		{
			return false;
		}
		else 
		{
			Entity entity = p_283373_.sourceEntity();
			if(entity != null) 
			{
				if(entity.isSpectator())
				{
					return false;
				}
				if(entity.isSteppingCarefully() && p_282750_.is(GameEventTags.IGNORE_VIBRATIONS_SNEAKING)) 
				{
					if(this.canTriggerAvoidVibration() && entity instanceof ServerPlayer) 
					{
						ServerPlayer serverplayer = (ServerPlayer) entity;
						CriteriaTriggers.AVOID_VIBRATION.trigger(serverplayer);
					}
					return false;
				}
				if(entity.dampensVibrations() || CrypticUtil.isBlockSilenced(entity.level, entity.getOnPos()))
				{
					return false;
				}
			}
			if(p_283373_.affectedState() != null)
			{
				return !p_283373_.affectedState().is(BlockTags.DAMPENS_VIBRATIONS);
			}
			else 
			{
				return true;
			}
		}
	}
}
