package com.min01.crypticfoes.event;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.effect.CrypticEffects;
import com.min01.crypticfoes.util.CrypticClientUtil;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrypticFoes.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandlerForge
{
	public static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation(CrypticFoes.MODID, "textures/gui/cryptic_foes_icon.png");
	
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
