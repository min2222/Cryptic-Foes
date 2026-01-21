package com.min01.crypticfoes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.min01.crypticfoes.misc.CrypticRenderType;
import com.min01.crypticfoes.shader.CrypticEntityEffect;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

@Mixin(RenderType.class)
public class MixinRenderType
{
	@Inject(at = @At(value = "HEAD"), method = "entityTranslucent(Lnet/minecraft/resources/ResourceLocation;Z)Lnet/minecraft/client/renderer/RenderType;", cancellable = true)
	private static void entityTranslucent(ResourceLocation pLocation, boolean pOutline, CallbackInfoReturnable<RenderType> cir) 
	{
		if(CrypticEntityEffect.INSTANCE.shouldShowEntityEffect())
		{
			cir.setReturnValue(CrypticRenderType.CRYPTIC_ENTITY_TRANSLUCENT.apply(pLocation, pOutline));
		}
	}
}
