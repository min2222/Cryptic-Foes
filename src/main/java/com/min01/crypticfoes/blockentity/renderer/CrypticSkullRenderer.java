package com.min01.crypticfoes.blockentity.renderer;

import java.util.Map;

import javax.annotation.Nullable;

import com.min01.crypticfoes.block.model.CrypticSkullModelBase;
import com.min01.crypticfoes.blockentity.CrypticSkullBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;

public class CrypticSkullRenderer extends SkullBlockRenderer
{
	private final Map<SkullBlock.Type, SkullModelBase> modelByType;
	   
	public CrypticSkullRenderer(Context pContext) 
	{
		super(pContext);
		this.modelByType = createSkullRenderers(pContext.getModelSet());
	}

	@Override
	public void render(SkullBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay)
	{
		float f = pBlockEntity.getAnimation(pPartialTick);
		BlockState blockState = pBlockEntity.getBlockState();
		boolean flag = blockState.getBlock() instanceof WallSkullBlock;
		Direction direction = flag ? blockState.getValue(WallSkullBlock.FACING) : null;
		int i = flag ? RotationSegment.convertToSegment(direction.getOpposite()) : blockState.getValue(SkullBlock.ROTATION);
		float f1 = RotationSegment.convertToDegrees(i);
		SkullBlock.Type type = ((AbstractSkullBlock) blockState.getBlock()).getType();
		SkullModelBase skullBase = this.modelByType.get(type);
		RenderType renderType = getRenderType(type, pBlockEntity.getOwnerProfile());
		if(skullBase instanceof CrypticSkullModelBase base && pBlockEntity instanceof CrypticSkullBlockEntity blockEntity)
		{
			renderSkull(blockEntity, pPartialTick, direction, f1, f, pPoseStack, pBuffer, pPackedLight, base, renderType);
		}
	}

	public static void renderSkull(CrypticSkullBlockEntity blockEntity, float partialTicks, @Nullable Direction pDirection, float pYRot, float pMouthAnimation, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, CrypticSkullModelBase pModel, RenderType pRenderType) 
	{
		pPoseStack.pushPose();
		if(pDirection == null)
		{
			pPoseStack.translate(0.5F, 0.0F, 0.5F);
		} 
		else
		{
			pPoseStack.translate(0.5F - (float) pDirection.getStepX() * 0.25F, 0.25F,	0.5F - (float) pDirection.getStepZ() * 0.25F);
		}
		pPoseStack.scale(-1.0F, -1.0F, 1.0F);
		VertexConsumer consumer = pBufferSource.getBuffer(pRenderType);
		pModel.setupAnim(blockEntity, partialTicks + blockEntity.tickCount);
		pModel.setupAnim(pMouthAnimation, pYRot, 0.0F);
		pModel.renderToBuffer(pPoseStack, consumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		pPoseStack.popPose();
	}
}
