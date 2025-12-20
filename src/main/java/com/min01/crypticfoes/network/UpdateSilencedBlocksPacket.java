package com.min01.crypticfoes.network;

import java.util.List;
import java.util.function.Supplier;

import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class UpdateSilencedBlocksPacket 
{
	private final List<BlockPos> blocks;

	public UpdateSilencedBlocksPacket(List<BlockPos> blocks) 
	{
		this.blocks = blocks;
	}

	public static UpdateSilencedBlocksPacket read(FriendlyByteBuf buf)
	{
		return new UpdateSilencedBlocksPacket(buf.readList(t -> t.readBlockPos()));
	}

	public void write(FriendlyByteBuf buf)
	{
		buf.writeCollection(this.blocks, (t, u) -> t.writeBlockPos(u));
	}

	public static boolean handle(UpdateSilencedBlocksPacket message, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			if(ctx.get().getDirection().getReceptionSide().isClient() && !CrypticUtil.SILENCED_BLOCKS.containsAll(message.blocks))
			{
				CrypticUtil.SILENCED_BLOCKS.addAll(message.blocks);
			}
		});
		ctx.get().setPacketHandled(true);
		return true;
	}
}
