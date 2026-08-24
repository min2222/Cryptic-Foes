package com.min01.crypticfoes.api.animation;

import java.util.ArrayList;
import java.util.List;

public class AnimationEntries 
{
	public final List<WalkAnimationEntry> walkEntries = new ArrayList<>();
	public final List<LerpingAnimationState> extraEntries = new ArrayList<>();

	public void addWalkEntry(LerpingAnimationState state, float scale)
	{
		this.walkEntries.add(new WalkAnimationEntry(state, scale));
	}
	
	public void addExtraEntry(LerpingAnimationState state)
	{
		this.extraEntries.add(state);
	}
	
	public static record WalkAnimationEntry(LerpingAnimationState state, float scale)
	{
		
	}
}
