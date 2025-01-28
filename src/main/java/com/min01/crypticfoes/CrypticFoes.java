package com.min01.crypticfoes;

import com.min01.crypticfoes.effect.CrypticEffects;
import com.min01.crypticfoes.entity.CrypticEntities;
import com.min01.crypticfoes.item.CrypticItems;
import com.min01.crypticfoes.misc.CrypticEntityDataSerializers;
import com.min01.crypticfoes.network.CrypticNetwork;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CrypticFoes.MODID)
public class CrypticFoes
{
	public static final String MODID = "crypticfoes";
	
	public CrypticFoes() 
	{
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		
		CrypticEntities.ENTITY_TYPES.register(bus);
		CrypticItems.ITEMS.register(bus);
		CrypticEntityDataSerializers.SERIALIZERS.register(bus);
		CrypticEffects.EFFECTS.register(bus);
		
		CrypticNetwork.registerMessages();
	}
}
