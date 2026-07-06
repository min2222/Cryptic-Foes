package com.min01.crypticfoes.misc;

import java.util.ArrayList;
import java.util.List;

public class AnimationEntries 
{
	public final List<WalkAnimationEntry> walkEntries = new ArrayList<>();
	public final List<SmoothAnimationState> extraEntries = new ArrayList<>();

	public void addWalkEntry(SmoothAnimationState state, float scale)
	{
		this.walkEntries.add(new WalkAnimationEntry(state, scale));
	}
	
	public void addExtraEntry(SmoothAnimationState state)
	{
		this.extraEntries.add(state);
	}
	
	public static record WalkAnimationEntry(SmoothAnimationState state, float scale)
	{
		
	}
}
