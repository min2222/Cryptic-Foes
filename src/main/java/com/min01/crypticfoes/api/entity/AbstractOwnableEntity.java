package com.min01.crypticfoes.api.entity;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public abstract class AbstractOwnableEntity<T extends Entity> extends Entity
{
	private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(AbstractOwnableEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	
	public AbstractOwnableEntity(EntityType<?> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
	}
	
	@Override
	protected void defineSynchedData()
	{
		this.entityData.define(OWNER_UUID, Optional.empty());
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag pCompound) 
	{
		if(pCompound.hasUUID("Owner")) 
		{
			this.entityData.set(OWNER_UUID, Optional.of(pCompound.getUUID("Owner")));
		}
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag pCompound) 
	{
		Optional<UUID> optional = this.entityData.get(OWNER_UUID);
		if(optional.isPresent())
		{
			pCompound.putUUID("Owner", optional.get());
		}
	}
	
	public void setOwner(T owner)
	{
		Optional<UUID> optional = Optional.empty();
		if(owner != null)
		{
			optional = Optional.of(owner.getUUID());
		}
		this.entityData.set(OWNER_UUID, optional);
	}
	
	@SuppressWarnings("unchecked")
	@Nullable
	public T getOwner() 
	{
		Optional<UUID> optional = this.entityData.get(OWNER_UUID);
		if(optional.isPresent()) 
		{
			return (T) CrypticUtil.getEntityByUUID(this.level, optional.get());
		}
		return null;
	}
	
	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}