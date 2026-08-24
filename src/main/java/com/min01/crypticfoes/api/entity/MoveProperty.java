package com.min01.crypticfoes.api.entity;

import org.joml.Vector2f;
import org.joml.Vector2i;

public class MoveProperty
{
	protected final Property body;
	protected final Property walk;
	protected final Property fly;
	protected final Property swim;
	
	public MoveProperty(Property body, Property walk, Property fly, Property swim)
	{
		this.body = body;
		this.walk = walk;
		this.fly = fly;
		this.swim = swim;
	}
	
	public int bodyTurnY()
	{
		return this.body.bodyTurnY;
	}
	
	public void bodyTurnY(int turnY)
	{
		this.body.bodyTurnY(turnY);
	}
	
	public void interval(MoveState state, int interval)
	{
		switch(state)
		{
		case WALK:
			this.walk.interval(interval);
			break;
		case SWIM:
			this.swim.interval(interval);
			break;
		case FLY:
			this.fly.interval(interval);
			break;
		}
	}
	
	public int interval(MoveState state)
	{
		switch(state)
		{
		case WALK:
			return this.walk.interval;
		case SWIM:
			return this.swim.interval;
		case FLY:
			return this.fly.interval;
		}
		return this.walk.interval;
	}
	
	public Vector2f turn(MoveState state)
	{
		switch(state)
		{
		case WALK:
			return this.walk.turn;
		case SWIM:
			return this.swim.turn;
		case FLY:
			return this.fly.turn;
		}
		return this.walk.turn;
	}
	
	public void turn(MoveState state, float turnX, float turnY)
	{
		switch(state)
		{
		case WALK:
			this.walk.turn(turnX, turnY);
			break;
		case SWIM:
			this.swim.turn(turnX, turnY);
			break;
		case FLY:
			this.fly.turn(turnX, turnY);
			break;
		}
	}
	
	public void radius(MoveState state, int radiusX, int radiusY)
	{
		switch(state)
		{
		case WALK:
			this.walk.radius(radiusX, radiusY);
			break;
		case SWIM:
			this.swim.radius(radiusX, radiusY);
			break;
		case FLY:
			this.fly.radius(radiusX, radiusY);
			break;
		}
	}
	
	public Vector2i radius(MoveState state)
	{
		switch(state)
		{
		case WALK:
			return this.walk.radius;
		case SWIM:
			return this.swim.radius;
		case FLY:
			return this.fly.radius;
		}
		return this.walk.radius;
	}
	
	public static class Property
	{
		protected int bodyTurnY = 75;
		protected int interval = 120;
		protected Vector2f turn = new Vector2f(85.0F, 10.0F);
		protected Vector2i radius = new Vector2i(10, 7);
		
		public void bodyTurnY(int bodyTurnY)
		{
			this.bodyTurnY = bodyTurnY;
		}
		
		public void interval(int interval)
		{
			this.interval = interval;
		}
		
		public void turn(float turnX, float turnY)
		{
			this.turn.set(turnX, turnY);
		}
		
		public void radius(int radiusX, int radiusY)
		{
			this.radius.set(radiusX, radiusY);
		}
	}
	
	public static class Builder
	{
		private final Property body = new Property();
		private final Property walk = new Property();
		private final Property fly = new Property();
		private final Property swim = new Property();
		
		public Builder() 
		{
			//MoveControl (default value is 90)
			this.walk.turn(0.0F, 90.0F);
		}
		
		public Builder bodyTurnY(int turnY)
		{
			this.body.bodyTurnY(turnY);
			return this;
		}
		
		public Builder interval(MoveState state, int interval)
		{
			switch(state)
			{
			case WALK:
				this.walk.interval(interval);
				break;
			case SWIM:
				this.swim.interval(interval);
				break;
			case FLY:
				this.fly.interval(interval);
				break;
			}
			return this;
		}
		
		public Builder turn(MoveState state, float turnX, float turnY)
		{
			switch(state)
			{
			case WALK:
				this.walk.turn(turnX, turnY);
				break;
			case SWIM:
				this.swim.turn(turnX, turnY);
				break;
			case FLY:
				this.fly.turn(turnX, turnY);
				break;
			}
			return this;
		}
		
		public Builder radius(MoveState state, int radiusX, int radiusY)
		{
			switch(state)
			{
			case WALK:
				this.walk.radius(radiusX, radiusY);
				break;
			case SWIM:
				this.swim.radius(radiusX, radiusY);
				break;
			case FLY:
				this.fly.radius(radiusX, radiusY);
				break;
			}
			return this;
		}
		
		public MoveProperty build()
		{
			return new MoveProperty(this.body, this.walk, this.fly, this.swim);
		}
	}
	
	public static enum MoveState
	{
		WALK,
		SWIM,
		FLY
	}
}
