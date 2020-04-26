package com.nimbus.engine.ecs.component.lib;

import com.nimbus.engine.ecs.component.IComponent;

public class CBody implements IComponent {
	
	private float width;
	private float height;
	
	public CBody(float width, float height) {
		this.width = width;
		this.height = height;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
}
