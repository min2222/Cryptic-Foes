package com.min01.crypticfoes.entity.renderer;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.entity.living.BumpyEntity;
import com.min01.crypticfoes.entity.model.BumpyModel;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BumpyRenderer extends MobRenderer<BumpyEntity, BumpyModel>
{
	public BumpyRenderer(Context pContext) 
	{
		super(pContext, new BumpyModel(pContext.bakeLayer(BumpyModel.LAYER_LOCATION)), 0.5F);
	}

	@Override
	public ResourceLocation getTextureLocation(BumpyEntity pEntity)
	{
		if(pEntity.isAnimationPlaying(1))
		{
			return ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "textures/entity/bumpy_shocked.png");
		}
		else if(pEntity.isTargetValid())
		{
			return ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "textures/entity/bumpy_mad.png");
		}
		return ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "textures/entity/bumpy_funny.png");
	}
}
