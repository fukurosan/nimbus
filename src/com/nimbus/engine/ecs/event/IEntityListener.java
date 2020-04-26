package com.nimbus.engine.ecs.event;

import com.nimbus.engine.ecs.entity.Entity;

public interface IEntityListener {
	public abstract void entityAdded(Entity entity);
	public abstract void entityRemoved(Entity entity);
}
