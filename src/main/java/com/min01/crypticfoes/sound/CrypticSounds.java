package com.min01.crypticfoes.sound;

import com.min01.crypticfoes.CrypticFoes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CrypticSounds 
{
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CrypticFoes.MODID);

	public static final RegistryObject<SoundEvent> BRANCHER_ANGRY = registerSound("brancher_angry");
	public static final RegistryObject<SoundEvent> BRANCHER_EXPLOSION = registerSound("brancher_explosion");
	public static final RegistryObject<SoundEvent> BRANCHER_HEARTBEAT = registerSound("brancher_heartbeat");
	public static final RegistryObject<SoundEvent> BRANCHER_HISS = registerSound("brancher_hiss");
	public static final RegistryObject<SoundEvent> BRANCHER_DEATH = registerSound("brancher_death");
	public static final RegistryObject<SoundEvent> BRANCHER_HURT = registerSound("brancher_hurt");
	public static final RegistryObject<SoundEvent> FALLEN_LEAVES_BREAK = registerSound("fallen_leaves_break");
	public static final RegistryObject<SoundEvent> FALLEN_LEAVES_PLACE = registerSound("fallen_leaves_place");
	public static final RegistryObject<SoundEvent> FALLEN_LEAVES_STEP = registerSound("fallen_leaves_step");
	public static final RegistryObject<SoundEvent> HOWLER_IDLE = registerSound("howler_idle");
	public static final RegistryObject<SoundEvent> HOWLER_SCREAM = registerSound("howler_scream");
	public static final RegistryObject<SoundEvent> HOWLER_LAND = registerSound("howler_land");
	public static final RegistryObject<SoundEvent> CAVE_SALAD_BURP = registerSound("cave_salad_burp");
	public static final RegistryObject<SoundEvent> MONSTROUS_HORN_INHALE = registerSound("monstrous_horn_inhale");
	public static final RegistryObject<SoundEvent> MONSTROUS_HORN_SCREAM = registerSound("monstrous_horn_scream");
	public static final RegistryObject<SoundEvent> SCREAMER_WORK = registerSound("screamer_work");
	public static final RegistryObject<SoundEvent> SCREAMER_SWITCH = registerSound("screamer_switch");
	public static final RegistryObject<SoundEvent> SILENCING_BLEND_OFF = registerSound("silencing_blend_off");
	public static final RegistryObject<SoundEvent> SILENCING_BLEND_ON = registerSound("silencing_blend_on");
	
	public static final SoundType FALLEN_LEAVES = new ForgeSoundType(1.0F, 1.0F, () -> FALLEN_LEAVES_BREAK.get(), () -> FALLEN_LEAVES_STEP.get(), () -> FALLEN_LEAVES_PLACE.get(), () -> SoundEvents.GRASS_HIT, () -> SoundEvents.GRASS_FALL);
	
	public static RegistryObject<SoundEvent> registerFixedSound(String name, float range) 
	{
		return SOUNDS.register(name, () -> SoundEvent.createFixedRangeEvent(ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, name), 16.0F * range));
    }
	
	public static RegistryObject<SoundEvent> registerSound(String name) 
	{
		return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, name)));
    }
}
