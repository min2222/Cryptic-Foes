package com.min01.crypticfoes.capabilties;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;

public class RollingCapabilityImpl implements IRollingCapability
{
	public static final Capability<IRollingCapability> ROLLING = CapabilityManager.get(new CapabilityToken<>() {});

	private float rollingSpeed;
	private float rollingYaw;
	private boolean isRolling;
	private int rollingTick;
	
	@Override
	public CompoundTag serializeNBT() 
	{
		CompoundTag nbt = new CompoundTag();
		nbt.putBoolean("isRolling", this.isRolling);
		nbt.putInt("RollingTick", this.rollingTick);
		nbt.putFloat("RollingYaw", this.rollingYaw);
		nbt.putFloat("RollingSpeed", this.rollingSpeed);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt)
	{
		this.setRolling(nbt.getBoolean("isRolling"));
		this.rollingTick = nbt.getInt("RollingTick");
		this.rollingYaw = nbt.getFloat("RollingYaw");
		this.rollingSpeed = nbt.getFloat("RollingSpeed");
	}
	
	@Override
	public void tick(LivingEntity entity) 
	{
		//TODO
	}
	
	@Override
	public void setRolling(boolean isRolling) 
	{
		this.isRolling = isRolling;
	}
	
	@Override
	public boolean isRolling() 
	{
		return this.isRolling;
	}
	
	@Override
	public void setRollingYaw(float yaw)
	{
		this.rollingYaw = yaw;
	}
	
	@Override
	public void setRollingTick(int tick)
	{
		this.rollingTick = tick;
	}
	
	@Override
	public void setRollingSpeed(float speed)
	{
		this.rollingSpeed = speed;
	}
	
	@Override
	public int getRollingTick() 
	{
		return this.rollingTick;
	}
	
	@Override
	public float getRollingYaw() 
	{
		return this.rollingYaw;
	}
	
	@Override
	public float getRollingSpeed()
	{
		return this.rollingSpeed;
	}
	
	@Override
	public void sync(boolean isRolling, int rollingTick, float rollingYaw, float rollingSpeed)
	{
		this.isRolling = isRolling;
		this.rollingTick = rollingTick;
		this.rollingYaw = rollingYaw;
		this.rollingSpeed = rollingSpeed;
	}

	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) 
	{
		return ROLLING.orEmpty(cap, LazyOptional.of(() -> this));
	}
}
