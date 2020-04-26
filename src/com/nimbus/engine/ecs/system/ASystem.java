package com.nimbus.engine.ecs.system;

import com.nimbus.engine.Renderer;
import com.nimbus.engine.ecs.Datastore;

public abstract class ASystem {

	protected int priority;
	protected boolean isParallel;

	// Instantiate with a given priority
	public ASystem(int priority, boolean isParallel) {
		setPriority(priority);
		setParallel(isParallel);
	}

	// Use default priority 0
	public ASystem() {
		setPriority(0);
		setParallel(false);
	}

	// Do any necessary system setup operations
	public abstract void init(Datastore datastore);

	// Life cycle method: the update function updates all values in the scene
	public abstract void update(float deltaTime);

	// Life cycle method: the render function renders to the canvas (note that this
	// is NOT in sync with update())
	public abstract void render(Renderer renderer);

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isParallel() {
		return isParallel;
	}

	public void setParallel(boolean isParallel) {
		this.isParallel = isParallel;
	}

}
