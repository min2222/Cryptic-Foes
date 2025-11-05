package com.min01.crypticfoes.misc;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.item.CrypticItems;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CrypticCreativeTabs 
{
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CrypticFoes.MODID);

    public static final RegistryObject<CreativeModeTab> CRYPTIC_FOES = CREATIVE_MODE_TAB.register("crypticfoes", () -> CreativeModeTab.builder()
    		.title(Component.translatable("itemGroup.crypticfoes"))
    		.icon(() -> new ItemStack(CrypticItems.HOWLER_MEMBRANE.get()))
    		.displayItems((enabledFeatures, output) -> 
    		{
    			output.accept(CrypticItems.HOWLER_SPAWN_EGG.get());
    			output.accept(CrypticItems.HOWLER_MEMBRANE.get());
    			output.accept(CrypticItems.SILENCING_BLEND.get());
    			output.accept(CrypticItems.CAVE_SALAD.get());
    			output.accept(CrypticItems.MONSTROUS_HORN.get());
    			output.accept(CrypticItems.SCREAMER.get());
    			output.accept(CrypticItems.HOWLER_HEAD.get());
    			output.accept(createPainting(enabledFeatures, CrypticPaintings.RESIN_KEY));
    			output.accept(createPainting(enabledFeatures, CrypticPaintings.DWELLERS_KEY));
    			output.accept(createPainting(enabledFeatures, CrypticPaintings.MOUNTAIN_HOLDER_KEY));
    		}).build());
    
    public static ItemStack createPainting(CreativeModeTab.ItemDisplayParameters enabledFeatures, ResourceKey<PaintingVariant> key)
    {
        ItemStack stack = new ItemStack(Items.PAINTING);
        CompoundTag tag = stack.getOrCreateTagElement("EntityTag");
        Holder.Reference<PaintingVariant> holder = enabledFeatures.holders().lookupOrThrow(Registries.PAINTING_VARIANT).getOrThrow(key);
        Painting.storeVariant(tag, holder);
        return stack;
    }
}
