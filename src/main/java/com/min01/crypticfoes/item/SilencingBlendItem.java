package com.min01.crypticfoes.item;

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
	public InteractionResult useOn(UseOnContext pContext)
	{
		Level level = pContext.getLevel();
		Player player = pContext.getPlayer();
		ItemStack stack = pContext.getItemInHand();
		BlockPos pos = pContext.getClickedPos();
		//TODO silencing block;
		return super.useOn(pContext);
	}
}
