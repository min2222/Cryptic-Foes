package com.min01.crypticfoes.entity.renderer.layer;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.entity.living.EntityHowler;
import com.min01.crypticfoes.entity.model.ModelHowler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class HowlerLayer extends RenderLayer<EntityHowler, ModelHowler>
{
	public HowlerLayer(RenderLayerParent<EntityHowler, ModelHowler> pRenderer)
	{
		super(pRenderer);
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, EntityHowler entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
	{
		if(!entity.isInvisible())
		{
			VertexConsumer consumer = bufferIn.getBuffer(RenderType.eyes(ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "textures/entity/howler_layer.png")));
			this.getParentModel().renderToBuffer(poseStack, consumer, packedLightIn, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
		}
	}
}
