package com.min01.crypticfoes.network;

import java.util.function.Supplier;

import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

public class UpdateSilencedBlocksPacket 
{
	public final ResourceKey<Level> dimension;
	public final BlockPos pos;

	public UpdateSilencedBlocksPacket(ResourceKey<Level> dimension, BlockPos pos) 
	{
		this.dimension = dimension;
		this.pos = pos;
	}

	public static UpdateSilencedBlocksPacket read(FriendlyByteBuf buf)
	{
		return new UpdateSilencedBlocksPacket(buf.readResourceKey(Registries.DIMENSION), buf.readBlockPos());
	}

	public void write(FriendlyByteBuf buf)
	{
		buf.writeResourceKey(this.dimension);
		buf.writeBlockPos(this.pos);
	}

	public static boolean handle(UpdateSilencedBlocksPacket message, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			if(ctx.get().getDirection().getReceptionSide().isClient()) 
			{
				CrypticUtil.SILENCED_BLOCKS.put(message.dimension, message.pos);
			}
		});
		ctx.get().setPacketHandled(true);
		return true;
	}
}
