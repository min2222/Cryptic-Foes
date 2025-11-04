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
	public static final ResourceLocation TEXTURE = new ResourceLocation(CrypticFoes.MODID, "textures/block/screamer.png");
	public static final ResourceLocation TEXTURE_CHARGED = new ResourceLocation(CrypticFoes.MODID, "textures/block/screamer_charged.png");
	public static final ResourceLocation TEXTURE_LAYER = new ResourceLocation(CrypticFoes.MODID, "textures/block/screamer_charged_layer.png");
	public final ModelScreamer model;
	
	public ScreamerRenderer(BlockEntityRendererProvider.Context p_172550_)
	{
		this.model = new ModelScreamer(p_172550_.bakeLayer(ModelScreamer.LAYER_LOCATION));
	}
	
	@Override
	public void render(ScreamerBlockEntity p_112307_, float p_112308_, PoseStack p_112309_, MultiBufferSource p_112310_, int p_112311_, int p_112312_) 
	{
		BlockState blockState = p_112307_.getBlockState();
		
		if(blockState.getValue(ScreamerBlock.CHARGED))
		{
			p_112309_.pushPose();
			p_112309_.translate(0.5F, 0.5F, 0.5F);
			this.rotate(blockState.getValue(BlockStateProperties.HORIZONTAL_FACING), p_112309_);
			p_112309_.scale(-1.0F, -1.0F, 1.0F);
			p_112309_.translate(0.0F, -1.0F, 0.0F);
			this.model.setupAnim(p_112307_, 0, 0, p_112308_ + p_112307_.tickCount, 0, 0);
			this.model.renderToBuffer(p_112309_, p_112310_.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_CHARGED)), p_112311_, p_112312_, 1.0F, 1.0F, 1.0F, 1.0F);
			p_112309_.popPose();
			
			p_112309_.pushPose();
			p_112309_.translate(0.5F, 0.5F, 0.5F);
			this.rotate(blockState.getValue(BlockStateProperties.HORIZONTAL_FACING), p_112309_);
			p_112309_.scale(-1.01F, -1.01F, 1.01F);
			p_112309_.translate(0.0F, -1.0F, 0.0F);
			this.model.setupAnim(p_112307_, 0, 0, p_112308_ + p_112307_.tickCount, 0, 0);
			this.model.renderToBuffer(p_112309_, p_112310_.getBuffer(RenderType.eyes(TEXTURE_LAYER)), p_112311_, p_112312_, 0.5F, 0.5F, 0.5F, 1.0F);
			p_112309_.popPose();
		}
		else
		{
			p_112309_.pushPose();
			p_112309_.translate(0.5F, 0.5F, 0.5F);
			this.rotate(blockState.getValue(BlockStateProperties.HORIZONTAL_FACING), p_112309_);
			p_112309_.scale(-1.0F, -1.0F, 1.0F);
			p_112309_.translate(0.0F, -1.0F, 0.0F);
			this.model.setupAnim(p_112307_, 0, 0, p_112308_ + p_112307_.tickCount, 0, 0);
			this.model.renderToBuffer(p_112309_, p_112310_.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)), p_112311_, p_112312_, 1.0F, 1.0F, 1.0F, 1.0F);
			p_112309_.popPose();
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
