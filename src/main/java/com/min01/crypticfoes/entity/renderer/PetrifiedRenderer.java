package com.min01.crypticfoes.entity.renderer;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.entity.living.EntityPetrified;
import com.min01.crypticfoes.entity.model.ModelPetrified;
import com.min01.crypticfoes.network.CrypticNetwork;
import com.min01.crypticfoes.network.UpdatePosArrayPacket;
import com.min01.crypticfoes.util.CrypticClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class PetrifiedRenderer extends MobRenderer<EntityPetrified, ModelPetrified>
{
	public PetrifiedRenderer(Context pContext)
	{
		super(pContext, new ModelPetrified(pContext.bakeLayer(ModelPetrified.LAYER_LOCATION)), 0.5F);
	}
	
	@Override
	public void render(EntityPetrified pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) 
	{
		super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
		Vec3 pos1 = CrypticClientUtil.getWorldPosition(pEntity, this.model.root(), new Vec3(0.0F, pEntity.yBodyRot, 0.0F), "body", "arms", "stone");
		pEntity.posArray[0] = pos1;
		CrypticNetwork.sendToServer(new UpdatePosArrayPacket(pEntity.getUUID(), pos1, 0));
	}

	@Override
	public ResourceLocation getTextureLocation(EntityPetrified pEntity) 
	{
		return ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "textures/entity/petrified.png");
	}
}
