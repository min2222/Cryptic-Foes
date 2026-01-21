package com.min01.crypticfoes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.min01.crypticfoes.misc.CrypticRenderType;
import com.min01.crypticfoes.shader.CrypticEntityEffect;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T>
{
	protected MixinLivingEntityRenderer(Context pContext) 
	{
		super(pContext);
	}

	@Inject(at = @At(value = "HEAD"), method = "getRenderType", cancellable = true)
	protected void getRenderType(T pLivingEntity, boolean pBodyVisible, boolean pTranslucent, boolean pGlowing, CallbackInfoReturnable<RenderType> cir) 
	{
		if(CrypticEntityEffect.INSTANCE.shouldShowEntityEffect())
		{
			cir.setReturnValue(CrypticRenderType.CRYPTIC_ENTITY_CUTOUT_NO_CULL.apply(this.getTextureLocation(pLivingEntity), true));
		}
	}
}
