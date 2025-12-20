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

@Mixin(value = VibrationSystem.User.class, priority = -10000)
public interface MixinVibrationSystem extends VibrationSystem.User 
{
	@Override
	default boolean isValidVibration(GameEvent pGameEvent, GameEvent.Context pContext) 
	{
		if(!pGameEvent.is(this.getListenableEvents())) 
		{
			return false;
		}
		else 
		{
			Entity entity = pContext.sourceEntity();
			if(entity != null) 
			{
				if(entity.isSpectator())
				{
					return false;
				}
				if(entity.isSteppingCarefully() && pGameEvent.is(GameEventTags.IGNORE_VIBRATIONS_SNEAKING)) 
				{
					if(this.canTriggerAvoidVibration() && entity instanceof ServerPlayer serverPlayer) 
					{
						CriteriaTriggers.AVOID_VIBRATION.trigger(serverPlayer);
					}
					return false;
				}
				if(entity.dampensVibrations() || CrypticUtil.isBlockSilenced(entity.level, entity.getOnPos()))
				{
					return false;
				}
			}
			if(pContext.affectedState() != null)
			{
				return !pContext.affectedState().is(BlockTags.DAMPENS_VIBRATIONS);
			}
			else 
			{
				return true;
			}
		}
	}
}
