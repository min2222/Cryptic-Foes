package com.min01.crypticfoes.capabilties;

import com.min01.crypticfoes.CrypticFoes;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

@AutoRegisterCapability
public interface IRollingCapability extends ICapabilitySerializable<CompoundTag>
{
	ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "rolling");

	void tick();
	
	void setRolling(boolean value);
	
	boolean isRolling();
	
	float getRollingYaw();
	
	void setRollingTick(int tick);
	
	int getRollingTick();
	
	void setRollingYaw(float yaw);
	
	void setRollingSpeed(float speed);
	
	float getRollingSpeed();
}
