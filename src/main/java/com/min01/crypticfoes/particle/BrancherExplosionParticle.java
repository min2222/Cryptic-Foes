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

public class BrancherExplosionParticle extends TextureSheetParticle
{
	public BrancherExplosionParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pQuadSizeMultiplier, SpriteSet sprites)
	{
		super(pLevel, pX, pY, pZ, 0.0D, 0.0D, 0.0D);
		this.lifetime = 50 + this.random.nextInt(4);
		this.quadSize = 2.0F * (1.0F - (float)pQuadSizeMultiplier * 0.5F);
		this.pickSprite(sprites);
	}
	
	@Override
	public float getQuadSize(float pScaleFactor) 
	{
		return super.getQuadSize(pScaleFactor) * 0.1F;
	}
	
	@Override
	public void tick() 
	{
		super.tick();
		this.yd -= 0.01D;
		if(this.age <= 20)
		{
			this.alpha -= 0.05D;
		}
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
			return new BrancherExplosionParticle(pLevel, pXSpeed, pYSpeed, pZ, pZSpeed, this.sprites);
		}
	}
}
