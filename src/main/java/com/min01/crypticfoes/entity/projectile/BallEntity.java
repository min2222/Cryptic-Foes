package com.min01.crypticfoes.entity.projectile;

import com.min01.crypticfoes.item.CrypticItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BallEntity extends Projectile implements ItemSupplier
{
	private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(BallEntity.class, EntityDataSerializers.ITEM_STACK);
	
	private static final float ROLL_RADIUS = 0.3125F;
	private static final float ROLL_SPIN_MULTIPLIER = 0.42F;
	private static final float MAX_SPIN_STEP_DEG = 30.0F;
	   
	private float rollX;
	private float rollZ;
	private float rollXOld;
	private float rollZOld;
	
	public BallEntity(EntityType<? extends Projectile> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
	}
	
	@Override
	protected void defineSynchedData() 
	{
		this.entityData.define(DATA_ITEM_STACK, ItemStack.EMPTY);
	}
	
	@Override
	public void tick()
	{
		super.tick();
		HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
		boolean flag = false;
		if(hitresult.getType() == HitResult.Type.BLOCK) 
		{
			BlockPos blockpos = ((BlockHitResult)hitresult).getBlockPos();
			BlockState blockstate = this.level.getBlockState(blockpos);
			if(blockstate.is(Blocks.NETHER_PORTAL))
			{
				this.handleInsidePortal(blockpos);
				flag = true;
			}
			else if(blockstate.is(Blocks.END_GATEWAY))
			{
				BlockEntity blockentity = this.level.getBlockEntity(blockpos);
				if(blockentity instanceof TheEndGatewayBlockEntity && TheEndGatewayBlockEntity.canEntityTeleport(this)) 
				{
	       	    	TheEndGatewayBlockEntity.teleportEntity(this.level, blockpos, blockstate, this, (TheEndGatewayBlockEntity)blockentity);
				}
	       	    flag = true;
			}
		}
		
		if(hitresult.getType() != HitResult.Type.MISS && !flag && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult))
		{
			this.onHit(hitresult);
		}

		this.checkInsideBlocks();
		Vec3 vec3 = this.getDeltaMovement();
		double d2 = this.getX() + vec3.x;
		double d0 = this.getY() + vec3.y;
		double d1 = this.getZ() + vec3.z;
		this.updateRotation();
		float f;
		if(this.isInWater())
		{
			for(int i = 0; i < 4; ++i) 
			{
				this.level.addParticle(ParticleTypes.BUBBLE, d2 - vec3.x * 0.25D, d0 - vec3.y * 0.25D, d1 - vec3.z * 0.25D, vec3.x, vec3.y, vec3.z);
			}
			f = 0.8F;
		} 
		else
		{
			f = 0.99F;
		}
		this.setDeltaMovement(vec3.scale((double)f));
		if(!this.isNoGravity()) 
		{
			Vec3 vec31 = this.getDeltaMovement();
			this.setDeltaMovement(vec31.x, vec31.y - (double)this.getGravity(), vec31.z);
		}
		this.move(MoverType.SELF, this.getDeltaMovement());

		this.rollXOld = this.rollX;
		this.rollZOld = this.rollZ;
		Vec3 movement = this.getDeltaMovement();
		if(this.onGround())
		{
			double y = Math.abs(movement.y) < 0.08D ? 0.0D : movement.y;
			movement = new Vec3(movement.x * 0.9D, y, movement.z * 0.9D);
			if(movement.horizontalDistanceSqr() < 0.0004D && Math.abs(y) < 0.03D)
			{
				movement = Vec3.ZERO;
			}
		}
		else
		{
			movement = movement.scale(0.99D);
		}
		this.setDeltaMovement(movement);
		this.updateRollingRotation();
	}
	
	protected float getGravity()
	{
		return 0.03F;
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
			x *= -0.65D;
		}
		if(direction.getAxis() == Direction.Axis.Y)
		{
			if(Math.abs(y) < 0.12D)
			{
				y = 0.0D;
			}
			else
			{
				y *= -0.35D;
			}
		}
		if(direction.getAxis() == Direction.Axis.Z)
		{
			z *= -0.65D;
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
	public boolean isPushable()
	{
		return true;
	}
	
	@Override
	public void push(Entity pEntity)
	{
		super.push(pEntity);
		if(!this.onGround())
		{
			return;
		}
		Vec3 pushMovement = pEntity.getDeltaMovement();
		if(pushMovement.horizontalDistanceSqr() < 1.0E-5D)
		{
			return;
		}
		Vec3 movement = this.getDeltaMovement().add(pushMovement.x * 0.35D, 0.0D, pushMovement.z * 0.35D);
		double horizontal = movement.horizontalDistance();
		if(horizontal > 0.9D)
		{
			double scale = 0.9D / horizontal;
			movement = new Vec3(movement.x * scale, movement.y, movement.z * scale);
		}
		this.setDeltaMovement(movement.x, this.getDeltaMovement().y, movement.z);
		this.hasImpulse = true;
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
	public void addAdditionalSaveData(CompoundTag pCompound)
	{
		super.addAdditionalSaveData(pCompound);
		pCompound.putFloat("RollX", this.rollX);
		pCompound.putFloat("RollZ", this.rollZ);
		ItemStack stack = this.getItemRaw();
		if(!stack.isEmpty())
		{
			pCompound.put("Item", stack.save(new CompoundTag()));
		}
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag pCompound)
	{
		super.readAdditionalSaveData(pCompound);
		this.rollX = pCompound.getFloat("RollX");
		this.rollZ = pCompound.getFloat("RollZ");
		this.rollXOld = this.rollX;
		this.rollZOld = this.rollZ;
		ItemStack itemstack = ItemStack.of(pCompound.getCompound("Item"));
		this.setItem(itemstack);
	}
	
	public float getRollX(float partialTick)
	{
		return Mth.lerp(partialTick, this.rollXOld, this.rollX);
	}
	
	public float getRollZ(float partialTick)
	{
		return Mth.lerp(partialTick, this.rollZOld, this.rollZ);
	}
	
	protected Item getDefaultItem()
	{
		return CrypticItems.BALL.get();
	}
	
	public void setItem(ItemStack pStack) 
	{
		if(!pStack.is(this.getDefaultItem()) || pStack.hasTag()) 
		{
			this.getEntityData().set(DATA_ITEM_STACK, pStack.copyWithCount(1));
		}
	}

	protected ItemStack getItemRaw() 
	{
		return this.getEntityData().get(DATA_ITEM_STACK);
	}

	@Override
	public ItemStack getItem() 
	{
		ItemStack stack = this.getItemRaw();
		return stack.isEmpty() ? new ItemStack(this.getDefaultItem()) : stack;
	}
	
	private void updateRollingRotation()
	{
		double dx = this.getX() - this.xo;
		double dz = this.getZ() - this.zo;
		double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
		if(horizontalDistance < 1.0E-4D)
		{
			return;
		}
		float rotationDegrees = (float)(horizontalDistance / (double)ROLL_RADIUS * (180.0D / Math.PI)) * ROLL_SPIN_MULTIPLIER;
		float spinX = (float)(dz / horizontalDistance) * rotationDegrees;
		float spinZ = -(float)(dx / horizontalDistance) * rotationDegrees;
		float magSq = spinX * spinX + spinZ * spinZ;
		if(magSq > MAX_SPIN_STEP_DEG * MAX_SPIN_STEP_DEG)
		{
			float mag = Mth.sqrt(magSq);
			float scale = MAX_SPIN_STEP_DEG / mag;
			spinX *= scale;
			spinZ *= scale;
		}
		this.rollX += spinX;
		this.rollZ += spinZ;
	}
}
