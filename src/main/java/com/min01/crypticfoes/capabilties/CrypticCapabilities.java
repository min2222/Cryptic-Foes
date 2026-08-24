package com.min01.crypticfoes.capabilties;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.AttachCapabilitiesEvent;

public class CrypticCapabilities 
{
	public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event)
	{
		event.addCapability(RollingCapabilityImpl.ID, new RollingCapabilityImpl());
	}
}
