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
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, CrypticFoes.MODID),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);
	
	public static int ID = 0;
	public static void registerMessages()
	{
		CHANNEL.registerMessage(ID++, UpdatePosArrayPacket.class, UpdatePosArrayPacket::write, UpdatePosArrayPacket::read, UpdatePosArrayPacket::handle);
		CHANNEL.registerMessage(ID++, AddSilencingParticlePacket.class, AddSilencingParticlePacket::write, AddSilencingParticlePacket::read, AddSilencingParticlePacket::handle);
		CHANNEL.registerMessage(ID++, UpdateSilencedBlocksPacket.class, UpdateSilencedBlocksPacket::write, UpdateSilencedBlocksPacket::read, UpdateSilencedBlocksPacket::handle);
		CHANNEL.registerMessage(ID++, UpdateStunnedEffectPacket.class, UpdateStunnedEffectPacket::write, UpdateStunnedEffectPacket::read, UpdateStunnedEffectPacket::handle);
	}
	
    public static <MSG> void sendToServer(MSG message) 
    {
    	CHANNEL.sendToServer(message);
    }
    
    public static <MSG> void sendToAll(MSG message)
    {
    	for(ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) 
    	{
    		CHANNEL.sendTo(message, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    	}
    }
}
