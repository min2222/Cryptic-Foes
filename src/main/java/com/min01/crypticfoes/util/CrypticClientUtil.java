package com.min01.crypticfoes.util;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class CrypticClientUtil 
{
	public static final Minecraft MC = Minecraft.getInstance();
	
    public static void drawQuad(PoseStack stack, VertexConsumer consumer, float size, int packedLightIn, float alpha) 
    {
        float minU = 0;
        float minV = 0;
        float maxU = 1;
        float maxV = 1;
        PoseStack.Pose matrixstack$entry = stack.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        drawVertex(matrix4f, matrix3f, consumer, size, size, 0, minU, minV, alpha, packedLightIn);
        drawVertex(matrix4f, matrix3f, consumer, size, -size, 0, minU, maxV, alpha, packedLightIn);
        drawVertex(matrix4f, matrix3f, consumer, -size, -size, 0, maxU, maxV, alpha, packedLightIn);
        drawVertex(matrix4f, matrix3f, consumer, -size, size, 0, maxU, minV, alpha, packedLightIn);
    }
    
    public static void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, float alpha, int packedLightIn)
    {
    	vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ).color(1, 1, 1, alpha).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(normals, 0.0F, 1.0F, 0.0F).endVertex();
    }
	
	public static void animateHead(ModelPart head, float netHeadYaw, float headPitch)
	{
		head.yRot += Math.toRadians(netHeadYaw);
		head.xRot += Math.toRadians(headPitch);
	}
	
	//https://github.com/EEEAB/EEEABsMobs/blob/master/src/main/java/com/eeeab/animate/client/util/ModelPartUtils.java#L57
    
    public static Vec3 getWorldPosition(Entity entity, ModelPart root, Vec3 rotation, String... modelPartName)
    {
        PoseStack poseStack = new PoseStack();
    	poseStack.translate(entity.getX(), entity.getY(), entity.getZ());
        poseStack.mulPose(new Quaternionf().rotateXYZ((float) Math.toRadians(rotation.x), (float) Math.toRadians(-rotation.y + 180.0F), (float) Math.toRadians(rotation.z)));
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        ModelPart nextPart = null;
        for(int i = 0; i < modelPartName.length; i++)
        {
            if(i == 0)
            {
                nextPart = root.getChild(modelPartName[0]);
                nextPart.translateAndRotate(poseStack);
            }
            else 
            {
                ModelPart child = nextPart.getChild(modelPartName[i]);
                child.translateAndRotate(poseStack);
                nextPart = child;
            }
        }
        PoseStack.Pose last = poseStack.last();
        Matrix4f matrix4f = last.pose();
        Vector4f vector4f = new Vector4f(0, 0, 0, 1);
        vector4f.mul(matrix4f);
        return new Vec3(vector4f.x(), vector4f.y(), vector4f.z());
    }
}
