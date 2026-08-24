package com.min01.crypticfoes;

import com.min01.crypticfoes.block.CrypticBlocks;
import com.min01.crypticfoes.capabilties.CrypticCapabilities;
import com.min01.crypticfoes.config.CrypticConfig;
import com.min01.crypticfoes.effect.CrypticEffects;
import com.min01.crypticfoes.enchantment.CrypticEnchantments;
import com.min01.crypticfoes.entity.CrypticEntities;
import com.min01.crypticfoes.item.CrypticItems;
import com.min01.crypticfoes.misc.CrypticCreativeTabs;
import com.min01.crypticfoes.misc.CrypticEntityDataSerializers;
import com.min01.crypticfoes.misc.CrypticPaintings;
import com.min01.crypticfoes.network.CrypticNetwork;
import com.min01.crypticfoes.particle.CrypticParticles;
import com.min01.crypticfoes.sound.CrypticSounds;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CrypticFoes.MODID)
public class CrypticFoes
{
	public static final String MODID = "crypticfoes";
	
	public CrypticFoes(FMLJavaModLoadingContext ctx) 
	{
		IEventBus bus = ctx.getModEventBus();
		
		CrypticEntities.ENTITY_TYPES.register(bus);
		CrypticItems.ITEMS.register(bus);
		CrypticEntityDataSerializers.SERIALIZERS.register(bus);
		CrypticEffects.MOB_EFFECTS.register(bus);
		CrypticEffects.POTIONS.register(bus);
		CrypticBlocks.BLOCKS.register(bus);
		CrypticBlocks.BLOCK_ENTITIES.register(bus);
		CrypticCreativeTabs.CREATIVE_MODE_TAB.register(bus);
		CrypticParticles.PARTICLES.register(bus);
		CrypticSounds.SOUNDS.register(bus);
		CrypticPaintings.PAINTING_VARIANTS.register(bus);
		CrypticEnchantments.ENCHANTMENTS.register(bus);
		
		CrypticNetwork.registerMessages();
		ctx.registerConfig(Type.COMMON, CrypticConfig.CONFIG_SPEC, "crypticfoes.toml");
		MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, CrypticCapabilities::onAttachEntityCapabilities);
	}
}
