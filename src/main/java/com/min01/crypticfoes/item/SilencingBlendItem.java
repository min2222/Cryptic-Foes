package com.min01.crypticfoes.item;

import com.min01.crypticfoes.advancements.CrypticCriteriaTriggers;
import com.min01.crypticfoes.sound.CrypticSounds;
import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class SilencingBlendItem extends Item
{
	public SilencingBlendItem() 
	{
		super(new Item.Properties());
	}
	
	@Override
	public InteractionResult useOn(UseOnContext pContext)
	{
		Level level = pContext.getLevel();
		Player player = pContext.getPlayer();
		ItemStack stack = pContext.getItemInHand();
		BlockPos pos = pContext.getClickedPos();
		if(!CrypticUtil.isBlockSilenced(level, pos))
		{
			if(!player.getAbilities().instabuild)
			{
				stack.shrink(1);
			}
			player.playSound(CrypticSounds.SILENCING_BLEND_ON.get());
			CrypticUtil.setBlockSilence(level, pos);
			if(player instanceof ServerPlayer serverPlayer)
			{
				CrypticCriteriaTriggers.SILENCING_BLEND.trigger(serverPlayer);
			}
			return InteractionResult.SUCCESS;
		}
		return super.useOn(pContext);
	}
}
