package com.min01.crypticfoes.misc;

import org.joml.Vector2f;

public class MovementData
{
	public float bodyTurnY = 75.0F;
	public final GroundData ground = new GroundData();
	public final FlyData fly = new FlyData();
	public final SwimData swim = new SwimData();
	
	public static class GroundData
	{
		public int interval = 60;
		public float turnY = 90.0F;
		public Vector2f radius = new Vector2f(10, 7);
	}
	
	public static class FlyData
	{
		public int interval = 60;
		public Vector2f turn = new Vector2f(85.0F, 10.0F);
		public Vector2f radius = new Vector2f(10, 7);
	}
	
	public static class SwimData
	{
		public int interval = 60;
		public Vector2f turn = new Vector2f(85.0F, 10.0F);
		public Vector2f radius = new Vector2f(10, 7);
	}
}
