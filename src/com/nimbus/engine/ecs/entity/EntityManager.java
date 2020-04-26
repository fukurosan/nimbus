package com.nimbus.engine.ecs.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.nimbus.engine.ecs.component.IComponent;
import com.nimbus.engine.ecs.event.IEntityListener;

public class EntityManager {

	private List<Entity> entities;
	private List<EntityJob> jobQueue;
	private List<IEntityListener> listeners;

	// Instantiate an empty array of entities and jobs
	public EntityManager() {
		entities = new ArrayList<Entity>();
		jobQueue = new ArrayList<EntityJob>();
		listeners = new ArrayList<IEntityListener>();
	}

	// Schedules an entity to be created
	public void addEntity(Entity entity, boolean isImmediate) {
		if (!entities.contains(entity)) {
			if (isImmediate) {
				addEntity(entity);
			} else {
				EntityJob updateJob = new EntityJob();
				updateJob.type = EntityJobType.Add;
				updateJob.entity = entity;
				jobQueue.add(updateJob);
			}
		}
	}

	// Schedules an entity to be removed
	public void removeEntity(Entity entity, boolean isImmediate) {
		if (isImmediate) {
			removeEntity(entity);
		} else {
			EntityJob updateJob = new EntityJob();
			updateJob.type = EntityJobType.Remove;
			updateJob.entity = entity;
			jobQueue.add(updateJob);
		}
	}

	// Process the job queue
	public void processJobQueue() {
		jobQueue.forEach(job -> {
			if (job.type == EntityJobType.Add) {
				addEntity(job.entity);
			} else {
				removeEntity(job.entity);
			}
		});
		jobQueue.clear();
	}

	// Add an entity
	private void addEntity(Entity entity) {
		entities.add(entity);
		for (IEntityListener listener : listeners) {
			listener.entityAdded(entity);
		}
	}

	// Remove an entity
	private void removeEntity(Entity entity) {
		entities.remove(entity);
		for (IEntityListener listener : listeners) {
			listener.entityRemoved(entity);
		}
	}

	// Helper class for job handling
	private enum EntityJobType {
		Add, Remove
	}

	private class EntityJob {
		public EntityJobType type;
		public Entity entity;
	}

	// Get all entities
	public List<Entity> getEntities() {
		return entities;
	}

	// Get entities that has a certain component
	public List<Entity> getEntitiesHas(List<Entity> entities, Class<? extends IComponent> clazz) {
		return entities.stream().filter(entity -> entity.hasComponent(clazz)).collect(Collectors.toList());
	}

	// Get entities that do not have a certain component
	public List<Entity> getEntitiesNotHas(List<Entity> entities, Class<? extends IComponent> clazz) {
		return entities.stream().filter(entity -> !entity.hasComponent(clazz)).collect(Collectors.toList());
	}

	// Add a listener
	public void addListener(IEntityListener listener) {
		listeners.add(listener);
	}

	// Remove a listener
	public void removeListener(IEntityListener listener) {
		listeners.remove(listener);
	}

}
