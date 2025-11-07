package com.min01.crypticfoes.event;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.effect.CrypticEffects;
import com.min01.crypticfoes.entity.EntityCameraShake;
import com.min01.crypticfoes.util.CrypticClientUtil;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrypticFoes.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandlerForge
{
	public static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation(CrypticFoes.MODID, "textures/gui/cryptic_foes_icon.png");
	
	@SubscribeEvent
	public static void onComputeFovModifier(ComputeFovModifierEvent event)
	{
		if(CrypticClientUtil.MC.player.hasEffect(CrypticEffects.STUNNED.get()))
		{
			float fov = event.getFovModifier();
			event.setNewFovModifier(fov - 0.5F);
		}
	}
	
    @SubscribeEvent
    public static void onSetupCamera(ViewportEvent.ComputeCameraAngles event) 
    {
        Player player = CrypticClientUtil.MC.player;
        float delta = Minecraft.getInstance().getFrameTime();
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
    public static void onMouseButtonInput(InputEvent.MouseButton.Pre event)
    {
		if(CrypticClientUtil.MC.player != null && CrypticClientUtil.MC.screen == null)
		{
			if(event.getButton() == CrypticClientUtil.MC.options.keyAttack.getKey().getValue() || event.getButton() == CrypticClientUtil.MC.options.keyUse.getKey().getValue())
			{
				if(CrypticClientUtil.MC.player.hasEffect(CrypticEffects.STUNNED.get()))
				{
					event.setCanceled(true);
				}
			}
		}
    }
	
	@SubscribeEvent
    public static void onRenderGuiOverlayEvent(RenderGuiOverlayEvent.Post event)
    {
		ForgeGui gui = (ForgeGui) CrypticClientUtil.MC.gui;
		if(!CrypticClientUtil.MC.options.hideGui && gui.shouldDrawSurvivalElements())
		{
			if(CrypticClientUtil.MC.player.hasEffect(CrypticEffects.FRAGILITY.get()))
			{
		    	if(event.getOverlay() == VanillaGuiOverlay.ARMOR_LEVEL.type())
		    	{
		    		GuiGraphics guiGraphics = event.getGuiGraphics();
		    		renderFragilityArmor(guiGraphics, guiGraphics.guiWidth(), guiGraphics.guiHeight());
		    	}
			}
		}
    }
	
    public static void renderFragilityArmor(GuiGraphics guiGraphics, int width, int height)
    {
        RenderSystem.enableBlend();
        int left = width / 2 - 91;
        int top = height - 49;
        int level = CrypticClientUtil.MC.player.getArmorValue();
        for(int i = 1; level > 0 && i < 20; i += 2)
        {
            if(i < level)
            {
                guiGraphics.blit(GUI_ICONS_LOCATION, left, top, 34, 9, 9, 9);
            }
            else if(i == level)
            {
                guiGraphics.blit(GUI_ICONS_LOCATION, left, top, 25, 9, 9, 9);
            }
            else if(i > level)
            {
                guiGraphics.blit(GUI_ICONS_LOCATION, left, top, 16, 9, 9, 9);
            }
            left += 8;
        }
        RenderSystem.disableBlend();
    }
}
