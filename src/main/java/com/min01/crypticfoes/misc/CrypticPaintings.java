package com.min01.crypticfoes.misc;

import com.min01.crypticfoes.CrypticFoes;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CrypticPaintings 
{
    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS = DeferredRegister.create(Registries.PAINTING_VARIANT, CrypticFoes.MODID);

    public static final RegistryObject<PaintingVariant> MOUNTAIN_HOLDER = PAINTING_VARIANTS.register("mountain_holder", () -> new PaintingVariant(64, 48));
    public static final RegistryObject<PaintingVariant> DWELLERS = PAINTING_VARIANTS.register("dwellers", () -> new PaintingVariant(32, 32));
    public static final RegistryObject<PaintingVariant> RESIN = PAINTING_VARIANTS.register("resin", () -> new PaintingVariant(16, 32));
    
    public static final ResourceKey<PaintingVariant> MOUNTAIN_HOLDER_KEY = ResourceKey.create(Registries.PAINTING_VARIANT, ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "mountain_holder"));
    public static final ResourceKey<PaintingVariant> DWELLERS_KEY = ResourceKey.create(Registries.PAINTING_VARIANT, ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "dwellers"));
    public static final ResourceKey<PaintingVariant> RESIN_KEY = ResourceKey.create(Registries.PAINTING_VARIANT, ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "resin"));
}
