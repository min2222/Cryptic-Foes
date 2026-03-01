package com.min01.crypticfoes.sound;

import com.min01.crypticfoes.CrypticFoes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CrypticSounds 
{
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CrypticFoes.MODID);

	public static final RegistryObject<SoundEvent> HOWLER_IDLE = registerSound("howler_idle");
	public static final RegistryObject<SoundEvent> HOWLER_HURT = registerSound("howler_hurt");
	public static final RegistryObject<SoundEvent> HOWLER_DEATH = registerSound("howler_death");
	public static final RegistryObject<SoundEvent> HOWLER_SCREAM = registerSound("howler_scream");
	public static final RegistryObject<SoundEvent> HOWLER_LAND = registerSound("howler_land");
	public static final RegistryObject<SoundEvent> HOWLER_SLEEP = registerSound("howler_sleep");
	public static final RegistryObject<SoundEvent> HOWLER_PUNCH = registerSound("howler_punch");
	public static final RegistryObject<SoundEvent> HOWLER_FLY = registerSound("howler_fly");
	public static final RegistryObject<SoundEvent> HOWLER_FLY_START = registerSound("howler_fly_start");
	public static final RegistryObject<SoundEvent> HOWLER_FLY_END = registerSound("howler_fly_end");
	public static final RegistryObject<SoundEvent> CAVE_SALAD_BURP = registerSound("cave_salad_burp");
	public static final RegistryObject<SoundEvent> MONSTROUS_HORN_INHALE = registerSound("monstrous_horn_inhale");
	public static final RegistryObject<SoundEvent> MONSTROUS_HORN_SCREAM = registerSound("monstrous_horn_scream");
	public static final RegistryObject<SoundEvent> SCREAMER_WORK = registerSound("screamer_work");
	public static final RegistryObject<SoundEvent> SCREAMER_SWITCH = registerSound("screamer_switch");
	public static final RegistryObject<SoundEvent> SILENCING_BLEND_OFF = registerSound("silencing_blend_off");
	public static final RegistryObject<SoundEvent> SILENCING_BLEND_ON = registerSound("silencing_blend_on");
	
	public static RegistryObject<SoundEvent> registerSound(String name) 
	{
		return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, name)));
    }
}
