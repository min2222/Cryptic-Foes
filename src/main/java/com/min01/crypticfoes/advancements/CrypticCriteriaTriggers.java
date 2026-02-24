package com.min01.crypticfoes.advancements;

import com.min01.crypticfoes.advancements.criterion.ItemUsedOnSilencedLocationTrigger;
import com.min01.crypticfoes.advancements.criterion.SilencingBlendTrigger;
import com.min01.crypticfoes.advancements.criterion.StunningSpeechTrigger;

import net.minecraft.advancements.CriteriaTriggers;

public class CrypticCriteriaTriggers
{
	public static final ItemUsedOnSilencedLocationTrigger ITEM_USED_ON_SILENCED_BLOCK = new ItemUsedOnSilencedLocationTrigger();
	public static final StunningSpeechTrigger STUNNING_SPEECH = new StunningSpeechTrigger();
	public static final SilencingBlendTrigger SILENCING_BLEND = new SilencingBlendTrigger();
	
	public static void init()
	{
		CriteriaTriggers.register(CrypticCriteriaTriggers.ITEM_USED_ON_SILENCED_BLOCK);
		CriteriaTriggers.register(CrypticCriteriaTriggers.STUNNING_SPEECH);
		CriteriaTriggers.register(CrypticCriteriaTriggers.SILENCING_BLEND);
	}
}
