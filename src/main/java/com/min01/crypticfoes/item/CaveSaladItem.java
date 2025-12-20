package com.min01.crypticfoes.item;

import com.min01.crypticfoes.entity.CrypticEntities;
import com.min01.crypticfoes.entity.projectile.EntityHowlerScream;
import com.min01.crypticfoes.misc.CrypticFoods;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;

public class CaveSaladItem extends Item
{
	public CaveSaladItem() 
	{
		super(new Item.Properties().food(CrypticFoods.CAVE_SALAD).stacksTo(1));
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity)
	{
		EntityHowlerScream scream = new EntityHowlerScream(CrypticEntities.HOWLER_SCREAM.get(), pLevel);
		scream.setOwner(pLivingEntity);
		scream.setPos(pLivingEntity.getEyePosition());
		scream.shootFromRotation(pLivingEntity, pLivingEntity.getXRot(), pLivingEntity.getYRot(), 0.0F, 0.75F, 1.0F);
		scream.setNoGravity(true);
		scream.setStunDuration(20);
		scream.setRange(0.06F - 0.0005F);
		pLevel.addFreshEntity(scream);
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
		if(ModList.get().isLoaded("farmersdelight"))
		{
			return 16;
		}
		return super.getMaxStackSize(stack);
	}
}
