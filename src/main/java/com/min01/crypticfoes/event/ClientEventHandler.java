package com.min01.crypticfoes.event;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.entity.CrypticEntities;
import com.min01.crypticfoes.entity.model.ModelBrancher;
import com.min01.crypticfoes.entity.model.ModelPetrified;
import com.min01.crypticfoes.entity.model.ModelPetrifiedStone;
import com.min01.crypticfoes.entity.renderer.BrancherRenderer;
import com.min01.crypticfoes.entity.renderer.PetrifiedRenderer;
import com.min01.crypticfoes.entity.renderer.PetrifiedStoneRenderer;
import com.min01.crypticfoes.particle.BrancherExplosionParticle;
import com.min01.crypticfoes.particle.BrancherExplosionSeedParticle;
import com.min01.crypticfoes.particle.CrypticParticles;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
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
		
	}
	
	@SubscribeEvent
	public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerEntityRenderer(CrypticEntities.PETRIFIED.get(), PetrifiedRenderer::new);
		event.registerEntityRenderer(CrypticEntities.PETRIFIED_STONE.get(), PetrifiedStoneRenderer::new);
		event.registerEntityRenderer(CrypticEntities.BRANCHER.get(), BrancherRenderer::new);
	}
	
    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
    	event.registerLayerDefinition(ModelPetrified.LAYER_LOCATION, ModelPetrified::createBodyLayer);
    	event.registerLayerDefinition(ModelPetrifiedStone.LAYER_LOCATION, ModelPetrifiedStone::createBodyLayer);
    	event.registerLayerDefinition(ModelBrancher.LAYER_LOCATION, ModelBrancher::createBodyLayer);
    }
    
	@SubscribeEvent
	public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event)
	{
		event.registerSpriteSet(CrypticParticles.BRANCHER_EXPLOSION.get(), BrancherExplosionParticle.Provider::new);
		event.registerSpecial(CrypticParticles.BRANCHER_EXPLOSION_SEED.get(), new BrancherExplosionSeedParticle.Provider());
	}
}
