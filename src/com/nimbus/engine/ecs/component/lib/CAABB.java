package com.nimbus.engine.ecs.component.lib;

import java.util.ArrayList;
import java.util.List;

import com.nimbus.engine.ecs.component.IComponent;
import com.nimbus.engine.ecs.entity.Entity;

public class CAABB implements IComponent{
	
	private List<Entity> collidedEntities;
	private float lastCenterX;
	private float lastCenterY;
	private float currentCenterX;
	private float currentCenterY;

	public CAABB() {
		collidedEntities = new ArrayList<Entity>();
	}

	public boolean isCollided() {
		return collidedEntities.size() > 0;
	}

	public List<Entity> getCollidedEntities() {
		return collidedEntities;
	}

	public void setCollidedEntities(List<Entity> collidedEntities) {
		this.collidedEntities = collidedEntities;
	}
	
	public boolean hasCollided() {
		return collidedEntities.size() > 0;
	}

	public float getLastCenterX() {
		return lastCenterX;
	}

	public void setLastCenterX(float lastCenterX) {
		this.lastCenterX = lastCenterX;
	}

	public float getLastCenterY() {
		return lastCenterY;
	}

	public void setLastCenterY(float lastCenterY) {
		this.lastCenterY = lastCenterY;
	}

	public float getCurrentCenterX() {
		return currentCenterX;
	}

	public void setCurrentCenterX(float currentCenterX) {
		this.currentCenterX = currentCenterX;
	}

	public float getCurrentCenterY() {
		return currentCenterY;
	}

	public void setCurrentCenterY(float currentCenterY) {
		this.currentCenterY = currentCenterY;
	}
	
}
