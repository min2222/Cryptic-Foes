package com.min01.crypticfoes.item;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.min01.crypticfoes.item.model.RollingArmorModel;
import com.min01.crypticfoes.misc.CrypticArmorMaterials;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class RollingChestplateItem extends ArmorItem
{
	public RollingChestplateItem(Properties pProperties)
	{
		super(CrypticArmorMaterials.ROLLING, ArmorItem.Type.CHESTPLATE, pProperties);
	}
	
	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) 
	{
		consumer.accept(new IClientItemExtensions() 
		{
			@Override
			public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) 
			{
				RollingArmorModel<?> armorModel = new RollingArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(RollingArmorModel.LAYER_LOCATION));
				armorModel.Body.visible = equipmentSlot == EquipmentSlot.CHEST;
				return armorModel;
			}
		});
	}
	
	@Override
	@Nullable
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type)
	{
		return "crypticfoes:textures/armor/rolling_armor.png";
	}
}
