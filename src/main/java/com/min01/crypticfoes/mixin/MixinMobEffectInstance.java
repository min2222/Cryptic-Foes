package com.min01.crypticfoes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.min01.crypticfoes.effect.CrypticEffects;

import net.minecraft.world.effect.MobEffectInstance;

@Mixin(MobEffectInstance.class)
public class MixinMobEffectInstance
{
	@Inject(at = @At("HEAD"), method = "isVisible", cancellable = true)
	private void isVisible(CallbackInfoReturnable<Boolean> cir)
	{
		MobEffectInstance instance = MobEffectInstance.class.cast(this);
		if(instance.getEffect() == CrypticEffects.STUNNED.get())
		{
			cir.setReturnValue(false);
		}
	}
}
