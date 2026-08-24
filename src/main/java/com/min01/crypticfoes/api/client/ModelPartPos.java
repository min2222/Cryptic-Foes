package com.min01.crypticfoes.api.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.world.phys.Vec3;

public class ModelPartPos
{
	private final Object2ObjectOpenHashMap<String, Vec3> parts = new Object2ObjectOpenHashMap<>();
	private final Object2ObjectOpenHashMap<String, Vec3> positions = new Object2ObjectOpenHashMap<>();
	
	//call in constructor of entity class;
	public void addPos(String partName, Vec3 extraOffset)
	{
		this.parts.putIfAbsent(partName, extraOffset);
	}
	
	public void setPos(String partName, Vec3 worldPos)
	{
		this.positions.put(partName, worldPos);
	}
	
	public Vec3 getPos(String partName)
	{
		return this.positions.getOrDefault(partName, Vec3.ZERO);
	}
	
	public Object2ObjectOpenHashMap<String, Vec3> getParts()
	{
		return this.parts;
	}
}
