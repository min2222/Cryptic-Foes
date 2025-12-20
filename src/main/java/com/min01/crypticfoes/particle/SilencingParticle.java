package com.min01.crypticfoes.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SilencingParticle extends TextureSheetParticle
{
	public SilencingParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet sprites) 
	{
		super(pLevel, pX, pY, pZ, 0.0D, 0.0D, 0.0D);
		this.friction = 0.96F;
		this.speedUpWhenYMotionIsBlocked = true;
		this.quadSize *= 0.75F;
		this.hasPhysics = false;
		this.pickSprite(sprites);
	}
	
	@Override
	public ParticleRenderType getRenderType()
	{
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType>
	{
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) 
		{
			this.sprites = sprites;
		}
		
		@Override
		public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed)
		{
			SilencingParticle particle = new SilencingParticle(pLevel, pX, pY, pZ, this.sprites);
			particle.setParticleSpeed(pXSpeed * 0.01D / 2.0D, pYSpeed * 0.01D, pZSpeed * 0.01D / 2.0D);
			particle.setLifetime(pLevel.random.nextInt(30) + 10);
			return particle;
		}
	}
}
