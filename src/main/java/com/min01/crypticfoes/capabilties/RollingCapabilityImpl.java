package com.min01.crypticfoes.capabilties;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.min01.crypticfoes.item.CrypticItems;
import com.min01.crypticfoes.network.CrypticNetwork;
import com.min01.crypticfoes.network.UpdateRollingCapabilityPacket;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;

public class RollingCapabilityImpl implements IRollingCapability
{
	public static final Capability<IRollingCapability> ROLLING = CapabilityManager.get(new CapabilityToken<>() {});
	
	private float rollingYaw;
	private boolean isRolling;
	private int rollingTick;
	private final Entity entity;
	
	public RollingCapabilityImpl(Entity entity)
	{
		this.entity = entity;
	}
	
	@Override
	public CompoundTag serializeNBT() 
	{
		CompoundTag nbt = new CompoundTag();
		nbt.putBoolean("isRolling", this.isRolling);
		nbt.putInt("RollingTick", this.rollingTick);
		nbt.putFloat("RollingYaw", this.rollingYaw);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt)
	{
		this.setRolling(nbt.getBoolean("isRolling"));
		this.rollingTick = nbt.getInt("RollingTick");
		this.rollingYaw = nbt.getFloat("RollingYaw");
	}
	
	@Override
	public void tick() 
	{
		if(this.entity instanceof LivingEntity living)
		{
			this.setRolling(this.entity.isShiftKeyDown() && this.entity.isSprinting() && living.getItemBySlot(EquipmentSlot.CHEST).is(CrypticItems.ROLLING_CHESTPLATE.get()));
			if(this.isRolling())
			{
				this.entity.setPose(Pose.SWIMMING);
				List<LivingEntity> list = this.entity.level.getEntitiesOfClass(LivingEntity.class, this.entity.getBoundingBox().inflate(0.5F), t -> t != this.entity && !t.isAlliedTo(this.entity));
				list.forEach(t -> 
				{
					t.hurt(this.entity.damageSources().mobAttack(living), 5.0F);
				});
			}
		}
	}
	
	@Override
	public void setRolling(boolean value) 
	{
	    boolean prev = this.isRolling;
	    this.isRolling = value;
	    if(!prev && value)
	    {
	        this.rollingYaw = this.entity.getYHeadRot();
	    }
	    this.sendUpdatePacket();
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
	public float getRollingYaw() 
	{
		return this.rollingYaw;
	}
	
	private void sendUpdatePacket() 
	{
		if(!this.entity.level.isClientSide)
		{
			CrypticNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.entity), new UpdateRollingCapabilityPacket(this.entity.getUUID(), this.isRolling, this.rollingTick, this.rollingYaw));
		}
	}

	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) 
	{
		return ROLLING.orEmpty(cap, LazyOptional.of(() -> this));
	}
}
