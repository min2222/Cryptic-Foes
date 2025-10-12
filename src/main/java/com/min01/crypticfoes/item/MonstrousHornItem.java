package com.min01.crypticfoes.item;

import com.min01.crypticfoes.entity.CrypticEntities;
import com.min01.crypticfoes.entity.projectile.EntityHowlerScream;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class MonstrousHornItem extends Item
{
	public MonstrousHornItem()
	{
		super(new Item.Properties().stacksTo(1));
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_)
	{
		ItemStack stack = p_41433_.getItemInHand(p_41434_);
		p_41433_.startUsingItem(p_41434_);
		return InteractionResultHolder.consume(stack);
	}
	
	@Override
	public void onUseTick(Level p_41428_, LivingEntity p_41429_, ItemStack p_41430_, int p_41431_)
	{
		if(p_41431_ % 20 == 0)
		{
			int charge = getHornCharge(p_41430_);
			if(charge < 6)
			{
				setHornCharge(p_41430_, charge + 1);
				setScreamTick(p_41430_, charge * 30);
				p_41429_.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP);
			}
		}
	}
	
	@Override
	public void releaseUsing(ItemStack p_41412_, Level p_41413_, LivingEntity p_41414_, int p_41415_) 
	{
		int charge = getHornCharge(p_41412_);
		if(charge > 0)
		{
			setScream(p_41412_, true);
		}
	}
	
	@Override
	public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) 
	{
		boolean isScream = isScream(p_41404_);
		if(p_41408_)
		{
			if(isScream)
			{
				int tick = getScreamTick(p_41404_);
				if(tick > 0)
				{
					setScreamTick(p_41404_, tick - 1);
					if(tick % 20 == 0)
					{
						int charge = getHornCharge(p_41404_);
						EntityHowlerScream scream = new EntityHowlerScream(CrypticEntities.HOWLER_SCREAM.get(), p_41405_);
						scream.setOwner(p_41406_);
						scream.setPos(p_41406_.getEyePosition());
						scream.shootFromRotation(p_41406_, p_41406_.getXRot(), p_41406_.getYRot(), 0.0F, 0.5F + (charge * 0.25F), 1.0F);
						scream.setNoGravity(true);
						scream.setStunDuration(charge * 20);
						scream.setRange(0.03F - (charge * 0.0035F));
						p_41405_.addFreshEntity(scream);
					}
				}
				else
				{
					setScream(p_41404_, false);
					setHornCharge(p_41404_, 0);
					setScreamTick(p_41404_, 0);
					if(p_41406_ instanceof Player player)
					{
						player.getCooldowns().addCooldown(p_41404_.getItem(), 140);
					}
				}
			}
		}
		else if(isScream)
		{
			setScream(p_41404_, false);
			setHornCharge(p_41404_, 0);
			setScreamTick(p_41404_, 0);
			if(p_41406_ instanceof Player player)
			{
				player.getCooldowns().addCooldown(p_41404_.getItem(), 140);
			}
		}
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) 
	{
		return newStack.getItem() != this;
	}
	
    public static boolean isScream(ItemStack stack)
    {
        CompoundTag tag = stack.getTag();
        return tag != null ? tag.getBoolean("isScream") : false;
    }

    public static void setScream(ItemStack stack, boolean scream)
    {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putBoolean("isScream", scream);
    }
	
    public static int getHornCharge(ItemStack stack)
    {
        CompoundTag tag = stack.getTag();
        return tag != null ? tag.getInt("HornCharge") : 0;
    }

    public static void setHornCharge(ItemStack stack, int charge)
    {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("HornCharge", charge);
    }
    
    public static int getScreamTick(ItemStack stack)
    {
        CompoundTag tag = stack.getTag();
        return tag != null ? tag.getInt("ScreamTick") : 0;
    }

    public static void setScreamTick(ItemStack stack, int tick)
    {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("ScreamTick", tick);
    }
	
	@Override
	public UseAnim getUseAnimation(ItemStack p_41452_)
	{
		return UseAnim.TOOT_HORN;
	}
	
	@Override
	public int getUseDuration(ItemStack p_41454_)
	{
		return 72000;
	}
}