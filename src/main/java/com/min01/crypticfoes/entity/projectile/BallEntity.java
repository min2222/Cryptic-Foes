package com.min01.crypticfoes.entity.projectile;

import com.min01.crypticfoes.item.CrypticItems;

import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class BallEntity extends ThrowableItemProjectile
{
	public BallEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
	}
	
	@Override
	public void tick()
	{
		super.tick();
		Vec3 movement = this.getDeltaMovement();
		if(this.onGround())
		{
			double y = Math.abs(movement.y) < 0.08D ? 0.0D : movement.y;
			movement = new Vec3(movement.x * 0.93D, y, movement.z * 0.93D);
			if(movement.horizontalDistanceSqr() < 0.0004D)
			{
				movement = new Vec3(0.0D, y, 0.0D);
			}
		}
		else
		{
			movement = movement.scale(0.99D);
		}
		this.setDeltaMovement(movement);
	}
	
	@Override
	protected void onHitBlock(BlockHitResult pResult)
	{
		super.onHitBlock(pResult);
		Direction direction = pResult.getDirection();
		Vec3 movement = this.getDeltaMovement();
		double x = movement.x;
		double y = movement.y;
		double z = movement.z;
		if(direction.getAxis() == Direction.Axis.X)
		{
			x *= -0.75D;
		}
		if(direction.getAxis() == Direction.Axis.Y)
		{
			y *= -0.7D;
		}
		if(direction.getAxis() == Direction.Axis.Z)
		{
			z *= -0.75D;
		}
		this.setDeltaMovement(x, y, z);
	}
	
	@Override
	protected void onHitEntity(EntityHitResult pResult)
	{
		super.onHitEntity(pResult);
		Entity target = pResult.getEntity();
		Vec3 movement = this.getDeltaMovement();
		target.push(movement.x * 0.35D, 0.1D, movement.z * 0.35D);
		this.setDeltaMovement(movement.scale(-0.6D));
	}
	
	@Override
	public boolean hurt(DamageSource pSource, float pAmount)
	{
		Entity entity = pSource.getEntity();
		if(entity != null)
		{
			Vec3 look = entity.getLookAngle().normalize();
			double y = Math.max(0.2D, look.y * 0.3D);
			this.setDeltaMovement(look.x * 0.9D, y, look.z * 0.9D);
			this.setOnGround(false);
			this.hasImpulse = true;
			this.playSound(SoundEvents.WOOL_HIT, 0.8F, 0.9F + this.random.nextFloat() * 0.2F);
		}
		return true;
	}
	
	@Override
	public InteractionResult interact(Player pPlayer, InteractionHand pHand)
	{
		if(pPlayer.isShiftKeyDown())
		{
			if(!this.level.isClientSide)
			{
				ItemStack stack = this.getItem().copy();
				if(stack.isEmpty())
				{
					stack = new ItemStack(CrypticItems.BALL.get());
				}
				if(!pPlayer.addItem(stack))
				{
					pPlayer.drop(stack, false);
				}
				this.playSound(SoundEvents.ITEM_PICKUP, 0.2F, 1.0F);
				this.discard();
			}
			return InteractionResult.sidedSuccess(this.level.isClientSide);
		}
		return InteractionResult.PASS;
	}
	
	@Override
	public boolean isPickable()
	{
		return true;
	}
	
	@Override
	public boolean isAttackable()
	{
		return true;
	}
	
	@Override
	protected Item getDefaultItem()
	{
		return CrypticItems.BALL.get();
	}
}
