package com.min01.crypticfoes.item;

import java.util.function.Consumer;

import com.min01.crypticfoes.item.renderer.ScreamerItemRenderer;
import com.min01.crypticfoes.util.CrypticClientUtil;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class ScreamerBlockItem extends BlockItem
{
	public ScreamerBlockItem(Block pBlock, Item.Properties pProperties) 
	{
		super(pBlock, pProperties);
	}
	
	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) 
	{
		consumer.accept(new IClientItemExtensions()
		{
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() 
			{
				return new ScreamerItemRenderer(CrypticClientUtil.MC.getBlockEntityRenderDispatcher(), CrypticClientUtil.MC.getEntityModels());
			}
		});
	}
}
