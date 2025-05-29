package com.min01.crypticfoes.misc;

import com.min01.crypticfoes.CrypticFoes;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CrypticPaintings 
{
    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS = DeferredRegister.create(Registries.PAINTING_VARIANT, CrypticFoes.MODID);
    
    public static final RegistryObject<PaintingVariant> MOUNTAIN_HOLDER = PAINTING_VARIANTS.register("mountain_holder", () -> new PaintingVariant(64, 48));
}
