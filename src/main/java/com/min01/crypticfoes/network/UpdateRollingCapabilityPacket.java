package com.min01.crypticfoes.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.min01.crypticfoes.capabilties.IRollingCapability;
import com.min01.crypticfoes.capabilties.RollingCapabilityImpl;
import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class UpdateRollingCapabilityPacket 
{
	private final UUID entityUUID;
	private final boolean isRolling;
	private final int rollingTick;
	private final float rollingYaw;
	private final float rollingSpeed;

	public UpdateRollingCapabilityPacket(UUID entityUUID, boolean isRolling, int rollingTick, float rollingYaw, float rollingSpeed) 
	{
		this.entityUUID = entityUUID;
		this.isRolling = isRolling;
		this.rollingTick = rollingTick;
		this.rollingYaw = rollingYaw;
		this.rollingSpeed = rollingSpeed;
	}

	public static UpdateRollingCapabilityPacket read(FriendlyByteBuf buf)
	{
		return new UpdateRollingCapabilityPacket(buf.readUUID(), buf.readBoolean(),  buf.readInt(), buf.readFloat(), buf.readFloat());
	}

	public void write(FriendlyByteBuf buf)
	{
		buf.writeUUID(this.entityUUID);
		buf.writeBoolean(this.isRolling);
		buf.writeInt(this.rollingTick);
		buf.writeFloat(this.rollingYaw);
		buf.writeFloat(this.rollingSpeed);
	}

	public static boolean handle(UpdateRollingCapabilityPacket message, Supplier<NetworkEvent.Context> supplier)
	{
		CrypticUtil.handlePacket(supplier, LogicalSide.CLIENT, ctx ->
		{
			CrypticUtil.getClientLevel(t -> 
			{
				Entity entity = CrypticUtil.getEntityByUUID(t, message.entityUUID);
				IRollingCapability cap = entity.getCapability(RollingCapabilityImpl.ROLLING).orElse(new RollingCapabilityImpl());
				cap.sync(message.isRolling, message.rollingTick, message.rollingYaw, message.rollingSpeed);
			});
		});
		return true;
	}
}
