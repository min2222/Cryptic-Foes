package com.min01.crypticfoes.item;

import java.util.List;

import com.min01.crypticfoes.CrypticFoes;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;

public class RollingSmithingTemplateItem extends SmithingTemplateItem
{
    private static final ChatFormatting TITLE_FORMAT = ChatFormatting.GRAY;
    private static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.BLUE;
    private static final Component ROLLING_UPGRADE = Component.translatable(Util.makeDescriptionId("upgrade", ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "rolling_upgrade"))).withStyle(TITLE_FORMAT);
    private static final Component ROLLING_UPGRADE_APPLIES_TO = Component.translatable(Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "smithing_template.rolling_upgrade.applies_to"))).withStyle(DESCRIPTION_FORMAT);
    private static final Component ROLLING_UPGRADE_INGREDIENTS = Component.translatable(Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "smithing_template.rolling_upgrade.ingredients"))).withStyle(DESCRIPTION_FORMAT);
    private static final Component ROLLING_UPGRADE_BASE_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "smithing_template.rolling_upgrade.base_slot_description")));
    private static final Component ROLLING_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(CrypticFoes.MODID, "smithing_template.rolling_upgrade.additions_slot_description")));
    
	public static final ResourceLocation EMPTY_SLOT_CHESTPLATE = ResourceLocation.parse("item/empty_armor_slot_chestplate");
	public static final ResourceLocation EMPTY_SLOT_INGOT = ResourceLocation.parse("item/empty_slot_ingot");
	
	public RollingSmithingTemplateItem() 
	{
		super(ROLLING_UPGRADE_APPLIES_TO, ROLLING_UPGRADE_INGREDIENTS, ROLLING_UPGRADE, ROLLING_UPGRADE_BASE_SLOT_DESCRIPTION, ROLLING_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, List.of(EMPTY_SLOT_CHESTPLATE), List.of(EMPTY_SLOT_INGOT));
	}
}
