package com.min01.crypticfoes.entity.renderer;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.entity.model.BallModel;
import com.min01.crypticfoes.entity.projectile.BallEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;

public class BallRenderer extends EntityRenderer<BallEntity>
{
	public final BallModel model;
	
	public BallRenderer(Context pContext) 
	{
		super(pContext);
		this.model = new BallModel(pContext.bakeLayer(BallModel.LAYER_LOCATION));
	}
	
	@Override
	public void render(BallEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) 
	{
		ItemStack stack = pEntity.getItem();
		int color = 0xFFFFFF;
		if(stack.getItem() instanceof DyeableLeatherItem dyeable)
		{
		    color = dyeable.getColor(stack);
		}
		float red = (float)(color >> 16 & 255) / 255.0F;
		float green = (float)(color >> 8 & 255) / 255.0F;
		float blue = (float)(color & 255) / 255.0F;
		
		pPoseStack.pushPose();
		pPoseStack.scale(-1.0F, -1.0F, 1.0F);
		pPoseStack.translate(0.0F, -1.5F, 0.0F);
		this.model.renderToBuffer(pPoseStack, pBuffer.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(pEntity))), pPackedLight, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
		pPoseStack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(BallEntity pEntity)
	{
		return ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "textures/entity/ball.png");
	}
}