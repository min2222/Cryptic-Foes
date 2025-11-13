package com.min01.crypticfoes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.min01.crypticfoes.item.CrypticItems;
import com.min01.crypticfoes.sound.CrypticSounds;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@Mixin(Player.class)
public class MixinPlayer
{
	@Inject(at = @At("HEAD"), method = "eat", cancellable = true)
	private void eat(Level level, ItemStack stack, CallbackInfoReturnable<ItemStack> cir)
	{
		Player player = Player.class.cast(this);
		if(stack.is(CrypticItems.CAVE_SALAD.get()))
		{
			cir.cancel();
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
