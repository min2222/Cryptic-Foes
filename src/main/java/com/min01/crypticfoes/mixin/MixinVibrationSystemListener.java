package com.min01.crypticfoes.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.Vec3;

@Mixin(VibrationSystem.Listener.class)
public class MixinVibrationSystemListener
{
	@WrapMethod(method = "handleGameEvent")
	private boolean crypticfoes$handleGameEvent(ServerLevel pLevel, GameEvent pGameEvent, GameEvent.Context pContext, Vec3 pPos, Operation<Boolean> original)
	{
        //TODO silencing blend;
		return original.call(pLevel, pGameEvent, pContext, pPos);
	}
}
