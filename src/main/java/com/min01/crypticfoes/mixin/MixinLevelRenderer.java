package com.min01.crypticfoes.mixin;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.min01.crypticfoes.shader.CrypticEntityEffect;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer
{
    @Inject(method = "Lnet/minecraft/client/renderer/LevelRenderer;initOutline()V", at = @At("TAIL"))
    private void initOutline(CallbackInfo ci)
    {
    	CrypticEntityEffect.INSTANCE.initEffect();
    }
    
    @Inject(method = "Lnet/minecraft/client/renderer/LevelRenderer;resize(II)V", at = @At("TAIL"))
    private void resize(int x, int y, CallbackInfo ci)
    {
    	CrypticEntityEffect.INSTANCE.resize(x, y);
    }
    
	@Inject(at = @At(value = "TAIL"), method = "renderLevel")
	private void renderLevel(PoseStack mtx, float frameTime, long nanoTime, boolean renderOutline, Camera camera, GameRenderer gameRenderer, LightTexture light, Matrix4f projMat, CallbackInfo ci)
	{
		CrypticEntityEffect.INSTANCE.process(frameTime);
	}
	
	@Inject(at = @At(value = "TAIL"), method = "doEntityOutline")
	private void doEntityOutline(CallbackInfo ci)
	{
    	CrypticEntityEffect.INSTANCE.doEntityEffect();
	}
}
