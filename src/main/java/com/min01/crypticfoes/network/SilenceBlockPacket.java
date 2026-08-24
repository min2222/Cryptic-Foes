package com.min01.crypticfoes.network;

import java.util.function.Supplier;

import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class SilenceBlockPacket 
{
	private final BlockPos pos;

	public SilenceBlockPacket(BlockPos pos) 
	{
		this.pos = pos;
	}

	public static SilenceBlockPacket read(FriendlyByteBuf buf)
	{
		return new SilenceBlockPacket(buf.readBlockPos());
	}

	public void write(FriendlyByteBuf buf)
	{
		buf.writeBlockPos(this.pos);
	}

	public static boolean handle(SilenceBlockPacket message, Supplier<NetworkEvent.Context> supplier)
	{
		CrypticUtil.handlePacket(supplier, LogicalSide.CLIENT, ctx ->
		{
			
		});
		return true;
	}
}
