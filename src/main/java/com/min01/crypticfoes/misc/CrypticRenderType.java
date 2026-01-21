package com.min01.crypticfoes.misc;

import java.util.function.BiFunction;

import com.min01.crypticfoes.shader.CrypticEntityEffect;
import com.min01.crypticfoes.util.CrypticClientUtil;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;

import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class CrypticRenderType extends RenderType
{
	public static final RenderStateShard.OutputStateShard ENTITY_OUTPUT = new RenderStateShard.OutputStateShard("entity_target", () -> 
    {
        RenderTarget target = CrypticEntityEffect.INSTANCE.entityTarget;
        if(target != null) 
        {
            target.copyDepthFrom(CrypticClientUtil.MC.getMainRenderTarget());
            target.bindWrite(false);
        }
    }, 
    () ->
    {
    	CrypticClientUtil.MC.getMainRenderTarget().bindWrite(false);
    });
	
	public CrypticRenderType(String pName, VertexFormat pFormat, Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, Runnable pSetupState, Runnable pClearState) 
	{
		super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
	}
	
	public static final BiFunction<ResourceLocation, Boolean, RenderType> CRYPTIC_ENTITY_CUTOUT_NO_CULL = Util.memoize((pLocation, pOutline) -> 
	{
		RenderType.CompositeState compositeState = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER).setTextureState(new RenderStateShard.TextureStateShard(pLocation, false, false)).setTransparencyState(NO_TRANSPARENCY).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).setOutputState(ENTITY_OUTPUT).createCompositeState(pOutline);
		return create("cryptic_entity_cutout_no_cull", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, compositeState);
	});
	
	public static final BiFunction<ResourceLocation, Boolean, RenderType> CRYPTIC_ENTITY_TRANSLUCENT = Util.memoize((pLocation, pOutline) ->
	{
		RenderType.CompositeState compositeState = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(pLocation, false, false)).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).setOutputState(ENTITY_OUTPUT).createCompositeState(pOutline);
		return create("cryptic_entity_translucent", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, compositeState);
	});
}
