package com.min01.crypticfoes.event;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.capabilties.RollingCapabilityImpl;
import com.min01.crypticfoes.effect.CrypticEffects;
import com.min01.crypticfoes.enchantment.CrypticEnchantments;
import com.min01.crypticfoes.entity.CameraShakeEntity;
import com.min01.crypticfoes.item.model.RollingArmorBallModel;
import com.min01.crypticfoes.shader.CrypticEntityEffect;
import com.min01.crypticfoes.util.CrypticClientUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.player.Input;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrypticFoes.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandlerForge
{
	public static final ResourceLocation BALL_TEXTURE = ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "textures/armor/rolling_armor_ball.png");
	
	@SubscribeEvent
	public static void onRenderArm(RenderArmEvent event)
	{
		if(CrypticEntityEffect.INSTANCE.shouldShowEntityEffect())
		{
	        RenderSystem.setShaderColor(100.0F, 100.0F, 100.0F, 1.0F);
		}
	}
	
	@SubscribeEvent
	public static void onRenderPlayerPre(RenderPlayerEvent.Pre event)
	{
		Player player = event.getEntity();
		PoseStack poseStack = event.getPoseStack();
		MultiBufferSource bufferSource = event.getMultiBufferSource();
		RollingArmorBallModel<Player> model = new RollingArmorBallModel<>(CrypticClientUtil.MC.getEntityModels().bakeLayer(RollingArmorBallModel.LAYER_LOCATION));
		float partialTick = event.getPartialTick();
		float yBodyRot = Mth.rotLerp(partialTick, player.yBodyRotO, player.yBodyRot);
		float yHeadRot = Mth.rotLerp(partialTick, player.yHeadRotO, player.yHeadRot);
		player.getCapability(RollingCapabilityImpl.ROLLING).ifPresent(t -> 
		{
			if(t.isRolling())
			{
				event.setCanceled(true);
				poseStack.pushPose();
				poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yBodyRot));
				poseStack.scale(-1.0F, -1.0F, 1.0F);
				poseStack.translate(0.0F, -1.5F, 0.0F);
				float yaw = t.getRollingYaw();
			    if(player.getItemBySlot(EquipmentSlot.CHEST).getEnchantmentLevel(CrypticEnchantments.MOBILITY.get()) > 0)
			    {
			    	yaw = yHeadRot;
			    }
				model.setupAnim(player, 0, 0, player.tickCount + partialTick, yaw - yBodyRot, 0);
				model.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityCutoutNoCull(BALL_TEXTURE)), event.getPackedLight(), OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
				poseStack.popPose();
			}
		});
	}
	
	@SubscribeEvent
	public static void onMovementInputUpdate(MovementInputUpdateEvent event)
	{
		Player player = event.getEntity();
		Input input = event.getInput();
		player.getCapability(RollingCapabilityImpl.ROLLING).ifPresent(t -> 
		{
			if(t.isRolling())
			{
				//FIXME not working, seems like impulse have cap
				float speed = t.getRollingSpeed();
			    float baseLeft = 0.0F;
			    float baseForward = input.forwardImpulse * speed;
			    float delta = t.getRollingYaw() - player.getYHeadRot();
			    float rad = delta * Mth.DEG_TO_RAD;
			    float cos = Mth.cos(rad);
			    float sin = Mth.sin(rad);
			    float rotatedLeft = baseLeft * cos - baseForward * sin;
			    float rotatedForward = baseLeft * sin + baseForward * cos;
			    if(player.getItemBySlot(EquipmentSlot.CHEST).getEnchantmentLevel(CrypticEnchantments.MOBILITY.get()) > 0)
			    {
			    	rotatedLeft = 0.0F;
			    	rotatedForward = baseForward;
			    }
			    input.leftImpulse = rotatedLeft;
			    input.forwardImpulse = rotatedForward;
			    input.down = false;
			}
		});
	}
	
	@SubscribeEvent
	public static void onRenderLivingPost(RenderLivingEvent.Post<?, ?> event)
	{
		CrypticEntityEffect.INSTANCE.enabled = true;
	}
	
	@SubscribeEvent
	public static void onRenderLevelStage(RenderLevelStageEvent event)
	{
		if(event.getStage() == Stage.AFTER_SOLID_BLOCKS)
		{
			CrypticEntityEffect.INSTANCE.enabled = true;
		}
	}
	
	@SubscribeEvent
	public static void onComputeFovModifier(ComputeFovModifierEvent event)
	{
		if(CrypticClientUtil.MC.player.hasEffect(CrypticEffects.STUNNED.get()) && !CrypticClientUtil.MC.player.isSpectator())
		{
			float fov = event.getFovModifier();
			event.setNewFovModifier(fov - 0.5F);
		}
	}
	
    @SubscribeEvent
    public static void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) 
    {
        Player player = CrypticClientUtil.MC.player;
        float delta = CrypticClientUtil.MC.getFrameTime();
        float ticksExistedDelta = player.tickCount + delta;
        if(player != null)
        {
            float shakeAmplitude = 0.0F;
            for(CameraShakeEntity cameraShake : player.level.getEntitiesOfClass(CameraShakeEntity.class, player.getBoundingBox().inflate(100.0F))) 
            {
                if(cameraShake.distanceTo(player) < cameraShake.getRadius())
                {
                    shakeAmplitude += cameraShake.getShakeAmount(player, delta);
                }
            }
            if(shakeAmplitude > 1.0F)
            {
                shakeAmplitude = 1.0F;
            }
            event.setPitch((float)(event.getPitch() + shakeAmplitude * Math.cos(ticksExistedDelta * 3.0F + 2.0F) * 25.0));
            event.setYaw((float)(event.getYaw() + shakeAmplitude * Math.cos(ticksExistedDelta * 5.0F + 1.0F) * 25.0));
            event.setRoll((float)(event.getRoll() + shakeAmplitude * Math.cos(ticksExistedDelta * 4.0F) * 25.0));
        }
    }
    
	@SubscribeEvent
	public static void onInteractionKeyMappingTriggered(InputEvent.InteractionKeyMappingTriggered event)
	{
		if(CrypticClientUtil.MC.player.hasEffect(CrypticEffects.STUNNED.get()) && !CrypticClientUtil.MC.player.isSpectator())
		{
			event.setSwingHand(false);
			event.setCanceled(true);
		}
	}
}
