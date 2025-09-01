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
	protected SilencingParticle(ClientLevel p_107647_, double p_107648_, double p_107649_, double p_107650_, SpriteSet p_107651_) 
	{
		super(p_107647_, p_107648_, p_107649_, p_107650_, 0.0D, 0.0D, 0.0D);
		this.friction = 0.96F;
		this.speedUpWhenYMotionIsBlocked = true;
		this.quadSize *= 0.75F;
		this.hasPhysics = false;
		this.pickSprite(p_107651_);
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

		public Provider(SpriteSet p_106555_) 
		{
			this.sprites = p_106555_;
		}
		
		@Override
		public Particle createParticle(SimpleParticleType p_107421_, ClientLevel p_107422_, double p_107423_, double p_107424_, double p_107425_, double p_107426_, double p_107427_, double p_107428_) 
		{
			SilencingParticle particle = new SilencingParticle(p_107422_, p_107423_, p_107424_, p_107425_, this.sprites);
			particle.setParticleSpeed(p_107426_ * 0.01D / 2.0D, p_107427_ * 0.01D, p_107428_ * 0.01D / 2.0D);
			particle.setLifetime(p_107422_.random.nextInt(30) + 10);
			return particle;
		}
	}
}
