package com.min01.crypticfoes.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.min01.crypticfoes.effect.CrypticEffects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

@Mixin(MobEffectInstance.class)
public class MixinMobEffectInstance
{
	@Shadow
	@Final
	private MobEffect effect;
	
	@ModifyReturnValue(method = "isVisible", at = @At("RETURN"))
	private boolean isVisible(boolean original)
	{
		if(this.effect == CrypticEffects.STUNNED.get())
		{
			return false;
		}
		return original;
	}
}
