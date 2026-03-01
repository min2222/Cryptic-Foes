package com.min01.crypticfoes.entity;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.entity.living.EntityHowler;
import com.min01.crypticfoes.entity.projectile.EntityHowlerScream;

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
	
	public static final RegistryObject<EntityType<EntityHowler>> HOWLER = registerEntity("howler", createBuilder(EntityHowler::new, MobCategory.MONSTER).sized(0.75F, 2.625F));
	
	public static final RegistryObject<EntityType<EntityHowlerScream>> HOWLER_SCREAM = registerEntity("howler_scream", createBuilder(EntityHowlerScream::new, MobCategory.MISC).sized(0.5F, 0.5F));

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
