package com.min01.crypticfoes.entity.renderer.layer;

import com.min01.crypticfoes.misc.CrypticRenderType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class GlowingLayer<T extends Entity, M extends EntityModel<T>> extends RenderLayer<T, M>
{
	public final M model;
	public final ResourceLocation texture;
	public final Vec3 color;
	
	public GlowingLayer(RenderLayerParent<T, M> renderer, M model, ResourceLocation texture) 
	{
		this(renderer, model, texture, new Vec3(1.0F, 1.0F, 1.0F));
	}
	
	public GlowingLayer(RenderLayerParent<T, M> renderer, M model, ResourceLocation texture, Vec3 color)
	{
		super(renderer);
		this.model = model;
		this.texture = texture;
		this.color = color;
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
	{
		this.coloredGlowingModelCopyLayerRender(this.getParentModel(), this.model, this.texture, poseStack, bufferIn, packedLightIn, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, (float) this.color.x, (float) this.color.y, (float) this.color.z);
	}
	
	public void coloredGlowingModelCopyLayerRender(M model1, M model2, ResourceLocation texture, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float r, float g, float b)
	{
		if(!entity.isInvisible())
		{
			model1.copyPropertiesTo(model2);
			model2.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
			this.renderColoredGlowingModel(model2, texture, poseStack, bufferSource, packedLight, entity, r, g, b);
		}
	}

	public void renderColoredGlowingModel(M model, ResourceLocation texture, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float r, float g, float b)
	{
		VertexConsumer consumer = bufferSource.getBuffer(this.getRenderType(texture));
		int overlayCoords = entity instanceof LivingEntity living ? LivingEntityRenderer.getOverlayCoords(living, 0.0F) : OverlayTexture.NO_OVERLAY;
		model.renderToBuffer(poseStack, consumer, packedLight, overlayCoords, r, g, b, this.getAlpha(entity));
	}
	
	public RenderType getRenderType(ResourceLocation texture)
	{
		return CrypticRenderType.eyesFix(texture);
	}
	
	public float getAlpha(T entity)
	{
		return 1.0F;
	}
}
