package com.min01.crypticfoes.network;

import java.util.function.Supplier;

import com.min01.crypticfoes.particle.CrypticParticles;
import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraftforge.network.NetworkEvent;

public class AddSilencingParticlePacket 
{
	private final BlockPos blockPos;

	public AddSilencingParticlePacket(BlockPos blockPos) 
	{
		this.blockPos = blockPos;
	}

	public AddSilencingParticlePacket(FriendlyByteBuf buf)
	{
		this.blockPos = buf.readBlockPos();
	}

	public void encode(FriendlyByteBuf buf)
	{
		buf.writeBlockPos(this.blockPos);
	}

	public static class Handler 
	{
		public static boolean onMessage(AddSilencingParticlePacket message, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() ->
			{
				if(ctx.get().getDirection().getReceptionSide().isClient())
				{
					CrypticUtil.getClientLevel(t -> 
					{
			            ParticleUtils.spawnParticlesOnBlockFaces(t, message.blockPos, CrypticParticles.SILENCING.get(), UniformInt.of(3, 5));
					});
				}
			});
			ctx.get().setPacketHandled(true);
			return true;
		}
	}
}
