package com.min01.crypticfoes.item;

import com.min01.crypticfoes.sound.CrypticSounds;
import com.min01.crypticfoes.util.CrypticUtil;

import net.minecraft.core.BlockPos;
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
	public InteractionResult useOn(UseOnContext p_41427_)
	{
		Level level = p_41427_.getLevel();
		Player player = p_41427_.getPlayer();
		ItemStack stack = p_41427_.getItemInHand();
		BlockPos pos = p_41427_.getClickedPos();
		if(!CrypticUtil.isBlockSilenced(level, pos))
		{
			if(!player.getAbilities().instabuild)
			{
				stack.shrink(1);
			}
			player.playSound(CrypticSounds.SILENCING_BLEND_ON.get());
			CrypticUtil.setBlockSilence(level, pos);
			return InteractionResult.SUCCESS;
		}
		return super.useOn(p_41427_);
	}
}
