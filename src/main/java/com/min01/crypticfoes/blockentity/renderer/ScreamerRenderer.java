package com.min01.crypticfoes.blockentity.renderer;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.block.ScreamerBlock;
import com.min01.crypticfoes.block.model.ModelScreamer;
import com.min01.crypticfoes.blockentity.ScreamerBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ScreamerRenderer implements BlockEntityRenderer<ScreamerBlockEntity>
{
	public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "textures/block/screamer.png");
	public static final ResourceLocation TEXTURE_CHARGED = ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "textures/block/screamer_charged.png");
	public static final ResourceLocation TEXTURE_LAYER = ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "textures/block/screamer_charged_layer.png");
	public final ModelScreamer model;
	
	public ScreamerRenderer(BlockEntityRendererProvider.Context ctx)
	{
		this.model = new ModelScreamer(ctx.bakeLayer(ModelScreamer.LAYER_LOCATION));
	}
	
	@Override
	public void render(ScreamerBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay)
	{
		BlockState blockState = pBlockEntity.getBlockState();
		if(blockState.getValue(ScreamerBlock.CHARGED))
		{
			pPoseStack.pushPose();
			pPoseStack.translate(0.5F, 0.5F, 0.5F);
			this.rotate(blockState.getValue(BlockStateProperties.HORIZONTAL_FACING), pPoseStack);
			pPoseStack.scale(-1.0F, -1.0F, 1.0F);
			pPoseStack.translate(0.0F, -1.0F, 0.0F);
			this.model.setupAnim(pBlockEntity, 0, 0, pPartialTick + pBlockEntity.tickCount, 0, 0);
			this.model.renderToBuffer(pPoseStack, pBuffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_CHARGED)), pPackedLight, pPackedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
			pPoseStack.popPose();
			
			pPoseStack.pushPose();
			pPoseStack.translate(0.5F, 0.5F, 0.5F);
			this.rotate(blockState.getValue(BlockStateProperties.HORIZONTAL_FACING), pPoseStack);
			pPoseStack.scale(-1.01F, -1.01F, 1.01F);
			pPoseStack.translate(0.0F, -1.0F, 0.0F);
			this.model.setupAnim(pBlockEntity, 0, 0, pPartialTick + pBlockEntity.tickCount, 0, 0);
			this.model.renderToBuffer(pPoseStack, pBuffer.getBuffer(RenderType.eyes(TEXTURE_LAYER)), pPackedLight, pPackedOverlay, 0.5F, 0.5F, 0.5F, 1.0F);
			pPoseStack.popPose();
		}
		else
		{
			pPoseStack.pushPose();
			pPoseStack.translate(0.5F, 0.5F, 0.5F);
			this.rotate(blockState.getValue(BlockStateProperties.HORIZONTAL_FACING), pPoseStack);
			pPoseStack.scale(-1.0F, -1.0F, 1.0F);
			pPoseStack.translate(0.0F, -1.0F, 0.0F);
			this.model.setupAnim(pBlockEntity, 0, 0, pPartialTick + pBlockEntity.tickCount, 0, 0);
			this.model.renderToBuffer(pPoseStack, pBuffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)), pPackedLight, pPackedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
			pPoseStack.popPose();
		}
	}
	
	public void rotate(Direction direction, PoseStack poseStack)
	{
		switch(direction)
		{
		case DOWN:
			break;
		case EAST:
			poseStack.mulPose(Axis.YP.rotationDegrees(90));
			break;
		case NORTH:
			break;
		case SOUTH:
			poseStack.mulPose(Axis.YP.rotationDegrees(180));
			break;
		case UP:
			break;
		case WEST:
			poseStack.mulPose(Axis.YP.rotationDegrees(270));
			break;
		default:
			break;
		}
	}
}
