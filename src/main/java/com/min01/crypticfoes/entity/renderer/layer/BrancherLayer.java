package com.min01.crypticfoes.entity.renderer.layer;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.entity.living.EntityBrancher;
import com.min01.crypticfoes.entity.model.ModelBrancher;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class BrancherLayer extends RenderLayer<EntityBrancher, ModelBrancher>
{
	public BrancherLayer(RenderLayerParent<EntityBrancher, ModelBrancher> pRenderer)
	{
		super(pRenderer);
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, EntityBrancher entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
	{
		if(!entity.isInvisible() && entity.getAngerCount() > 0)
		{
			VertexConsumer consumer = bufferIn.getBuffer(RenderType.eyes(ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "textures/entity/brancher_layer.png")));
	        float strength = 0.5F + Mth.clamp(((float) Math.cos((entity.glowingTicks + ageInTicks) * 0.1F)) - 0.5F, -0.5F, 0.5F);

	        strength += Mth.lerp(ageInTicks, entity.brightnessOld, entity.brightness) * Mth.PI;
	        strength = Mth.clamp(strength, 0.0F, 1.0F);
	        strength *= entity.getAngerCount() / 2.0F;
	        if(entity.explosionTick > 0)
	        {
	        	strength = 1.0F;
	        }
			this.getParentModel().renderToBuffer(poseStack, consumer, packedLightIn, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), strength, strength, strength, 1.0F);
		}
	}
}
