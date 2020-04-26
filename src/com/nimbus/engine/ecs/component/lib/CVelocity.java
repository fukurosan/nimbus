package com.nimbus.engine.ecs.component.lib;

import com.nimbus.engine.ecs.component.IComponent;
import com.nimbus.engine.graphics.Vector2f;

public class CVelocity implements IComponent {
	private Vector2f velocity;

	public CVelocity(Vector2f velocity) {
		this.velocity = velocity;
	}

	public Vector2f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2f velocity) {
		this.velocity = velocity;
	}
}
