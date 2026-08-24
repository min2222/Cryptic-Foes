package com.min01.crypticfoes.network;

import com.min01.crypticfoes.CrypticFoes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;

public class CrypticNetwork 
{
	public static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, CrypticFoes.MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
	
	public static void registerMessages()
	{
		int id = 0;
		CHANNEL.registerMessage(id, UpdateRollingCapabilityPacket.class, UpdateRollingCapabilityPacket::write, UpdateRollingCapabilityPacket::read, UpdateRollingCapabilityPacket::handle);
		CHANNEL.registerMessage(id, AddSilencingParticlePacket.class, AddSilencingParticlePacket::write, AddSilencingParticlePacket::read, AddSilencingParticlePacket::handle);
		CHANNEL.registerMessage(id, SilenceBlockPacket.class, SilenceBlockPacket::write, SilenceBlockPacket::read, SilenceBlockPacket::handle);
		CHANNEL.registerMessage(id, UpdateModelPartPosPacket.class, UpdateModelPartPosPacket::write, UpdateModelPartPosPacket::read, UpdateModelPartPosPacket::handle);
	}
	
    public static <MSG> void sendToServer(MSG message) 
    {
    	CHANNEL.sendToServer(message);
    }
    
    public static <MSG> void sendToAll(MSG message)
    {
    	for(ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) 
    	{
    		sendToPlayer(message, player);
    	}
    }
    
    public static <MSG> void sendToPlayer(MSG msg, ServerPlayer player) 
    {
        CHANNEL.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}
