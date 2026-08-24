package com.min01.crypticfoes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.min01.crypticfoes.entity.CrypticEntities;
import com.min01.crypticfoes.entity.projectile.HowlerScreamEntity;
import com.min01.crypticfoes.misc.CrypticTags;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@Mixin(Player.class)
public class MixinPlayer
{
	@ModifyReturnValue(method = "isStayingOnGroundSurface", at = @At("RETURN"))
	private boolean crypticfoes$isStayingOnGroundSurface(boolean original)
	{
		Player player = (Player) (Object) this;
		//TODO return false when rolling;
		return original;
	}
	
	@WrapMethod(method = "eat")
	private ItemStack crypticfoes$eat(Level pLevel, ItemStack pStack, Operation<ItemStack> original)
	{
		ItemStack stack = original.call(pLevel, pStack);
		Player player = (Player) (Object) this;
		if(pStack.is(CrypticTags.BURP_FOODS))
		{
			HowlerScreamEntity scream = new HowlerScreamEntity(CrypticEntities.HOWLER_SCREAM.get(), pLevel);
			scream.setOwner(player);
			scream.setPos(player.getEyePosition());
			scream.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0.75F, 1.0F);
			scream.setNoGravity(true);
			scream.setStunDuration(20);
			scream.setRange(0.06F - 0.0005F);
			pLevel.addFreshEntity(scream);
			return pStack;
		}
		return stack;
	}
}
