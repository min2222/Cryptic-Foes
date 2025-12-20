package com.min01.crypticfoes.misc;

import com.min01.crypticfoes.CrypticFoes;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CrypticEntityDataSerializers
{
	public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, CrypticFoes.MODID);
	
	public static final RegistryObject<EntityDataSerializer<Vec3>> VEC3 = SERIALIZERS.register("vec3", () -> EntityDataSerializer.simple(CrypticEntityDataSerializers::writeVec3, CrypticEntityDataSerializers::readVec3));

	public static ByteBuf writeVec3(FriendlyByteBuf buf, Vec3 vec3)
	{
		buf.writeDouble(vec3.x);
		buf.writeDouble(vec3.y);
		buf.writeDouble(vec3.z);
		return buf;
	}
	
	public static Vec3 readVec3(ByteBuf buf)
	{
		return new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
	}
}
