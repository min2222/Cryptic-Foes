package com.min01.crypticfoes.item;

import com.min01.crypticfoes.network.AddSilencingParticlePacket;
import com.min01.crypticfoes.network.CrypticNetwork;
import com.min01.crypticfoes.world.CrypticSavedData;

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
		if(!level.isClientSide)
		{
			Player player = p_41427_.getPlayer();
			ItemStack stack = p_41427_.getItemInHand();
			BlockPos pos = p_41427_.getClickedPos();
			CrypticSavedData data = CrypticSavedData.get(level);
			if(data != null && !data.isBlockSilenced(level, pos))
			{
				if(!player.getAbilities().instabuild)
				{
					stack.shrink(1);
				}
				data.setBlockSilence(pos);
				CrypticNetwork.sendToAll(new AddSilencingParticlePacket(pos));
				return InteractionResult.SUCCESS;
			}
		}
		return super.useOn(p_41427_);
	}
}
