package com.min01.crypticfoes.event;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.api.entity.CameraShakeEntity;
import com.min01.crypticfoes.effect.CrypticEffects;
import com.min01.crypticfoes.item.model.RollingArmorBallModel;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrypticFoes.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandlerForge
{
	public static final ResourceLocation BALL_TEXTURE = ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "textures/armor/rolling_armor_ball.png");
	
	@SubscribeEvent
	public static void onRenderPlayerPre(RenderPlayerEvent.Pre event)
	{
		Minecraft minecraft = Minecraft.getInstance();
		Player player = event.getEntity();
		PoseStack poseStack = event.getPoseStack();
		MultiBufferSource bufferSource = event.getMultiBufferSource();
		RollingArmorBallModel<Player> model = new RollingArmorBallModel<>(minecraft.getEntityModels().bakeLayer(RollingArmorBallModel.LAYER_LOCATION));
		//TODO rolling armor rendering;
	}
	
	@SubscribeEvent
	public static void onMovementInputUpdate(MovementInputUpdateEvent event)
	{
		Player player = event.getEntity();
		Input input = event.getInput();
		//TODO rolling movement logic;
	}
	
	@SubscribeEvent
	public static void onComputeFovModifier(ComputeFovModifierEvent event)
	{
		Minecraft minecraft = Minecraft.getInstance();
		if(minecraft.player.hasEffect(CrypticEffects.STUNNED.get()) && !minecraft.player.isSpectator())
		{
			float fov = event.getFovModifier();
			event.setNewFovModifier(fov - 0.5F);
		}
	}
	
    @SubscribeEvent
    public static void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) 
    {
    	CameraShakeEntity.cameraShake(event);
    }
    
	@SubscribeEvent
	public static void onInteractionKeyMappingTriggered(InputEvent.InteractionKeyMappingTriggered event)
	{
		Minecraft minecraft = Minecraft.getInstance();
		if(minecraft.player.hasEffect(CrypticEffects.STUNNED.get()) && !minecraft.player.isSpectator())
		{
			event.setSwingHand(false);
			event.setCanceled(true);
		}
	}
}
