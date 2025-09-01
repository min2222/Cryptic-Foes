package com.min01.crypticfoes.misc;

import com.min01.crypticfoes.CrypticFoes;
import com.min01.crypticfoes.item.CrypticItems;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CrypticCreativeTabs 
{
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CrypticFoes.MODID);

    public static final RegistryObject<CreativeModeTab> CRYPTIC_FOES = CREATIVE_MODE_TAB.register("crypticfoes", () -> CreativeModeTab.builder()
    		.title(Component.translatable("itemGroup.crypticfoes"))
    		.icon(() -> new ItemStack(CrypticItems.FRAGILE_BONE.get()))
    		.displayItems((enabledFeatures, output) -> 
    		{
    			output.accept(CrypticItems.PETRIFIED_SPAWN_EGG.get());
    			output.accept(CrypticItems.BRANCHER_SPAWN_EGG.get());
    			output.accept(CrypticItems.HOWLER_SPAWN_EGG.get());
    			output.accept(CrypticItems.FRAGILE_BONE.get());
    			output.accept(CrypticItems.UNSTABLE_RESIN.get());
    			output.accept(CrypticItems.HOWLER_MEMBRANE.get());
    			output.accept(CrypticItems.MONSTROUS_HORN.get());
    			output.accept(CrypticItems.SILENCING_BLEND.get());
    			output.accept(CrypticItems.FALLEN_LEAVES.get());
    			output.accept(CrypticItems.BLOCK_OF_FRAGILE_BONES.get());
    			output.accept(CrypticItems.PILE_OF_FRAGILE_BONES.get());
    			output.accept(CrypticItems.POLISHED_PILE_OF_FRAGILE_BONES.get());
    			output.accept(CrypticItems.WAXED_PILE_OF_FRAGILE_BONES.get());
    			output.accept(CrypticItems.WAXED_POLISHED_PILE_OF_FRAGILE_BONES.get());
    			output.accept(CrypticItems.PILE_OF_FRAGILE_BONES_SLAB.get());
    			output.accept(CrypticItems.POLISHED_PILE_OF_FRAGILE_BONES_SLAB.get());
    		}).build());
}
