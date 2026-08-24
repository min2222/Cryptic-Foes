package com.min01.crypticfoes.entity.renderer.layer;

import com.min01.crypticfoes.misc.CrypticRenderType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class GlowingLayer<T extends Entity, M extends EntityModel<T>> extends RenderLayer<T, M>
{
	public final ResourceLocation texture;
	public final float color;

	public GlowingLayer(RenderLayerParent<T, M> renderer, ResourceLocation texture)
	{
		this(renderer, texture, 1.0F);
	}
	
	public GlowingLayer(RenderLayerParent<T, M> renderer, ResourceLocation texture, float color)
	{
		super(renderer);
		this.texture = texture;
		this.color = color;
	}

	@Override
	public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) 
	{
		VertexConsumer consumer = pBuffer.getBuffer(CrypticRenderType.eyesFix(this.texture));
		this.getParentModel().renderToBuffer(pPoseStack, consumer, LightTexture.FULL_SKY, OverlayTexture.NO_OVERLAY, this.color, this.color, this.color, 1.0F);
	}
}
