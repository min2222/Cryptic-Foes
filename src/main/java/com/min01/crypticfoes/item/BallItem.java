package com.min01.crypticfoes.item;

import com.min01.crypticfoes.entity.CrypticEntities;
import com.min01.crypticfoes.entity.projectile.BallEntity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BallItem extends Item implements DyeableLeatherItem
{
	public BallItem()
	{
		super(new Item.Properties());
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand)
	{
		ItemStack stack = pPlayer.getItemInHand(pUsedHand);
		if(pPlayer.isShiftKeyDown())
		{
			BlockHitResult hitResult = getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.NONE);
			if(hitResult.getType() == HitResult.Type.BLOCK)
			{
				Vec3 location = hitResult.getLocation();
				BallEntity ball = new BallEntity(CrypticEntities.BALL.get(), pLevel);
				ball.setOwner(pPlayer);
				ball.setItem(stack.copyWithCount(1));
				ball.setPos(location.x, location.y + 0.2F, location.z);
				ball.setDeltaMovement(Vec3.ZERO);
				if(!pLevel.isClientSide)
				{
					pLevel.addFreshEntity(ball);
					if(!pPlayer.getAbilities().instabuild)
					{
						stack.shrink(1);
					}
				}
				return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide);
			}
		}
		else
		{
			BallEntity ball = new BallEntity(CrypticEntities.BALL.get(), pLevel);
			ball.setOwner(pPlayer);
			ball.setItem(stack.copyWithCount(1));
			ball.setPos(pPlayer.getX(), pPlayer.getEyeY() - 0.1F, pPlayer.getZ());
			ball.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.0F, 0.5F);
			if(!pLevel.isClientSide)
			{
				pLevel.addFreshEntity(ball);
				if(!pPlayer.getAbilities().instabuild)
				{
					stack.shrink(1);
				}
			}
			pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.5F, 0.8F + pLevel.random.nextFloat() * 0.4F);
			return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide);
		}
		return InteractionResultHolder.pass(stack);
	}
}
