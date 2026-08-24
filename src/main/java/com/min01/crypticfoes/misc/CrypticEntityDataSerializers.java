package com.min01.crypticfoes.misc;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CrypticEntityDataSerializers
{
	public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, CrypticFoes.MODID);
	
	public static final RegistryObject<EntityDataSerializer<Vec3>> VEC3 = SERIALIZERS.register("vec3", () -> EntityDataSerializer.simple(CrypticUtil::writeVec3, CrypticUtil::readVec3));
}
