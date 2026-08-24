package com.min01.crypticfoes.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.min01.crypticfoes.api.client.ModelPartPos;
import com.min01.crypticfoes.api.entity.IAnimatable;
import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class UpdateModelPartPosPacket 
{
	private final UUID entityUUID;
	private final Vec3 pos;
	private final String partName;

	public UpdateModelPartPosPacket(UUID entityUUID, Vec3 pos, String partName) 
	{
		this.entityUUID = entityUUID;
		this.pos = pos;
		this.partName = partName;
	}

	public static UpdateModelPartPosPacket read(FriendlyByteBuf buf)
	{
		return new UpdateModelPartPosPacket(buf.readUUID(), CrypticUtil.readVec3(buf), buf.readUtf());
	}

	public void write(FriendlyByteBuf buf)
	{
		buf.writeUUID(this.entityUUID);
		CrypticUtil.writeVec3(buf, this.pos);
		buf.writeUtf(this.partName);
	}

	public static boolean handle(UpdateModelPartPosPacket message, Supplier<NetworkEvent.Context> supplier)
	{
		CrypticUtil.handlePacket(supplier, LogicalSide.SERVER, ctx -> 
		{
			ServerPlayer sender = ctx.getSender();
			Entity entity = CrypticUtil.getEntityByUUID(sender.level, message.entityUUID);
			if(entity instanceof IAnimatable mob) 
			{
				ModelPartPos partPos = mob.getModelPartPos();
				partPos.setPos(message.partName, message.pos);
			}
		});
		return true;
	}
}
