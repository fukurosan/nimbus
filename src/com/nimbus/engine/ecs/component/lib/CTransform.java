package com.nimbus.engine.ecs.component.lib;

import com.nimbus.engine.ecs.component.IComponent;
import com.nimbus.engine.graphics.Vector2f;

public class CTransform implements IComponent{
	private Vector2f position;
	private Vector2f scale;
	private int zDepth;
	
	public CTransform(Vector2f position, Vector2f scale, int zDepth) {
		this.position = position;
		this.scale = scale;
		this.zDepth = zDepth;
	}

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public Vector2f getScale() {
		return scale;
	}

	public void setScale(Vector2f scale) {
		this.scale = scale;
	}

	public int getzDepth() {
		return zDepth;
	}

	public void setzDepth(int zDepth) {
		this.zDepth = zDepth;
	}
}
