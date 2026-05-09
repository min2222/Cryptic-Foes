package com.min01.crypticfoes.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.min01.crypticfoes.capabilties.RollingCapabilityImpl;
import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

public class UpdateRollingSpeedPacket 
{
	private final UUID entityUUID;
	private final float rollingSpeed;

	public UpdateRollingSpeedPacket(UUID uuid, float rollingSpeed) 
	{
		this.entityUUID = uuid;
		this.rollingSpeed = rollingSpeed;
	}

	public static UpdateRollingSpeedPacket read(FriendlyByteBuf buf)
	{
		return new UpdateRollingSpeedPacket(buf.readUUID(), buf.readFloat());
	}

	public void write(FriendlyByteBuf buf)
	{
		buf.writeUUID(this.entityUUID);
		buf.writeFloat(this.rollingSpeed);
	}

	public static boolean handle(UpdateRollingSpeedPacket message, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			if(ctx.get().getDirection().getReceptionSide().isClient())
			{
				CrypticUtil.getClientLevel(t -> 
				{
					Entity entity = CrypticUtil.getEntityByUUID(t, message.entityUUID);
					entity.getCapability(RollingCapabilityImpl.ROLLING).ifPresent(cap -> 
					{
						cap.setRollingSpeed(message.rollingSpeed);
					});
				});
			}
		});
		ctx.get().setPacketHandled(true);
		return true;
	}
}
