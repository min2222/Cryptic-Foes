package com.min01.crypticfoes.api.entity;

import com.min01.crypticfoes.config.CrypticConfig;
import com.min01.crypticfoes.entity.CrypticEntities;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.network.NetworkHooks;

public class CameraShakeEntity extends Entity 
{
	private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(CameraShakeEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> MAGNITUDE = SynchedEntityData.defineId(CameraShakeEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(CameraShakeEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> FADE_DURATION = SynchedEntityData.defineId(CameraShakeEntity.class, EntityDataSerializers.INT);

    public CameraShakeEntity(EntityType<?> type, Level world) 
    {
        super(type, world);
    }

    public CameraShakeEntity(Level world, Vec3 position, float radius, float magnitude, int duration, int fadeDuration) 
    {
        super(CrypticEntities.CAMERA_SHAKE.get(), world);
        this.setRadius(radius);
        this.setMagnitude(magnitude);
        this.setDuration(duration);
        this.setFadeDuration(fadeDuration);
        this.setPos(position);
    }

    @Override
    protected void defineSynchedData()
    {
        this.entityData.define(RADIUS, 10.0F);
        this.entityData.define(MAGNITUDE, 1.0F);
        this.entityData.define(DURATION, 0);
        this.entityData.define(FADE_DURATION, 5);
    }

    @Override
    public void tick() 
    {
        super.tick();
        if(this.tickCount > this.getDuration() + this.getFadeDuration()) 
        {
        	this.discard();
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) 
    {
        this.setRadius(compound.getFloat("Radius"));
        this.setMagnitude(compound.getFloat("Magnitude"));
        this.setDuration(compound.getInt("Duration"));
        this.setFadeDuration(compound.getInt("FadeDuration"));
        this.tickCount = compound.getInt("TickCount");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound)
    {
        compound.putFloat("Radius", this.getRadius());
        compound.putFloat("Magnitude", this.getMagnitude());
        compound.putInt("Duration", this.getDuration());
        compound.putInt("FadeDuration", this.getFadeDuration());
        compound.putInt("TickCount", this.tickCount);
    }

    public void setRadius(float radius)
    {
        this.entityData.set(RADIUS, radius);
    }

    public float getRadius() 
    {
        return this.entityData.get(RADIUS);
    }

    public void setMagnitude(float magnitude) 
    {
        this.entityData.set(MAGNITUDE, magnitude);
    }

    public float getMagnitude() 
    {
        return this.entityData.get(MAGNITUDE);
    }

    public void setDuration(int duration)
    {
        this.entityData.set(DURATION, duration);
    }

    public int getDuration() 
    {
        return this.entityData.get(DURATION);
    }

    public void setFadeDuration(int fadeDuration) 
    {
        this.entityData.set(FADE_DURATION, fadeDuration);
    }

    public int getFadeDuration()
    {
        return this.entityData.get(FADE_DURATION);
    }

    public float getShakeAmount(Player player, float partialTick) 
    {
        float tick = this.tickCount + partialTick;
        float timeFraction = 1.0F - (tick - this.getDuration()) / (this.getFadeDuration() + 1.0F);
        float amount = tick < this.getDuration() ? this.getMagnitude() : timeFraction * timeFraction * this.getMagnitude();
        float distFraction = (float) (1.0F - Mth.clamp(this.position().distanceTo(player.getEyePosition(partialTick)) / this.getRadius(), 0, 1));
        return amount * distFraction * distFraction;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() 
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public static void cameraShake(Level world, Vec3 position, float radius, float magnitude, int duration, int fadeDuration)
    {
    	if(!CrypticConfig.cameraShakes.get())
    	{
    		return;
    	}
        if(!world.isClientSide) 
        {
            CameraShakeEntity cameraShake = new CameraShakeEntity(world, position, radius, magnitude, duration, fadeDuration);
            world.addFreshEntity(cameraShake);
        }
    }

    @OnlyIn(Dist.CLIENT)
	public static void cameraShake(ViewportEvent.ComputeCameraAngles event)
	{
    	if(!CrypticConfig.cameraShakes.get())
    	{
    		return;
    	}
		Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        float partialTick = minecraft.getPartialTick();
        float tick = player.tickCount + partialTick;
        if(player != null)
        {
            float amplitude = 0.0F;
            for(CameraShakeEntity cameraShake : player.level.getEntitiesOfClass(CameraShakeEntity.class, player.getBoundingBox().inflate(100.0F))) 
            {
                if(cameraShake.distanceTo(player) < cameraShake.getRadius())
                {
                	amplitude += cameraShake.getShakeAmount(player, partialTick);
                }
            }
            amplitude = Math.min(amplitude, 1.0F);
            event.setPitch((float)(event.getPitch() + amplitude * Math.cos(tick * 3.0F + 2.0F) * 25.0));
            event.setYaw((float)(event.getYaw() + amplitude * Math.cos(tick * 5.0F + 1.0F) * 25.0));
            event.setRoll((float)(event.getRoll() + amplitude * Math.cos(tick * 4.0F) * 25.0));
        }
	}
}
