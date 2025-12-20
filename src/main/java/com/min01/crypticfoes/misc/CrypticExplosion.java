package com.min01.crypticfoes.misc;

import javax.annotation.Nullable;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

public class CrypticExplosion extends Explosion
{
	private final Level level;
	private final double x;
	private final double y;
	private final double z;
	private final ParticleOptions particle;
	   
	public CrypticExplosion(Level pLevel, @Nullable Entity pSource, double pToBlowX, double pToBlowY, double pToBlowZ, float pRadius, ParticleOptions particle) 
	{
		super(pLevel, pSource, pToBlowX, pToBlowY, pToBlowZ, pRadius, false, BlockInteraction.KEEP);
		this.level = pLevel;
		this.x = pToBlowX;
		this.y = pToBlowY;
		this.z = pToBlowZ;
		this.particle = particle;
	}
	
	@Override
	public void finalizeExplosion(boolean pSpawnParticles) 
	{
		super.finalizeExplosion(pSpawnParticles);
		if(this.level instanceof ServerLevel serverLevel)
		{
			serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1, 1, 0, 0, 1);
			serverLevel.sendParticles(this.particle, this.x, this.y, this.z, 1, 1, 0, 0, 1);
		}
	}
}
