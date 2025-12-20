package com.min01.crypticfoes.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BrancherExplosionSeedParticle extends NoRenderParticle
{
	private int life;
	private final int lifeTime = 8;

	public BrancherExplosionSeedParticle(ClientLevel pLevel, double pX, double pY, double pZ) 
	{
		super(pLevel, pX, pY, pZ, 0.0D, 0.0D, 0.0D);
	}

	@Override
	public void tick()
	{
		for(int i = 0; i < 6; ++i) 
		{
			double d0 = this.x + (this.random.nextDouble() - this.random.nextDouble()) * 4.0D;
			double d1 = this.y + (this.random.nextDouble() - this.random.nextDouble()) * 4.0D;
			double d2 = this.z + (this.random.nextDouble() - this.random.nextDouble()) * 4.0D;
			this.level.addParticle(CrypticParticles.BRANCHER_EXPLOSION.get(), d0, d1, d2, (double) ((float) this.life / (float) this.lifeTime), 0.0D, 0.0D);
		}

		++this.life;
		if(this.life == this.lifeTime)
		{
			this.remove();
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> 
	{
		@Override
		public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed)
		{
			return new BrancherExplosionSeedParticle(pLevel, pX, pY, pZ);
		}
	}
}
