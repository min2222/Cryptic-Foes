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
	   
	protected StunnedParticle(ClientLevel p_107647_, double p_107648_, double p_107649_, double p_107650_, double xd, double yd, double zd, SpriteSet p_107651_) 
	{
		super(p_107647_, p_107648_, p_107649_, p_107650_, xd, yd, zd);
		this.friction = 0.0F;
		this.lifetime = 3;
		this.sprites = p_107651_;
		this.hasPhysics = false;
		this.setSpriteFromAge(p_107651_);
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

		public Provider(SpriteSet p_106555_) 
		{
			this.sprites = p_106555_;
		}
		
		@Override
		public Particle createParticle(SimpleParticleType p_107421_, ClientLevel p_107422_, double p_107423_, double p_107424_, double p_107425_, double p_107426_, double p_107427_, double p_107428_) 
		{
			return new StunnedParticle(p_107422_, p_107423_, p_107424_, p_107425_, p_107426_, p_107427_, p_107428_, this.sprites);
		}
	}
}
