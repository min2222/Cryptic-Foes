package com.min01.crypticfoes.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.min01.crypticfoes.capabilties.RollingCapabilityImpl;
import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

public class UpdateRollingCapabilityPacket 
{
	private final UUID entityUUID;
	private final boolean isRolling;
	private final int rollingTick;
	private final float rollingYaw;

	public UpdateRollingCapabilityPacket(UUID uuid, boolean isRolling, int rollingTick, float rollingYaw) 
	{
		this.entityUUID = uuid;
		this.isRolling = isRolling;
		this.rollingTick = rollingTick;
		this.rollingYaw = rollingYaw;
	}

	public static UpdateRollingCapabilityPacket read(FriendlyByteBuf buf)
	{
		return new UpdateRollingCapabilityPacket(buf.readUUID(), buf.readBoolean(),  buf.readInt(), buf.readFloat());
	}

	public void write(FriendlyByteBuf buf)
	{
		buf.writeUUID(this.entityUUID);
		buf.writeBoolean(this.isRolling);
		buf.writeInt(this.rollingTick);
		buf.writeFloat(this.rollingYaw);
	}

	public static boolean handle(UpdateRollingCapabilityPacket message, Supplier<NetworkEvent.Context> ctx)
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
						cap.setRolling(message.isRolling);
						cap.setRollingYaw(message.rollingYaw);
					});
				});
			}
		});
		ctx.get().setPacketHandled(true);
		return true;
	}
}
