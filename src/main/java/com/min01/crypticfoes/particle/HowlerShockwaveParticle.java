package com.min01.crypticfoes.particle;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class HowlerShockwaveParticle extends TextureSheetParticle
{
	public HowlerShockwaveParticle(ClientLevel pLevel, double pX, double pY, double pZ, int lifeTime, SpriteSet sprites) 
	{
		super(pLevel, pX, pY, pZ, 0.0D, 0.0D, 0.0D);
		this.quadSize = 0.0F;
		this.lifetime = lifeTime;
		this.pickSprite(sprites);
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
		if(this.age++ >= this.lifetime)
		{
			this.remove();
		}
		else
		{
			if(this.lifetime >= 60)
			{
				this.quadSize += 0.25F;
				this.alpha = Mth.lerp(this.age / 100.0F, this.alpha, 0.0F);
			}
			else
			{
				this.quadSize += 0.15F;
				if(this.alpha > 0.0F)
				{
					this.alpha -= 0.05F;
				}
			}
		}
	}
	
	@Override
	public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks)
	{
		Vec3 vec3 = pRenderInfo.getPosition();
		float f = (float)(Mth.lerp((double)pPartialTicks, this.xo, this.x) - vec3.x());
		float f1 = (float)(Mth.lerp((double)pPartialTicks, this.yo, this.y) - vec3.y());
		float f2 = (float)(Mth.lerp((double)pPartialTicks, this.zo, this.z) - vec3.z());
		Quaternionf quaternionf = Axis.XP.rotationDegrees(90.0F);

		Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
		float f3 = this.getQuadSize(pPartialTicks);

		for(int i = 0; i < 4; ++i) 
		{
			Vector3f vector3f = avector3f[i];
			vector3f.rotate(quaternionf);
			vector3f.mul(f3);
			vector3f.add(f, f1, f2);
		}

		float f6 = this.getU0();
		float f7 = this.getU1();
		float f4 = this.getV0();
		float f5 = this.getV1();
		int j = this.getLightColor(pPartialTicks);
		pBuffer.vertex((double)avector3f[0].x(), (double)avector3f[0].y(), (double)avector3f[0].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
		pBuffer.vertex((double)avector3f[1].x(), (double)avector3f[1].y(), (double)avector3f[1].z()).uv(f7, f4).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
		pBuffer.vertex((double)avector3f[2].x(), (double)avector3f[2].y(), (double)avector3f[2].z()).uv(f6, f4).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
		pBuffer.vertex((double)avector3f[3].x(), (double)avector3f[3].y(), (double)avector3f[3].z()).uv(f6, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
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
			return new HowlerShockwaveParticle(pLevel, pX, pY, pZ, (int) pXSpeed, this.sprites);
		}
	}
}
