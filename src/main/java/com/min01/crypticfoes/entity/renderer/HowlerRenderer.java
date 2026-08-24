package com.min01.crypticfoes.entity.renderer;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.api.client.ModelPartChain;
import com.min01.crypticfoes.entity.living.HowlerEntity;
import com.min01.crypticfoes.entity.model.HowlerModel;
import com.min01.crypticfoes.entity.renderer.layer.HowlerLayer;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class HowlerRenderer extends MobRenderer<HowlerEntity, HowlerModel>
{
	public HowlerRenderer(Context pContext)
	{
		super(pContext, new HowlerModel(pContext.bakeLayer(HowlerModel.LAYER_LOCATION)), 0.5F);
		this.addLayer(new HowlerLayer(this));
	}
	
	@Override
	public void render(HowlerEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) 
	{
		super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
		ModelPartChain.setPos(pEntity, this.model.root());
	}

	@Override
	public ResourceLocation getTextureLocation(HowlerEntity pEntity) 
	{
		String variant  = "";
		if(pEntity.hasCustomName())
		{
			Component component = pEntity.getCustomName();
			String name = component.getString();
			if(name.equals("Sonar") || name.equals("Fruit"))
			{
				variant = "_" + name.toLowerCase();
			}
		}
		return ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "textures/entity/howler" + variant + ".png");
	}
}
