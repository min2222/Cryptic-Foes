package com.min01.crypticfoes.entity.renderer;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.entity.projectile.EntityHowlerScream;
import com.min01.crypticfoes.util.CrypticClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class HowlerScreamRenderer extends EntityRenderer<EntityHowlerScream>
{
	public HowlerScreamRenderer(Context pContext) 
	{
		super(pContext);
	}
	
	@Override
	public void render(EntityHowlerScream pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) 
	{
		pPoseStack.pushPose();
		float scale = 0.25F + (pEntity.tickCount * 0.08F);
		scale = Mth.clamp(scale, 0.0F, 3.0F);
		float xRot = Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot());
		float yRot = Mth.rotLerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot());
		if(pEntity.getOwner() instanceof Player)
		{
			xRot = -xRot;
		}
		pPoseStack.mulPose(Axis.YP.rotationDegrees(yRot));
		pPoseStack.mulPose(Axis.XP.rotationDegrees(xRot));
		pPoseStack.scale(scale, scale, scale);
		pPoseStack.translate(0, 0.5F, 0);
		CrypticClientUtil.drawQuad(pPoseStack, pBuffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(pEntity))), 1.0F, pPackedLight, pEntity.alpha);
		pPoseStack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(EntityHowlerScream pEntity)
	{
		return ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "textures/entity/howler_scream.png");
	}
}