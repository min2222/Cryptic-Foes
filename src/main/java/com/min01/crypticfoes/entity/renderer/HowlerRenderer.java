package com.min01.crypticfoes.entity.renderer;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.entity.living.EntityHowler;
import com.min01.crypticfoes.entity.model.ModelHowler;
import com.min01.crypticfoes.entity.renderer.layer.HowlerLayer;
import com.min01.crypticfoes.network.CrypticNetwork;
import com.min01.crypticfoes.network.UpdatePosArrayPacket;
import com.min01.crypticfoes.util.CrypticClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class HowlerRenderer extends MobRenderer<EntityHowler, ModelHowler>
{
	public HowlerRenderer(Context pContext)
	{
		super(pContext, new ModelHowler(pContext.bakeLayer(ModelHowler.LAYER_LOCATION)), 0.5F);
		this.addLayer(new HowlerLayer(this));
	}
	
	@Override
	public void render(EntityHowler pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) 
	{
		super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, CrypticClientUtil.MC.renderBuffers().outlineBufferSource(), pPackedLight);
		Vec3 pos1 = CrypticClientUtil.getWorldPosition(pEntity, this.model.root(), new Vec3(0.0F, pEntity.yBodyRot, 0.0F), "howlersleeppivot", "howler", "body", "body_no_hands", "head");
		pEntity.posArray[0] = pos1;
		CrypticNetwork.sendToServer(new UpdatePosArrayPacket(pEntity.getUUID(), pos1, 0));
	}

	@Override
	public ResourceLocation getTextureLocation(EntityHowler pEntity) 
	{
		if(pEntity.hasCustomName())
		{
			if(pEntity.getCustomName().getString().equals("Sonar"))
			{
				return ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "textures/entity/howler_sonar.png");
			}
			if(pEntity.getCustomName().getString().equals("Fruit"))
			{
				return ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "textures/entity/howler_fruit.png");
			}
		}
		return ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "textures/entity/howler.png");
	}
}
