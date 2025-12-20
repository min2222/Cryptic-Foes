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

public class StunnedParticle extends TextureSheetParticle
{
	public final SpriteSet sprites;
	public int tick;
	   
	public StunnedParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet sprites) 
	{
		super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
		this.friction = 0.0F;
		this.lifetime = 3;
		this.sprites = sprites;
		this.hasPhysics = false;
		this.setSpriteFromAge(sprites);
	}
	
	@Override
	public ParticleRenderType getRenderType()
	{
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	@Override
	public void tick() 
	{
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		this.tick++;
		if(this.tick % 5 == 0)
		{
			if(this.age < this.lifetime)
			{
				this.age++;
			}
			else
			{
				this.age = 0;
			}
		}
		if(this.tick >= 100 || this.alpha <= 0.0F)
		{
			this.remove();
		}
		else
		{
			this.yd = 0.01F;
			this.move(0.0F, this.yd, 0.0F);
			if(this.alpha > 0.0F)
			{
				this.alpha -= 0.015F;
			}
			this.alpha = Math.max(this.alpha, 0.0F);
		}
		this.setSpriteFromAge(this.sprites);
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
			return new StunnedParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.sprites);
		}
	}
}
