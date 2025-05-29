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
	public PetrifiedStoneRenderer(Context p_174008_) 
	{
		super(p_174008_);
		this.model = new ModelPetrifiedStone(p_174008_.bakeLayer(ModelPetrifiedStone.LAYER_LOCATION));
	}
	
	@Override
	public void render(EntityPetrifiedStone p_114485_, float p_114486_, float p_114487_, PoseStack p_114488_, MultiBufferSource p_114489_, int p_114490_) 
	{
		p_114488_.pushPose();
		p_114488_.scale(-1.0F, -1.0F, 1.0F);
		p_114488_.translate(0.0F, -1.5F, 0.0F);
		p_114488_.mulPose(Axis.XP.rotationDegrees(p_114485_.tickCount * 0.01F));
		this.model.renderToBuffer(p_114488_, p_114489_.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(p_114485_))), p_114490_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		p_114488_.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(EntityPetrifiedStone p_114482_)
	{
		return new ResourceLocation(CrypticFoes.MODID, "textures/entity/petrified.png");
	}

}
