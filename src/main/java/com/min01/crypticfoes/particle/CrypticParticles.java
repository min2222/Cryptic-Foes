package com.min01.crypticfoes.particle;

import com.min01.crypticfoes.CrypticFoes;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CrypticParticles 
{
	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, CrypticFoes.MODID);
	
	public static final RegistryObject<SimpleParticleType> BRANCHER_EXPLOSION = PARTICLES.register("brancher_explosion", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> BRANCHER_EXPLOSION_SEED = PARTICLES.register("brancher_explosion_seed", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> SILENCING = PARTICLES.register("silencing", () -> new SimpleParticleType(false));
}
