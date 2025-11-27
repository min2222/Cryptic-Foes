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
	public ItemStack finishUsingItem(ItemStack p_41409_, Level p_41410_, LivingEntity p_41411_)
	{
		EntityHowlerScream scream = new EntityHowlerScream(CrypticEntities.HOWLER_SCREAM.get(), p_41410_);
		scream.setOwner(p_41411_);
		scream.setPos(p_41411_.getEyePosition());
		scream.shootFromRotation(p_41411_, p_41411_.getXRot(), p_41411_.getYRot(), 0.0F, 0.75F, 1.0F);
		scream.setNoGravity(true);
		scream.setStunDuration(20);
		scream.setRange(0.06F - 0.0005F);
		p_41410_.addFreshEntity(scream);
		ItemStack stack = super.finishUsingItem(p_41409_, p_41410_, p_41411_);
		if(stack.getCount() > 1)
		{
			if(p_41411_ instanceof Player player && !player.getAbilities().instabuild)
			{
				stack.shrink(1);
				player.getInventory().add(new ItemStack(Items.BOWL));
			}
			return stack;
		}
		return p_41411_ instanceof Player player && player.getAbilities().instabuild ? stack : new ItemStack(Items.BOWL);
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
