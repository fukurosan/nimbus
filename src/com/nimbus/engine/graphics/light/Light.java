package com.nimbus.engine.graphics.light;

public class Light {

	public static final int IGNORE = -1; // Ignore lighting but keep ambiance
	public static final int BLOCKED = 0; //No light can penetrate
	public static final int ABSORBED = 1; //Absorbs light, but does not let it through
	public static final int SELF_ILLUMINATED = 2; //Not affected by lighting / ambiance
	public static final int FULL = 3; //Fully affected by light (usually default)
	
	private int radius;
	private int diameter;
	private int colour;
	private int[] lightMap;
	
	public Light(int radius, int colour) {
		this.radius = radius;
		this.diameter = radius * 2;
		this.colour = colour;
		lightMap = new int[diameter * diameter];
		
		for(int y = 0; y < diameter; y++) {
			for(int x = 0; x < diameter; x++) {
				double distance = Math.sqrt((x - radius) * (x - radius) + (y - radius) * (y - radius));
				if(distance < radius) {
					double power = 1 - (distance / radius);
					lightMap[x + y * diameter] = applyLightPower(colour, (float) power);
				}
				else {
					lightMap[x + y * diameter] = 0;
				}
			}
		}
	}
	
	public int getLightValue(int x, int y) {
		if(x < 0 || x >= diameter || y < 0 || y >= diameter) {
			return 0;
		}
		return lightMap[x + y * diameter];
	}
	
	public static int applyLightPower(int colour, float power)
	{
		float red = ((0xff & colour >> 16) / 255f) * power; 
		if(red < 0) {
			red = 0;
		}
		else if(red > 1) {
			red = 1;
		}
		
		float green = ((0xff & colour >> 8) / 255f) * power; 
		if(green < 0) {
			green = 0;
		}
		else if(green > 1) {
			green = 1;
		}
		
		float blue = ((0xff & colour) / 255f) * power;
		if(blue < 0) {
			blue = 0;
		}
		else if(blue > 1) {
			blue = 1;
		}
		
		return ((int)(red * 255) << 16 | (int)(green * 255) << 8 | (int)(blue * 255));
	}
	
	public static int getLightSum(int colour, float power)
	{
		float red = ((0xff & colour >> 16) / 255f) + power; 
		if(red < 0) {
			red = 0;
		}
		else if(red > 1) {
			red = 1;
		}
		
		float green = ((0xff & colour >> 8) / 255f) + power; 
		if(green < 0) {
			green = 0;
		}
		else if(green > 1) {
			green = 1;
		}
		
		float blue = ((0xff & colour) / 255f) + power;
		if(blue < 0) {
			blue = 0;
		}
		else if(blue > 1) {
			blue = 1;
		}
		
		return ((int)(red * 255) << 16 | (int)(green * 255) << 8 | (int)(blue * 255));
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getDiameter() {
		return diameter;
	}

	public void setDiameter(int diameter) {
		this.diameter = diameter;
	}

	public int getColour() {
		return colour;
	}

	public void setColour(int colour) {
		this.colour = colour;
	}

	public int[] getLightMap() {
		return lightMap;
	}

	public void setLightMap(int[] lightMap) {
		this.lightMap = lightMap;
	}
}
