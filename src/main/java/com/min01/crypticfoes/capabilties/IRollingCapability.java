package com.min01.crypticfoes.capabilties;

import com.min01.crypticfoes.CrypticFoes;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

@AutoRegisterCapability
public interface IRollingCapability extends ICapabilitySerializable<CompoundTag>
{
	ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "rolling");

	void tick(LivingEntity entity);
	
	void setRolling(boolean isRolling);

	void setRollingTick(int tick);
	
	void setRollingYaw(float yaw);
	
	void setRollingSpeed(float speed);
	
	void sync(boolean isRolling, int rollingTick, float rollingYaw, float rollingSpeed);

	boolean isRolling();
	
	int getRollingTick();
	
	float getRollingYaw();
	
	float getRollingSpeed();
}
