package com.min01.crypticfoes.event;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.block.CrypticBlocks;
import com.min01.crypticfoes.block.model.ModelHowlerHead;
import com.min01.crypticfoes.block.model.ModelScreamer;
import com.min01.crypticfoes.blockentity.renderer.CrypticSkullRenderer;
import com.min01.crypticfoes.blockentity.renderer.ScreamerRenderer;
import com.min01.crypticfoes.entity.CrypticEntities;
import com.min01.crypticfoes.entity.model.ModelBrancher;
import com.min01.crypticfoes.entity.model.ModelHowler;
import com.min01.crypticfoes.entity.model.ModelPetrified;
import com.min01.crypticfoes.entity.model.ModelPetrifiedStone;
import com.min01.crypticfoes.entity.renderer.BrancherRenderer;
import com.min01.crypticfoes.entity.renderer.HowlerRenderer;
import com.min01.crypticfoes.entity.renderer.HowlerScreamRenderer;
import com.min01.crypticfoes.entity.renderer.NoneRenderer;
import com.min01.crypticfoes.entity.renderer.PetrifiedRenderer;
import com.min01.crypticfoes.entity.renderer.PetrifiedStoneRenderer;
import com.min01.crypticfoes.item.CrypticItems;
import com.min01.crypticfoes.item.MonstrousHornItem;
import com.min01.crypticfoes.misc.CrypticSkullTypes;
import com.min01.crypticfoes.particle.BrancherExplosionParticle;
import com.min01.crypticfoes.particle.BrancherExplosionSeedParticle;
import com.min01.crypticfoes.particle.CrypticParticles;
import com.min01.crypticfoes.particle.DustPillarParticle;
import com.min01.crypticfoes.particle.HowlerShockwaveParticle;
import com.min01.crypticfoes.particle.SilencingParticle;
import com.min01.crypticfoes.particle.StunnedParticle;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
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
        BlockEntityRenderers.register(CrypticBlocks.CRYPTIC_SKULL_BLOCK_ENTITY.get(), CrypticSkullRenderer::new);
        BlockEntityRenderers.register(CrypticBlocks.SCREAMER_BLOCK_ENTITY.get(), ScreamerRenderer::new);
        ItemProperties.register(CrypticItems.MONSTROUS_HORN.get(), ResourceLocation.parse("charge"), (p_174585_, p_174586_, p_174587_, p_174588_) ->
        {
        	if(p_174587_ != null && p_174587_.isUsingItem())
        	{
        		return Mth.floor(MonstrousHornItem.getHornCharge(p_174585_) / 2) + 0.5F;
        	}
        	return Mth.floor(MonstrousHornItem.getHornCharge(p_174585_) / 2);
        });
        SkullBlockRenderer.SKIN_BY_TYPE.put(CrypticSkullTypes.HOWLER, ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "textures/entity/howler.png"));
	}
	
	@SubscribeEvent
	public static void onRegisterEntitySpectatorShaders(RegisterEntitySpectatorShadersEvent event)
	{
		event.register(CrypticEntities.HOWLER.get(), ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "shaders/post/howler.json"));
	}
	
	@SubscribeEvent
	public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerEntityRenderer(CrypticEntities.PETRIFIED.get(), PetrifiedRenderer::new);
		event.registerEntityRenderer(CrypticEntities.PETRIFIED_STONE.get(), PetrifiedStoneRenderer::new);
		event.registerEntityRenderer(CrypticEntities.BRANCHER.get(), BrancherRenderer::new);
		event.registerEntityRenderer(CrypticEntities.HOWLER.get(), HowlerRenderer::new);
		event.registerEntityRenderer(CrypticEntities.HOWLER_SCREAM.get(), HowlerScreamRenderer::new);
		event.registerEntityRenderer(CrypticEntities.CAMERA_SHAKE.get(), NoneRenderer::new);
	}
	
	@SubscribeEvent
	public static void onCreateSkullModels(EntityRenderersEvent.CreateSkullModels event)
	{
		event.registerSkullModel(CrypticSkullTypes.HOWLER, new ModelHowlerHead(event.getEntityModelSet().bakeLayer(ModelHowlerHead.LAYER_LOCATION)));
	}
	
    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
    	event.registerLayerDefinition(ModelPetrified.LAYER_LOCATION, ModelPetrified::createBodyLayer);
    	event.registerLayerDefinition(ModelPetrifiedStone.LAYER_LOCATION, ModelPetrifiedStone::createBodyLayer);
    	event.registerLayerDefinition(ModelBrancher.LAYER_LOCATION, ModelBrancher::createBodyLayer);
    	event.registerLayerDefinition(ModelHowler.LAYER_LOCATION, ModelHowler::createBodyLayer);
    	event.registerLayerDefinition(ModelHowlerHead.LAYER_LOCATION, ModelHowlerHead::createHeadModel);
    	event.registerLayerDefinition(ModelScreamer.LAYER_LOCATION, ModelScreamer::createBodyLayer);
    }
    
	@SubscribeEvent
	public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event)
	{
		event.registerSpriteSet(CrypticParticles.BRANCHER_EXPLOSION.get(), BrancherExplosionParticle.Provider::new);
		event.registerSpecial(CrypticParticles.BRANCHER_EXPLOSION_SEED.get(), new BrancherExplosionSeedParticle.Provider());
		event.registerSpriteSet(CrypticParticles.SILENCING.get(), SilencingParticle.Provider::new);
		event.registerSpriteSet(CrypticParticles.HOWLER_SHOCKWAVE.get(), HowlerShockwaveParticle.Provider::new);
		event.registerSpecial(CrypticParticles.DUST_PILLAR.get(), new DustPillarParticle.Provider());
		event.registerSpriteSet(CrypticParticles.STUNNED.get(), StunnedParticle.Provider::new);
	}
}
