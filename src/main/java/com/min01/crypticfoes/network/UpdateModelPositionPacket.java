package com.min01.crypticfoes.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.min01.crypticfoes.entity.IAnimatable;
import com.min01.crypticfoes.misc.CrypticEntityDataSerializers;
import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

public class UpdateModelPositionPacket 
{
	private final UUID entityUUID;
	private final Vec3 pos;
	private final String partName;

	public UpdateModelPositionPacket(UUID uuid, Vec3 pos, String partName) 
	{
		this.entityUUID = uuid;
		this.pos = pos;
		this.partName = partName;
	}

	public static UpdateModelPositionPacket read(FriendlyByteBuf buf)
	{
		return new UpdateModelPositionPacket(buf.readUUID(), CrypticEntityDataSerializers.readVec3(buf), buf.readUtf());
	}

	public void write(FriendlyByteBuf buf)
	{
		buf.writeUUID(this.entityUUID);
		CrypticEntityDataSerializers.writeVec3(buf, this.pos);
		buf.writeUtf(this.partName);
	}

	public static boolean handle(UpdateModelPositionPacket message, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			if(ctx.get().getDirection().getReceptionSide().isServer())
			{
				Entity entity = CrypticUtil.getEntityByUUID(ctx.get().getSender().level, message.entityUUID);
				if(entity instanceof IAnimatable mob) 
				{
					mob.getModelPositions().putModelPos(message.partName, message.pos);
				}
			}
		});
		ctx.get().setPacketHandled(true);
		return true;
	}
}
