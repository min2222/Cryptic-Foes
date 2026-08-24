package com.min01.crypticfoes.api.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector4f;

import com.min01.crypticfoes.api.entity.IAnimatable;
import com.min01.crypticfoes.network.CrypticNetwork;
import com.min01.crypticfoes.network.UpdateModelPartPosPacket;
import com.mojang.blaze3d.vertex.PoseStack;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

public class ModelPartChain 
{
	private static final WeakHashMap<ModelPart, Map<String, List<ModelPart>>> CACHE = new WeakHashMap<>();
	
	public static Map<String, List<ModelPart>> getChains(ModelPart root)
	{
	    return CACHE.computeIfAbsent(root, ModelPartChain::build);
	}
	
	public static Map<String, List<ModelPart>> build(ModelPart root)
	{
	    Object2ObjectOpenHashMap<String, List<ModelPart>> chains = new Object2ObjectOpenHashMap<>();
	    collect(root, Collections.emptyList(), chains);
	    return Map.copyOf(chains);
	}

	public static void collect(ModelPart root, List<ModelPart> list, Object2ObjectOpenHashMap<String, List<ModelPart>> chains) 
	{
		for(Map.Entry<String, ModelPart> entry : root.children.entrySet())
		{
			String name = entry.getKey();
			ModelPart child = entry.getValue();
			List<ModelPart> newChain = new ArrayList<>(list.size() + 1);
			newChain.addAll(list);
			newChain.add(child);
			chains.putIfAbsent(name, List.copyOf(newChain));
			collect(child, newChain, chains);
		}
	}

	//https://github.com/EEEAB/EEEABsMobs/blob/master/src/main/java/com/eeeab/animate/client/util/ModelPartUtils.java#L57
    public static Vec3 getWorldPosition(Entity entity, ModelPart root, float yBodyRot, Vec3 extraOffset, String partName)
    {
    	List<ModelPart> chain = getChains(root).get(partName);
    	if(chain == null)
    	{
    		return Vec3.ZERO;
    	}
        PoseStack poseStack = new PoseStack();
        Minecraft minecraft = Minecraft.getInstance();
        float partialTick = minecraft.getPartialTick();
        double x = Mth.lerp((double)partialTick, entity.xOld, entity.getX());
        double y = Mth.lerp((double)partialTick, entity.yOld, entity.getY());
        double z = Mth.lerp((double)partialTick, entity.zOld, entity.getZ());
    	poseStack.translate(x, y, z);
        Quaternionf quat = new Quaternionf().rotateXYZ(0.0F, (float) Math.toRadians(-yBodyRot + 180.0F), 0.0F);
        poseStack.mulPose(quat);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        for(ModelPart part : chain) 
        {
            part.translateAndRotate(poseStack);
        }
        poseStack.translate(extraOffset.x, extraOffset.y, extraOffset.z);
        PoseStack.Pose last = poseStack.last();
        Matrix4f matrix4f = last.pose();
        Vector4f vector4f = new Vector4f(0, 0, 0, 1);
        vector4f.mul(matrix4f);
        return new Vec3(vector4f.x(), vector4f.y(), vector4f.z());
    }
	
	//call in render method of entity renderer;
	public static <T extends Mob & IAnimatable> void setPos(T entity, ModelPart root)
	{
		ModelPartPos partPos = entity.getModelPartPos();
		for(Entry<String, Vec3> entry : partPos.getParts().entrySet())
		{
			String partName = entry.getKey();
			Vec3 extraOffset = entry.getValue();
			Vec3 pos = getWorldPosition(entity, root, entity.yBodyRot, extraOffset, partName);
			partPos.setPos(partName, pos);
			CrypticNetwork.sendToServer(new UpdateModelPartPosPacket(entity.getUUID(), pos, partName));
		}
	}
}
