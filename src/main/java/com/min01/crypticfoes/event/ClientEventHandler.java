package com.min01.crypticfoes.event;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.block.CrypticBlocks;
import com.min01.crypticfoes.block.model.HowlerHeadModel;
import com.min01.crypticfoes.block.model.ScreamerModel;
import com.min01.crypticfoes.blockentity.renderer.CrypticSkullRenderer;
import com.min01.crypticfoes.blockentity.renderer.ScreamerRenderer;
import com.min01.crypticfoes.entity.CrypticEntities;
import com.min01.crypticfoes.entity.model.BallModel;
import com.min01.crypticfoes.entity.model.BumpyModel;
import com.min01.crypticfoes.entity.model.HowlerModel;
import com.min01.crypticfoes.entity.renderer.BallRenderer;
import com.min01.crypticfoes.entity.renderer.BumpyRenderer;
import com.min01.crypticfoes.entity.renderer.HowlerRenderer;
import com.min01.crypticfoes.entity.renderer.HowlerScreamRenderer;
import com.min01.crypticfoes.entity.renderer.NoneRenderer;
import com.min01.crypticfoes.item.CrypticItems;
import com.min01.crypticfoes.item.MonstrousHornItem;
import com.min01.crypticfoes.item.model.RollingArmorBallModel;
import com.min01.crypticfoes.item.model.RollingArmorModel;
import com.min01.crypticfoes.misc.CrypticSkullTypes;
import com.min01.crypticfoes.particle.CrypticParticles;
import com.min01.crypticfoes.particle.DustPillarParticle;
import com.min01.crypticfoes.particle.HowlerShockwaveParticle;
import com.min01.crypticfoes.particle.SilencingParticle;
import com.min01.crypticfoes.particle.StunnedParticle;
import com.min01.crypticfoes.shader.CrypticShaders;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterEntitySpectatorShadersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = CrypticFoes.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler 
{
	@SubscribeEvent
	public static void onFMLClientSetup(FMLClientSetupEvent event)
	{
		event.enqueueWork(() ->
		{
	        BlockEntityRenderers.register(CrypticBlocks.CRYPTIC_SKULL_BLOCK_ENTITY.get(), CrypticSkullRenderer::new);
	        BlockEntityRenderers.register(CrypticBlocks.SCREAMER_BLOCK_ENTITY.get(), ScreamerRenderer::new);
	        ItemProperties.register(CrypticItems.MONSTROUS_HORN.get(), ResourceLocation.parse("charge"), (pStack, pLevel, pEntity, pSeed) ->
	        {
	        	if(pEntity != null && pEntity.isUsingItem())
	        	{
	        		return Mth.floor(MonstrousHornItem.getHornCharge(pStack) / 2) + 0.5F;
	        	}
	        	return Mth.floor(MonstrousHornItem.getHornCharge(pStack) / 2);
	        });
	        SkullBlockRenderer.SKIN_BY_TYPE.put(CrypticSkullTypes.HOWLER, ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "textures/entity/howler.png"));
		});
	}
	
	@SubscribeEvent
	public static void onRegisterEntitySpectatorShaders(RegisterEntitySpectatorShadersEvent event)
	{
		event.register(CrypticEntities.HOWLER.get(), ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "shaders/post/howler_dummy.json"));
	}
	
	@SubscribeEvent
	public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event)
	{
		event.register((stack, tintIndex) ->
		{
			if(tintIndex != 0)
			{
				return 0xFFFFFF;
			}
			if(stack.getItem() instanceof DyeableLeatherItem dyeable)
			{
				return dyeable.getColor(stack);
			}
			return 0xFFFFFF;
		}, CrypticItems.BALL.get());
	}
	
	@SubscribeEvent
	public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerEntityRenderer(CrypticEntities.HOWLER.get(), HowlerRenderer::new);
		event.registerEntityRenderer(CrypticEntities.HOWLER_SCREAM.get(), HowlerScreamRenderer::new);
		event.registerEntityRenderer(CrypticEntities.BALL.get(), BallRenderer::new);

		event.registerEntityRenderer(CrypticEntities.BUMPY.get(), BumpyRenderer::new);
		
		event.registerEntityRenderer(CrypticEntities.CAMERA_SHAKE.get(), NoneRenderer::new);
	}
	
	@SubscribeEvent
	public static void onCreateSkullModels(EntityRenderersEvent.CreateSkullModels event)
	{
		event.registerSkullModel(CrypticSkullTypes.HOWLER, new HowlerHeadModel(event.getEntityModelSet().bakeLayer(HowlerHeadModel.LAYER_LOCATION)));
	}
	
    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
    	event.registerLayerDefinition(HowlerModel.LAYER_LOCATION, HowlerModel::createBodyLayer);
    	event.registerLayerDefinition(HowlerHeadModel.LAYER_LOCATION, HowlerHeadModel::createHeadModel);
    	event.registerLayerDefinition(ScreamerModel.LAYER_LOCATION, ScreamerModel::createBodyLayer);

    	event.registerLayerDefinition(BumpyModel.LAYER_LOCATION, BumpyModel::createBodyLayer);
    	event.registerLayerDefinition(RollingArmorModel.LAYER_LOCATION, RollingArmorModel::createBodyLayer);
    	event.registerLayerDefinition(RollingArmorBallModel.LAYER_LOCATION, RollingArmorBallModel::createBodyLayer);
    	
    	event.registerLayerDefinition(BallModel.LAYER_LOCATION, BallModel::createBodyLayer);
    }
    
	@SubscribeEvent
	public static void onRegisterClientReloadListeners(RegisterClientReloadListenersEvent event)
	{
		event.registerReloadListener(new CrypticShaders());
	}
    
	@SubscribeEvent
	public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event)
	{
		event.registerSpriteSet(CrypticParticles.SILENCING.get(), SilencingParticle.Provider::new);
		event.registerSpriteSet(CrypticParticles.HOWLER_SHOCKWAVE.get(), HowlerShockwaveParticle.Provider::new);
		event.registerSpecial(CrypticParticles.DUST_PILLAR.get(), new DustPillarParticle.Provider());
		event.registerSpriteSet(CrypticParticles.STUNNED.get(), StunnedParticle.Provider::new);
	}
}
