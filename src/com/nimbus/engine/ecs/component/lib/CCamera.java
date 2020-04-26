package com.nimbus.engine.ecs.component.lib;

import com.nimbus.engine.ecs.component.IComponent;
import com.nimbus.engine.graphics.Vector2f;

public class CCamera implements IComponent{

	private Vector2f position;
	private int width;
	private int height;
	private boolean isBound;
	private int boundOffsetX;
	private int boundOffsetY;
	private int cameraSpeed = 4;
	
	public CCamera(Vector2f position, int width, int height, int boundOffsetX, int boundOffsetY, int cameraSpeed) {
		this.position = position;
		this.width = width;
		this.height = height;
		this.boundOffsetX = boundOffsetX;
		this.boundOffsetY = boundOffsetY;
		isBound = true;
		this.cameraSpeed = cameraSpeed;
	}
	
	public CCamera(Vector2f position, int width, int height) {
		this.position = position;
		this.width = width;
		this.height = height;
		isBound = false;
		boundOffsetX = -1;
		boundOffsetY = -1;
		cameraSpeed = -1;
	}
	
	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getBoundOffsetX() {
		return boundOffsetX;
	}

	public void setBoundOffsetX(int boundOffsetX) {
		this.boundOffsetX = boundOffsetX;
	}

	public int getBoundOffsetY() {
		return boundOffsetY;
	}

	public void setBoundOffsetY(int boundOffsetY) {
		this.boundOffsetY = boundOffsetY;
	}

	public int getCameraSpeed() {
		return cameraSpeed;
	}

	public void setCameraSpeed(int cameraSpeed) {
		this.cameraSpeed = cameraSpeed;
	}

	public boolean isBound() {
		return isBound;
	}

	public void setBound(boolean isBound) {
		this.isBound = isBound;
	}
	
}
