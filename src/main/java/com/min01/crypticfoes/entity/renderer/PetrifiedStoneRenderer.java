package com.min01.crypticfoes.entity.renderer;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.entity.model.ModelPetrifiedStone;
import com.min01.crypticfoes.entity.projectile.EntityPetrifiedStone;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class PetrifiedStoneRenderer extends EntityRenderer<EntityPetrifiedStone>
{
	public final ModelPetrifiedStone model;
	public PetrifiedStoneRenderer(Context pContext) 
	{
		super(pContext);
		this.model = new ModelPetrifiedStone(pContext.bakeLayer(ModelPetrifiedStone.LAYER_LOCATION));
	}
	
	@Override
	public void render(EntityPetrifiedStone pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) 
	{
		pPoseStack.pushPose();
		pPoseStack.scale(-1.0F, -1.0F, 1.0F);
		pPoseStack.translate(0.0F, -1.5F, 0.0F);
		pPoseStack.mulPose(Axis.XP.rotationDegrees(pEntity.tickCount * 0.01F));
		this.model.renderToBuffer(pPoseStack, pBuffer.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(pEntity))), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		pPoseStack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(EntityPetrifiedStone pEntity)
	{
		return ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "textures/entity/petrified.png");
	}
}
