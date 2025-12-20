package com.min01.crypticfoes.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.min01.crypticfoes.effect.CrypticEffects;
import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

public class UpdateStunnedEffectPacket 
{
	public final UUID entityUUID;
	public final int amplifier;
	public final int duration;
	public final boolean remove;

	public UpdateStunnedEffectPacket(UUID entityUUID, int amplifier, int duration, boolean remove) 
	{
		this.entityUUID = entityUUID;
		this.amplifier = amplifier;
		this.duration = duration;
		this.remove = remove;
	}

	public static UpdateStunnedEffectPacket read(FriendlyByteBuf buf)
	{
		return new UpdateStunnedEffectPacket(buf.readUUID(), buf.readInt(), buf.readInt(), buf.readBoolean());
	}

	public void write(FriendlyByteBuf buf)
	{
		buf.writeUUID(this.entityUUID);
		buf.writeInt(this.amplifier);
		buf.writeInt(this.duration);
		buf.writeBoolean(this.remove);
	}

	public static boolean handle(UpdateStunnedEffectPacket message, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			if(ctx.get().getDirection().getReceptionSide().isClient())
			{
				CrypticUtil.getClientLevel(level -> 
				{
					Entity entity = CrypticUtil.getEntityByUUID(level, message.entityUUID);
					if(entity instanceof LivingEntity living) 
					{
						if(!message.remove)
						{
							living.addEffect(new MobEffectInstance(CrypticEffects.STUNNED.get(), message.duration, message.amplifier));
						}
						else if(living.hasEffect(CrypticEffects.STUNNED.get()))
						{
							living.removeEffect(CrypticEffects.STUNNED.get());
						}
					}
				});
			}
		});
		ctx.get().setPacketHandled(true);
		return true;
	}
}