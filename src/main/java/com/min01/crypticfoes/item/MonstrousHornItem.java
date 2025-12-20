package com.min01.crypticfoes.item;

import com.min01.crypticfoes.advancements.CrypticCriteriaTriggers;
import com.min01.crypticfoes.entity.CrypticEntities;
import com.min01.crypticfoes.entity.projectile.EntityHowlerScream;
import com.min01.crypticfoes.sound.CrypticSounds;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
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
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand)
	{
		ItemStack stack = pPlayer.getItemInHand(pUsedHand);
		boolean isScream = isScream(stack);
		if(!isScream)
		{
			pPlayer.startUsingItem(pUsedHand);
			return InteractionResultHolder.consume(stack);
		}
		return InteractionResultHolder.pass(stack);
	}
	
	@Override
	public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration)
	{
		if(pRemainingUseDuration % 10 == 0)
		{
			int charge = getHornCharge(pStack);
			if(charge < 6)
			{
				setHornCharge(pStack, charge + 1);
				setCurrentHornCharge(pStack, charge + 1);
				pLivingEntity.playSound(CrypticSounds.MONSTROUS_HORN_INHALE.get(), 1.0F, charge / 2.0F);
			}
		}
	}
	
	@Override
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) 
	{
		boolean isScream = isScream(pStack);
		if(pIsSelected)
		{
			if(isScream)
			{
				int stunCount = getStunCount(pStack);
				int charge = getHornCharge(pStack);
				int chargeTick = getHornChargeTick(pStack);
				int tick = getScreamTick(pStack);
				if(chargeTick < charge * 3)
				{
					setScreamTick(pStack, tick + 1);
					if(tick % 2 == 0)
					{
						EntityHowlerScream scream = new EntityHowlerScream(CrypticEntities.HOWLER_SCREAM.get(), pLevel);
						scream.setOwner(pEntity);
						scream.setPos(pEntity.getEyePosition());
						scream.shootFromRotation(pEntity, pEntity.getXRot(), pEntity.getYRot(), 0.0F, 0.5F + (charge * 0.25F), 1.0F);
						scream.setNoGravity(true);
						scream.setStunDuration(charge * 20);
						scream.setRange(0.06F - (charge * 0.0005F));
						pLevel.addFreshEntity(scream);
						setHornChargeTick(pStack, chargeTick + 1);
						if(Math.random() <= 0.5F)
						{
							pEntity.playSound(CrypticSounds.MONSTROUS_HORN_SCREAM.get());
						}
					}
					if(stunCount >= 10 && pEntity instanceof ServerPlayer serverPlayer)
					{
						CrypticCriteriaTriggers.STUNNING_SPEECH.trigger(serverPlayer);
					}
				}
				else
				{
					reset(pStack, pEntity);
				}
			}
		}
		else if(getHornCharge(pStack) > 0)
		{
			reset(pStack, pEntity);
		}
	}
	
	public static void reset(ItemStack stack, Entity entity)
	{
		setScream(stack, false);
		setScreamTick(stack, 0);
		setHornCharge(stack, 0);
		setCurrentHornCharge(stack, 0);
		setHornChargeTick(stack, 0);
		setStunCount(stack, 0);
		if(entity instanceof Player player)
		{
			player.getCooldowns().addCooldown(stack.getItem(), 140);
		}
	}

	@Override
	public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) 
	{
		int charge = getHornCharge(pStack);
		if(charge > 0)
		{
			setScream(pStack, true);
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
    
    public static int getStunCount(ItemStack stack)
    {
        CompoundTag tag = stack.getTag();
        return tag != null ? tag.getInt("StunCount") : 0;
    }

    public static void setStunCount(ItemStack stack, int tick)
    {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("StunCount", tick);
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
    
    public static int getHornChargeTick(ItemStack stack)
    {
        CompoundTag tag = stack.getTag();
        return tag != null ? tag.getInt("HornChargeTick") : 0;
    }

    public static void setHornChargeTick(ItemStack stack, int tick)
    {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("HornChargeTick", tick);
        if(tick % 3 == 0)
        {
        	setCurrentHornCharge(stack, getCurrentHornCharge(stack) - 1);
        }
    }
    
    public static int getCurrentHornCharge(ItemStack stack)
    {
        CompoundTag tag = stack.getTag();
        return tag != null ? tag.getInt("CurrentHornCharge") : 0;
    }

    public static void setCurrentHornCharge(ItemStack stack, int charge)
    {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("CurrentHornCharge", charge);
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
	
	@Override
	public UseAnim getUseAnimation(ItemStack pStack)
	{
		return UseAnim.TOOT_HORN;
	}
	
	@Override
	public int getUseDuration(ItemStack pStack)
	{
		return 72000;
	}
}