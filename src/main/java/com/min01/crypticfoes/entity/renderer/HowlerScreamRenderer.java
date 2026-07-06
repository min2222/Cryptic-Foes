package com.min01.crypticfoes.entity.renderer;

import org.joml.Vector2f;
import org.joml.Vector4f;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.entity.projectile.HowlerScreamEntity;
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

public class HowlerScreamRenderer extends EntityRenderer<HowlerScreamEntity>
{
	public HowlerScreamRenderer(Context pContext) 
	{
		super(pContext);
	}
	
	@Override
	public void render(HowlerScreamEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) 
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
		CrypticClientUtil.drawQuad(pPoseStack, pBuffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(pEntity))), new Vector2f(1.0F, 1.0F), new Vector4f(1.0F, 1.0F, 1.0F, pEntity.alpha), pPackedLight);
		pPoseStack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(HowlerScreamEntity pEntity)
	{
		return ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "textures/entity/howler_scream.png");
	}
}