package com.min01.crypticfoes.capabilties;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.min01.crypticfoes.enchantment.CrypticEnchantments;
import com.min01.crypticfoes.item.CrypticItems;
import com.min01.crypticfoes.network.CrypticNetwork;
import com.min01.crypticfoes.network.UpdateRollingCapabilityPacket;
import com.min01.crypticfoes.network.UpdateRollingSpeedPacket;
import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;

public class RollingCapabilityImpl implements IRollingCapability
{
	public static final Capability<IRollingCapability> ROLLING = CapabilityManager.get(new CapabilityToken<>() {});

	private float rollingSpeed;
	private float rollingYaw;
	private boolean isRolling;
	private int rollingTick;
	private boolean rollingExhausted;
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
		nbt.putBoolean("RollingExhausted", this.rollingExhausted);
		nbt.putFloat("RollingYaw", this.rollingYaw);
		nbt.putFloat("RollingSpeed", this.rollingSpeed);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt)
	{
		this.setRolling(nbt.getBoolean("isRolling"));
		this.rollingTick = nbt.getInt("RollingTick");
		this.rollingExhausted = nbt.getBoolean("RollingExhausted");
		this.rollingYaw = nbt.getFloat("RollingYaw");
		this.rollingSpeed = nbt.getFloat("RollingSpeed");
	}
	
	@Override
	public void tick() 
	{
		if(this.entity.level.isClientSide)
		{
			return;
		}
		if(this.entity instanceof LivingEntity living)
		{
			if(this.rollingExhausted)
			{
				if(!this.entity.isShiftKeyDown() || !this.entity.isSprinting())
				{
					this.rollingExhausted = false;
					this.rollingTick = 0;
				}
				this.setRolling(false);
				return;
			}
			boolean wantsRoll = this.entity.isShiftKeyDown() && this.entity.isSprinting() && living.getItemBySlot(EquipmentSlot.CHEST).is(CrypticItems.ROLLING_CHESTPLATE.get());
			if(wantsRoll)
			{
				if(!this.isRolling())
				{
					this.rollingTick = 0;
				}
				this.setRolling(true);
				this.rollingTick++;
				float speed = 3.5F;
				int level = living.getItemBySlot(EquipmentSlot.CHEST).getEnchantmentLevel(CrypticEnchantments.ACCELERATE.get());
				if(level > 0)
				{
					speed += CrypticUtil.percent(this.rollingTick / 20.0F, 15 * level);
				}
				this.setRollingSpeed(speed);
				if(this.rollingTick >= this.getMaxRollingTick(living))
				{
					this.rollingExhausted = true;
					this.setRolling(false);
					return;
				}
				List<LivingEntity> list = this.entity.level.getEntitiesOfClass(LivingEntity.class, this.entity.getBoundingBox().inflate(0.5F), t -> t != this.entity && !t.isAlliedTo(this.entity));
				list.forEach(t -> 
				{
					t.hurt(this.entity.damageSources().mobAttack(living), 5.0F);
				});
			}
			else
			{
				this.rollingTick = 0;
				this.setRolling(false);
			}
		}
	}
	
	public int getMaxRollingTick(LivingEntity living)
	{
		int tick = 140;
		int level = living.getItemBySlot(EquipmentSlot.CHEST).getEnchantmentLevel(CrypticEnchantments.PATIENCE.get());
		tick += (40 * level);
		return tick;
	}
	
	@Override
	public void setRolling(boolean value) 
	{
	    boolean prev = this.isRolling;
	    this.isRolling = value;
	    if(this.entity instanceof Player player)
	    {
	    	player.setForcedPose(value ? Pose.SWIMMING : null);
	    }
	    else if(value)
	    {
	    	this.entity.setPose(Pose.SWIMMING);
	    }
	    if(!prev && value )
	    {
	        this.rollingYaw = this.entity.getYHeadRot();
	    }
	    if(prev != this.isRolling)
	    {
	    	this.sendUpdatePacket();
	    }
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
	public void setRollingSpeed(float speed)
	{
		this.rollingSpeed = speed;
		this.sendSpeedUpdatePacket();
	}
	
	@Override
	public float getRollingSpeed()
	{
		return this.rollingSpeed;
	}
	
	private void sendSpeedUpdatePacket() 
	{
		if(!this.entity.level.isClientSide)
		{
			CrypticNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.entity), new UpdateRollingSpeedPacket(this.entity.getUUID(), this.rollingSpeed));
		}
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
