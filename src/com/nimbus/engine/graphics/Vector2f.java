package com.nimbus.engine.graphics;

public class Vector2f
{
	private float x; 
	private float y;
	
	public Vector2f(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2f add(Vector2f vector)
	{
		return new Vector2f(x + vector.getX(), y + vector.getY());
	}
	
	public Vector2f subtract(Vector2f vector)
	{
		return new Vector2f(x - vector.getX(), y - vector.getY());
	}
	
	public Vector2f multiply(Vector2f vector)
	{
		return new Vector2f(x * vector.getX(), y * vector.getY());
	}

	public Vector2f divide(Vector2f vector)
	{
		return new Vector2f(x / vector.getX(), y / vector.getY());
	}

	public float getLength()
	{
		return (float) Math.sqrt(x * x + y * y);
	}
	
	public Vector2f normalize()
	{
		float length = this.getLength();
		return new Vector2f(x / length, y / length);
	}
		
	public Vector2f reflect()
	{
		return new Vector2f(-x, -y);
	}
		
	public boolean isEqual(Vector2f vector)
	{
		return x == vector.getX() && y == vector.getY();
	}
	
	public float getX()
	{
		return x;
	}

	public void setX(float x)
	{
		this.x = x;
	}

	public float getY()
	{
		return y;
	}

	public void setY(float y)
	{
		this.y = y;
	}

}