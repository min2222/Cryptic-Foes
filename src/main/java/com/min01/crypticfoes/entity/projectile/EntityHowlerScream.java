package com.min01.crypticfoes.entity.projectile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.min01.crypticfoes.effect.CrypticEffects;
import com.min01.crypticfoes.item.CrypticItems;
import com.min01.crypticfoes.item.MonstrousHornItem;
import com.min01.crypticfoes.misc.CrypticTags;
import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class EntityHowlerScream extends ThrowableProjectile
{
	public static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(EntityHowlerScream.class, EntityDataSerializers.OPTIONAL_UUID);
	public static final EntityDataAccessor<Integer> STUN_DURATION = SynchedEntityData.defineId(EntityHowlerScream.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Float> RANGE = SynchedEntityData.defineId(EntityHowlerScream.class, EntityDataSerializers.FLOAT);
	public float alpha = 1.0F;
	
	public EntityHowlerScream(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
	}

	@Override
	protected void defineSynchedData() 
	{
		this.entityData.define(OWNER_UUID, Optional.empty());
		this.entityData.define(STUN_DURATION, 160);
		this.entityData.define(RANGE, 0.03F);
	}
	
	@Override
	public void tick()
	{
		super.tick();
		this.refreshDimensions();
		this.alpha -= this.getRange();
		this.alpha = Mth.clamp(this.alpha, 0.0F, 1.0F);
		if(this.alpha <= 0.0F)
		{
			this.discard();
		}
		else if(this.getOwner() != null)
		{
			float scale = 0.25F + (this.tickCount * 0.08F);
			scale = Mth.clamp(scale, 0.0F, 3.0F);
			List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(scale), t -> !t.isAlliedTo(this.getOwner()) && t != this.getOwner() && !t.hasEffect(CrypticEffects.STUNNED.get()));
			list.forEach(t -> 
			{
				if(!t.getType().is(CrypticTags.CrypticEntity.RESIST_TO_STUN) && !t.hasEffect(CrypticEffects.STUNNED.get()))
				{
					this.addStunCount();
					t.addEffect(new MobEffectInstance(CrypticEffects.STUNNED.get(), this.getStunDuration()));
				}
			});
		}
	}
	
	public void addStunCount()
	{
		if(this.getOwner() instanceof Player player)
		{
			ItemStack stack = player.getItemInHand(player.getUsedItemHand());
			if(stack.is(CrypticItems.MONSTROUS_HORN.get()))
			{
				MonstrousHornItem.setStunCount(stack, MonstrousHornItem.getStunCount(stack) + 1);
			}
		}
	}
	
	@Override
	protected void onHitBlock(BlockHitResult pResult)
	{
		super.onHitBlock(pResult);
		BlockPos blockPos = pResult.getBlockPos();
		BlockState state = this.level.getBlockState(blockPos);
		if(state.is(CrypticTags.CrypticBlocks.BREAKABLE_BY_SCREAM) && !this.level.isClientSide && this.mayInteract(this.level, blockPos) && this.getDeltaMovement().length() > 0.6D) 
		{
			this.level.destroyBlock(blockPos, true);
		}
	}
	
	@Override
	public EntityDimensions getDimensions(Pose pPose)
	{
		float scale = 0.25F + (this.tickCount * 0.08F);
		scale = Mth.clamp(scale, 0.0F, 3.0F);
		return EntityDimensions.scalable(scale, scale);
	}
	
	@Override
	protected void updateRotation()
	{
		
	}
	
	@Override
	public boolean displayFireAnimation() 
	{
		return false;
	}
	
	@Override
	public boolean isInWater() 
	{
		return false;
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag pCompound) 
	{
		if(this.entityData.get(OWNER_UUID).isPresent())
		{
			pCompound.putUUID("Owner", this.entityData.get(OWNER_UUID).get());
		}
		pCompound.putInt("StunDuration", this.getStunDuration());
		pCompound.putFloat("Range", this.getRange());
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag pCompound) 
	{
		if(pCompound.hasUUID("Owner")) 
		{
			this.entityData.set(OWNER_UUID, Optional.of(pCompound.getUUID("Owner")));
		}
		this.setStunDuration(pCompound.getInt("StunDuration"));
		this.setRange(pCompound.getFloat("Range"));
	}
	
	@Override
	public void setOwner(Entity owner)
	{
		this.entityData.set(OWNER_UUID, Optional.of(owner.getUUID()));
	}
	
	@Override
	public Entity getOwner()
	{
		if(this.entityData.get(OWNER_UUID).isPresent()) 
		{
			return CrypticUtil.getEntityByUUID(this.level, this.entityData.get(OWNER_UUID).get());
		}
		return null;
	}
	
	public void setStunDuration(int duration)
	{
		this.entityData.set(STUN_DURATION, duration);
	}
	
	public int getStunDuration()
	{
		return this.entityData.get(STUN_DURATION);
	}
	
	public void setRange(float range)
	{
		this.entityData.set(RANGE, range);
	}
	
	public float getRange()
	{
		return this.entityData.get(RANGE);
	}
}
