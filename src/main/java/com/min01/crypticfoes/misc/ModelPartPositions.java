package com.min01.crypticfoes.misc;

import java.util.Map.Entry;

import com.min01.crypticfoes.network.CrypticNetwork;
import com.min01.crypticfoes.network.UpdateModelPositionPacket;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ModelPartPositions
{
	public final Mob entity;
	public final Object2ObjectOpenHashMap<String, Vec3> parts = new Object2ObjectOpenHashMap<>();
	public final Object2ObjectOpenHashMap<String, Vec3> positions = new Object2ObjectOpenHashMap<>();
	
	public ModelPartPositions(Mob entity) 
	{
		this.entity = entity;
	}
	
	//called in constructor of entity class
	public void addModelPos(String partName, Vec3 extraOffset)
	{
		this.parts.putIfAbsent(partName, extraOffset);
	}
	
	public void putModelPos(String partName, Vec3 worldPos)
	{
		this.positions.put(partName, worldPos);
	}
	
	public Vec3 getModelPos(String partName)
	{
		return this.positions.getOrDefault(partName, Vec3.ZERO);
	}
	
	//called in renderer
	@OnlyIn(Dist.CLIENT)
	public void setModelPos(Mob entity, ModelPart root)
	{
		for(Entry<String, Vec3> parts : this.parts.entrySet())
		{
			String partName = parts.getKey();
			Vec3 extraOffset = parts.getValue();
			Vec3 pos = ModelPartChains.getWorldPosition(entity, root, entity.yBodyRot, extraOffset, partName);
			this.putModelPos(partName, pos);
			CrypticNetwork.sendToServer(new UpdateModelPositionPacket(entity.getUUID(), pos, partName));
		}
	}
}
