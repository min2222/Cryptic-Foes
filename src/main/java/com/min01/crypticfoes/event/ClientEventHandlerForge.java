package com.min01.crypticfoes.event;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.effect.CrypticEffects;
import com.min01.crypticfoes.entity.EntityCameraShake;
import com.min01.crypticfoes.shader.CrypticEntityEffect;
import com.min01.crypticfoes.util.CrypticClientUtil;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrypticFoes.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandlerForge
{
	@SubscribeEvent
	public static void onRenderArm(RenderArmEvent event)
	{
		if(CrypticEntityEffect.INSTANCE.shouldShowEntityEffect())
		{
	        RenderSystem.setShaderColor(100.0F, 100.0F, 100.0F, 1.0F);
		}
	}
	
	@SubscribeEvent
	public static void onRenderLivingPost(RenderLivingEvent.Post<?, ?> event)
	{
		CrypticEntityEffect.INSTANCE.enabled = true;
	}
	
	@SubscribeEvent
	public static void onRenderLevelStage(RenderLevelStageEvent event)
	{
		if(event.getStage() == Stage.AFTER_SOLID_BLOCKS)
		{
			CrypticEntityEffect.INSTANCE.enabled = true;
		}
	}
	
	@SubscribeEvent
	public static void onComputeFovModifier(ComputeFovModifierEvent event)
	{
		if(CrypticClientUtil.MC.player.hasEffect(CrypticEffects.STUNNED.get()) && !CrypticClientUtil.MC.player.isSpectator())
		{
			float fov = event.getFovModifier();
			event.setNewFovModifier(fov - 0.5F);
		}
	}
	
    @SubscribeEvent
    public static void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) 
    {
        Player player = CrypticClientUtil.MC.player;
        float delta = CrypticClientUtil.MC.getFrameTime();
        float ticksExistedDelta = player.tickCount + delta;
        if(player != null)
        {
            float shakeAmplitude = 0.0F;
            for(EntityCameraShake cameraShake : player.level.getEntitiesOfClass(EntityCameraShake.class, player.getBoundingBox().inflate(100.0F))) 
            {
                if(cameraShake.distanceTo(player) < cameraShake.getRadius())
                {
                    shakeAmplitude += cameraShake.getShakeAmount(player, delta);
                }
            }
            if(shakeAmplitude > 1.0F)
            {
                shakeAmplitude = 1.0F;
            }
            event.setPitch((float)(event.getPitch() + shakeAmplitude * Math.cos(ticksExistedDelta * 3.0F + 2.0F) * 25.0));
            event.setYaw((float)(event.getYaw() + shakeAmplitude * Math.cos(ticksExistedDelta * 5.0F + 1.0F) * 25.0));
            event.setRoll((float)(event.getRoll() + shakeAmplitude * Math.cos(ticksExistedDelta * 4.0F) * 25.0));
        }
    }
    
	@SubscribeEvent
	public static void onInteractionKeyMappingTriggered(InputEvent.InteractionKeyMappingTriggered event)
	{
		if(CrypticClientUtil.MC.player.hasEffect(CrypticEffects.STUNNED.get()) && !CrypticClientUtil.MC.player.isSpectator())
		{
			event.setSwingHand(false);
			event.setCanceled(true);
		}
	}
}
