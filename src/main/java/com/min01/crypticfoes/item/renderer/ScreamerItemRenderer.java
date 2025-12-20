package com.min01.crypticfoes.item.renderer;

import com.min01.crypticfoes.block.CrypticBlocks;
import com.min01.crypticfoes.blockentity.ScreamerBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ScreamerItemRenderer extends BlockEntityWithoutLevelRenderer
{
	private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
	private final ScreamerBlockEntity blockEntity;
	   
	public ScreamerItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet)
	{
		super(dispatcher, modelSet);
		this.blockEntityRenderDispatcher = dispatcher;
		this.blockEntity = new ScreamerBlockEntity(BlockPos.ZERO, CrypticBlocks.SCREAMER.get().defaultBlockState());
	}
	
	@Override
	public void renderByItem(ItemStack pStack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay)
	{
		pPoseStack.pushPose();
		this.blockEntityRenderDispatcher.renderItem(this.blockEntity, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
		pPoseStack.popPose();
	}
}
