package com.min01.crypticfoes.shader;

import java.io.IOException;

import com.google.gson.JsonSyntaxException;
import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.util.CrypticClientUtil;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;

public class CrypticEntityEffect
{
	public static final CrypticEntityEffect INSTANCE = new CrypticEntityEffect();
	
	public RenderTarget entityTarget;
	public PostChain entityEffect;
	public boolean enabled;
	
	public final Minecraft minecraft = CrypticClientUtil.MC;
	
	public void doEntityEffect()
	{
		if(this.shouldShowEntityEffect())
		{
			RenderSystem.enableBlend();
			RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
			this.entityTarget.blitToScreen(this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight(), false);
			this.entityTarget.clear(Minecraft.ON_OSX);
			this.minecraft.getMainRenderTarget().bindWrite(false);
			this.enabled = false;
			RenderSystem.disableBlend();
			RenderSystem.defaultBlendFunc();
		}
	}
	
	public void initEffect()
	{
		if(this.entityEffect != null)
		{
			this.entityEffect.close();
		}
		try 
		{
			ResourceLocation location = ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "shaders/post/entity.json");
			this.entityEffect = new PostChain(this.minecraft.getTextureManager(), this.minecraft.getResourceManager(), this.minecraft.getMainRenderTarget(), location);
			this.entityEffect.resize(this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight());
			this.entityTarget = this.entityEffect.getTempTarget("swap");
		}
		catch(IOException e) 
		{
			e.printStackTrace();
			this.entityEffect = null;
			this.entityTarget = null;
		} 
		catch(JsonSyntaxException e)
		{
			e.printStackTrace();
			this.entityEffect = null;
			this.entityTarget = null;
		}
	}
	
	public void resize(int pWidth, int pHeight)
	{
		this.minecraft.levelRenderer.needsUpdate();
		if(this.entityEffect != null) 
		{
			this.entityEffect.resize(pWidth, pHeight);
		}
	}
	
	public void process(float partialTicks)
	{
		if(this.shouldShowEntityEffect())
		{
			ExtendedPostChain shaderChain = CrypticShaders.getHowler();
			EffectInstance shader = shaderChain.getMainShader();
			if(shader != null)
			{
				shader.setSampler("EntitySampler", () -> this.entityTarget.getColorTextureId());
				shaderChain.process(partialTicks);
			}
			this.entityEffect.process(partialTicks);
			this.minecraft.getMainRenderTarget().bindWrite(false);
		}
	}
	
	public boolean shouldShowEntityEffect()
	{
		if(this.minecraft.player == null)
		{
			return false;
		}
		if(this.minecraft.gameRenderer.currentEffect() == null)
		{
			return false;
		}
		if(!this.minecraft.gameRenderer.currentEffect().getName().equals("crypticfoes:shaders/post/howler_dummy.json"))
		{
			return false;
		}
		if(!CrypticClientUtil.isFirstPersonPlayer(this.minecraft.player))
		{
			return false;
		}
		return !this.minecraft.gameRenderer.isPanoramicMode() && this.entityTarget != null && this.entityEffect != null && this.enabled;
	}
}