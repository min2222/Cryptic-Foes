package com.min01.crypticfoes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.Vec3;

@Mixin(value = VibrationSystem.Listener.class, priority = -10000)
public class MixinVibrationSystem
{
	@Inject(at = @At("HEAD"), method = "handleGameEvent", cancellable = true)
    public void handleGameEvent(ServerLevel pLevel, GameEvent pGameEvent, GameEvent.Context pContext, Vec3 pPos, CallbackInfoReturnable<Boolean> cir) 
    {
		if(pContext.sourceEntity() != null)
		{
	    	if(CrypticUtil.isBlockSilenced(pLevel, pContext.sourceEntity().getOnPos()))
	    	{
	    		cir.setReturnValue(false);
	    	}
		}
    	if(CrypticUtil.isBlockSilenced(pLevel, BlockPos.containing(pPos)))
    	{
    		cir.setReturnValue(false);
    	}
    }
}
