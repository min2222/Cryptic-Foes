package com.min01.crypticfoes.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DustPillarParticle
{
	@OnlyIn(Dist.CLIENT)
	public static class Provider extends TerrainParticle.Provider
	{
		@Override
		public Particle createParticle(BlockParticleOption pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed)
		{
			Particle particle = super.createParticle(pType, pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
			if(particle != null)
			{
                particle.setParticleSpeed(pLevel.random.nextGaussian() / 30.0, pYSpeed + pLevel.random.nextGaussian() / 2.0, pLevel.random.nextGaussian() / 30.0);
                particle.setLifetime(pLevel.random.nextInt(20) + 20);
			}
			return particle;
		}
	}
}
