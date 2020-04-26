package com.nimbus.engine.ecs.entity;

import java.util.HashMap;
import java.util.UUID;

import com.nimbus.engine.ecs.component.IComponent;

public class Entity {

	private UUID entityID;
	private HashMap<Class<? extends IComponent>, IComponent> components;

	public Entity(UUID entityID) {
		this.entityID = entityID;
		this.components = new HashMap<Class<? extends IComponent>, IComponent>();
	}
	
	public Entity() {
		this.entityID = UUID.randomUUID();
		this.components = new HashMap<Class<? extends IComponent>, IComponent>();
	}

	public void addComponent(IComponent component) {
		this.components.put(component.getClass(), component);
	}

	public void removeComponentClass(Class<? extends IComponent> classToRemove) {
		this.components.remove(classToRemove);
	}

	public IComponent getComponent(Class<? extends IComponent> componentClass) {
		return components.get(componentClass);
	}

	public boolean hasComponent(Class<? extends IComponent> componentClass) {
		return components.containsKey(componentClass);
	}

	public UUID getEntityID() {
		return entityID;
	}

	public HashMap<Class<? extends IComponent>, IComponent> getAllComponents() {
		return components;
	}

}
