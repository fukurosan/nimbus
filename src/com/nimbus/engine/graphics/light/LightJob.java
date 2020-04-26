package com.nimbus.engine.graphics.light;

public class LightJob {
	private Light light;
	private int locationX;
	private int locationY;
	
	public LightJob(Light light, int locationX, int locationY) {
		this.light = light;
		this.locationX = locationX;
		this.locationY = locationY;
	}

	public Light getLight() {
		return light;
	}

	public void setLight(Light light) {
		this.light = light;
	}

	public int getLocationX() {
		return locationX;
	}

	public void setLocationX(int locationX) {
		this.locationX = locationX;
	}

	public int getLocationY() {
		return locationY;
	}

	public void setLocationY(int locationY) {
		this.locationY = locationY;
	}
}
