package com.min01.crypticfoes.capabilties;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;

public class CrypticCapabilities 
{
	public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event)
	{
    	Entity entity = event.getObject();
		if(entity instanceof Player player)
		{
			event.addCapability(RollingCapabilityImpl.ID, new RollingCapabilityImpl(player));
		}
	}
}
