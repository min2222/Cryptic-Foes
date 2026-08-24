package com.min01.crypticfoes.item;

import com.min01.crypticfoes.config.CrypticConfig;
import com.min01.crypticfoes.misc.CrypticFoods;
import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class CaveSaladItem extends Item
{
	public CaveSaladItem() 
	{
		super(new Item.Properties().food(CrypticFoods.CAVE_SALAD).stacksTo(1));
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity)
	{
		ItemStack stack = super.finishUsingItem(pStack, pLevel, pLivingEntity);
		if(stack.getCount() > 1)
		{
			if(pLivingEntity instanceof Player player && !player.getAbilities().instabuild)
			{
				stack.shrink(1);
				player.getInventory().add(new ItemStack(Items.BOWL));
			}
			return stack;
		}
		return pLivingEntity instanceof Player player && player.getAbilities().instabuild ? stack : new ItemStack(Items.BOWL);
	}
	
	@Override
	public int getMaxStackSize(ItemStack stack) 
	{
		if(CrypticUtil.isModLoaded("farmersdelight") && CrypticConfig.enableFarmersDelightCompat.get())
		{
			return 16;
		}
		return super.getMaxStackSize(stack);
	}
}
