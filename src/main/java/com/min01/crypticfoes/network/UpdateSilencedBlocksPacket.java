package com.min01.crypticfoes.network;

import java.util.function.Supplier;

import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class UpdateSilencedBlocksPacket 
{
	public final BlockPos pos;

	public UpdateSilencedBlocksPacket(BlockPos pos) 
	{
		this.pos = pos;
	}

	public static UpdateSilencedBlocksPacket read(FriendlyByteBuf buf)
	{
		return new UpdateSilencedBlocksPacket(buf.readBlockPos());
	}

	public void write(FriendlyByteBuf buf)
	{
		buf.writeBlockPos(this.pos);
	}

	public static boolean handle(UpdateSilencedBlocksPacket message, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			if(ctx.get().getDirection().getReceptionSide().isClient() && !CrypticUtil.SILENCED_BLOCKS.contains(message.pos))
			{
				CrypticUtil.SILENCED_BLOCKS.add(message.pos);
			}
		});
		ctx.get().setPacketHandled(true);
		return true;
	}
}
