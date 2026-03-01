package com.min01.crypticfoes.entity.renderer;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.entity.living.EntityBumpy;
import com.min01.crypticfoes.entity.model.ModelBumpy;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BumpyRenderer extends MobRenderer<EntityBumpy, ModelBumpy>
{
	public BumpyRenderer(Context pContext) 
	{
		super(pContext, new ModelBumpy(pContext.bakeLayer(ModelBumpy.LAYER_LOCATION)), 0.5F);
	}

	@Override
	public ResourceLocation getTextureLocation(EntityBumpy pEntity)
	{
		return ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "textures/entity/bumpy_funny.png");
	}
}
