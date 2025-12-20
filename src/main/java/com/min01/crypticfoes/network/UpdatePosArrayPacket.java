package com.min01.crypticfoes.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.min01.crypticfoes.entity.IPosArray;
import com.min01.crypticfoes.misc.CrypticEntityDataSerializers;
import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

public class UpdatePosArrayPacket 
{
	private final UUID entityUUID;
	private final int array;
	private final Vec3 pos;

	public UpdatePosArrayPacket(UUID uuid, Vec3 pos, int array) 
	{
		this.entityUUID = uuid;
		this.pos = pos;
		this.array = array;
	}

	public static UpdatePosArrayPacket read(FriendlyByteBuf buf)
	{
		return new UpdatePosArrayPacket(buf.readUUID(), CrypticEntityDataSerializers.readVec3(buf), buf.readInt());
	}

	public void write(FriendlyByteBuf buf)
	{
		buf.writeUUID(this.entityUUID);
		CrypticEntityDataSerializers.writeVec3(buf, this.pos);
		buf.writeInt(this.array);
	}

	public static boolean handle(UpdatePosArrayPacket message, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			if(ctx.get().getDirection().getReceptionSide().isServer())
			{
				Entity entity = CrypticUtil.getEntityByUUID(ctx.get().getSender().level, message.entityUUID);
				if(entity instanceof IPosArray mob) 
				{
					mob.getPosArray()[message.array] = message.pos;
				}
			}
			else
			{
				CrypticUtil.getClientLevel(t -> 
				{
					Entity entity = CrypticUtil.getEntityByUUID(t, message.entityUUID);
					if(entity instanceof IPosArray mob) 
					{
						mob.getPosArray()[message.array] = message.pos;
					}
				});
			}
		});
		ctx.get().setPacketHandled(true);
		return true;
	}
}
