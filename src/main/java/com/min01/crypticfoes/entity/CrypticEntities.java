package com.min01.crypticfoes.entity;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.entity.living.BumpyEntity;
import com.min01.crypticfoes.entity.living.HowlerEntity;
import com.min01.crypticfoes.entity.projectile.BallEntity;
import com.min01.crypticfoes.entity.projectile.HowlerScreamEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CrypticEntities
{
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CrypticFoes.MODID);
	
	public static final RegistryObject<EntityType<HowlerEntity>> HOWLER = registerEntity("howler", createBuilder(HowlerEntity::new, MobCategory.MONSTER).sized(0.75F, 2.625F));
	public static final RegistryObject<EntityType<BumpyEntity>> BUMPY = registerEntity("bumpy", createBuilder(BumpyEntity::new, MobCategory.CREATURE).sized(1.0F, 1.3125F));
	
	public static final RegistryObject<EntityType<HowlerScreamEntity>> HOWLER_SCREAM = registerEntity("howler_scream", createBuilder(HowlerScreamEntity::new, MobCategory.MISC).sized(0.5F, 0.5F));
	public static final RegistryObject<EntityType<BallEntity>> BALL = registerEntity("ball", createBuilder(BallEntity::new, MobCategory.MISC).sized(0.625F, 0.625F));

	public static final RegistryObject<EntityType<EntityCameraShake>> CAMERA_SHAKE = registerEntity("camera_shake", EntityType.Builder.<EntityCameraShake>of(EntityCameraShake::new, MobCategory.MISC).sized(0.0F, 0.0F));
	
	public static <T extends Entity> EntityType.Builder<T> createBuilder(EntityType.EntityFactory<T> factory, MobCategory category)
	{
		return EntityType.Builder.<T>of(factory, category);
	}
	
	public static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name, EntityType.Builder<T> builder) 
	{
		return ENTITY_TYPES.register(name, () -> builder.build(ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, name).toString()));
	}
}
