package com.min01.crypticfoes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.min01.crypticfoes.capabilties.RollingCapabilityImpl;
import com.min01.crypticfoes.entity.CrypticEntities;
import com.min01.crypticfoes.entity.projectile.HowlerScreamEntity;
import com.min01.crypticfoes.misc.CrypticTags;
import com.min01.crypticfoes.sound.CrypticSounds;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@Mixin(value = Player.class, priority = -10000)
public class MixinPlayer
{
	@Inject(at = @At("HEAD"), method = "isStayingOnGroundSurface", cancellable = true)
	private void crypticfoes$rollingAllowWalkOffEdge(CallbackInfoReturnable<Boolean> cir)
	{
		Player player = Player.class.cast(this);
		player.getCapability(RollingCapabilityImpl.ROLLING).ifPresent(cap ->
		{
			if(cap.isRolling())
			{
				cir.setReturnValue(false);
			}
		});
	}
	
	@Inject(at = @At("HEAD"), method = "eat", cancellable = true)
	private void eat(Level level, ItemStack stack, CallbackInfoReturnable<ItemStack> cir)
	{
		Player player = Player.class.cast(this);
		if(stack.is(CrypticTags.BURP_FOODS))
		{
			cir.cancel();

			HowlerScreamEntity scream = new HowlerScreamEntity(CrypticEntities.HOWLER_SCREAM.get(), level);
			scream.setOwner(player);
			scream.setPos(player.getEyePosition());
			scream.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0.75F, 1.0F);
			scream.setNoGravity(true);
			scream.setStunDuration(20);
			scream.setRange(0.06F - 0.0005F);
			level.addFreshEntity(scream);
			
			player.getFoodData().eat(stack.getItem(), stack, player);
			player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
			level.playSound(null, player.getX(), player.getY(), player.getZ(), CrypticSounds.CAVE_SALAD_BURP.get(), SoundSource.PLAYERS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
			if(player instanceof ServerPlayer serverPlayer) 
			{
				CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
			}
			cir.setReturnValue(stack);
		}
	}
}
