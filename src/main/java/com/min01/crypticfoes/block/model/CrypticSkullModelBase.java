package com.min01.crypticfoes.block.model;

import com.min01.crypticfoes.api.animation.IHierarchicalModel;
import com.min01.crypticfoes.blockentity.CrypticSkullBlockEntity;

import net.minecraft.client.model.SkullModelBase;

public abstract class CrypticSkullModelBase extends SkullModelBase implements IHierarchicalModel<CrypticSkullBlockEntity>
{
	public void setupAnim(CrypticSkullBlockEntity object, float pAgeInTicks) 
	{
		this.setupAnim(object, 0, 0, pAgeInTicks, 0, 0);
	}
}
