package com.nimbus.engine.ecs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.nimbus.engine.Nimbus;
import com.nimbus.engine.Renderer;
import com.nimbus.engine.ecs.component.ComponentManager;
import com.nimbus.engine.ecs.component.IComponent;
import com.nimbus.engine.ecs.component.IGlobalComponent;
import com.nimbus.engine.ecs.entity.Entity;
import com.nimbus.engine.ecs.entity.EntityManager;
import com.nimbus.engine.ecs.event.IComponentListener;
import com.nimbus.engine.ecs.event.IEntityListener;
import com.nimbus.engine.ecs.event.ISystemListener;
import com.nimbus.engine.ecs.system.ASystem;
import com.nimbus.engine.ecs.system.SystemManager;

public class Datastore {

	private EntityManager entityManager;
	private ComponentManager componentManager;
	private SystemManager systemManager;

	// Instantiate entity and system managers
	public Datastore() {
		entityManager = new EntityManager();
		componentManager = new ComponentManager();
		systemManager = new SystemManager(this);

		DatastoreEventHandler eventHandler = new DatastoreEventHandler(this);
		addEntityEventListener(eventHandler);
		addComponentEventListener(eventHandler);
		addSystemEventListener(eventHandler);
	}

	// Lifecycle method: Triggered on update tick
	public void update(Nimbus nimbus, float deltaTime) {
		systemManager.update(deltaTime);
		entityManager.processJobQueue();
	}

	// Lifecycle method: Triggered on render tick
	public void render(Nimbus nimbus, Renderer renderer) {
		systemManager.render(renderer);
	}

	// Execute queries to find entities in the data store that match certain criteria
	@SuppressWarnings("unchecked")
	public List<Entity> executeQuery(Query query) {
		List<Entity> entities = new ArrayList<Entity>(entityManager.getEntities());
		for (Operation operation : query.getOperations()) {
			switch (operation.getOperationType()) {
			case HAS:
				entities = entityManager.getEntitiesHas(entities, (Class<? extends IComponent>) operation.getData());
				break;
			case NOT_HAS:
				entities = entityManager.getEntitiesNotHas(entities, (Class<? extends IComponent>) operation.getData());
				break;
			case INNER_JOIN:
				List<Entity> innerJoinEntities = executeQuery((Query) operation.getData());
				entities = entities.stream().filter(entity -> innerJoinEntities.contains(entity)).collect(Collectors.toList());
				break;
			case OUTER_JOIN:
				List<Entity> outerJoinEntities = executeQuery((Query) operation.getData());
				entities = Stream.concat(entities.stream(), outerJoinEntities.stream()).distinct().collect(Collectors.toList());
				break;
			default:
				System.out.println("NO SUCH QUERY OPERATION");
				break;
			}
		}
		return entities;
	}

	// Supporting methods to allow for interacting with the data store
	public void addEntity(Entity entity, boolean isImmediate) {
		entityManager.addEntity(entity, isImmediate);
	}

	public void removeEntity(Entity entity, boolean isImmediate) {
		entityManager.removeEntity(entity, isImmediate);
	}

	public void addGlobalComponent(IGlobalComponent component) {
		componentManager.addGlobalComponent(component);
	}

	public void removeGlobalComponent(IGlobalComponent component) {
		componentManager.removeGlobalComponent(component);
	}

	public IGlobalComponent getGlobalComponent(Class<? extends IGlobalComponent> clazz) {
		return componentManager.getGlobalComponents().get(clazz);
	}

	public void addSystem(ASystem system) {
		systemManager.addSystem(system);
	}

	public void removeSystem(ASystem system) {
		systemManager.removeSystem(system);
	}

	public void addEntityEventListener(IEntityListener listener) {
		entityManager.addListener(listener);
	}

	public void addComponentEventListener(IComponentListener listener) {
		componentManager.addListener(listener);
	}

	public void addSystemEventListener(ISystemListener listener) {
		systemManager.addListener(listener);
	}

	// Event handler for the different systems to help organize the data flows
	private class DatastoreEventHandler implements IEntityListener, IComponentListener, ISystemListener {

		@SuppressWarnings("unused")
		private Datastore datastore;

		public DatastoreEventHandler(Datastore datastore) {
			this.datastore = datastore;
		}

		@Override
		public void entityAdded(Entity entity) {}

		@Override
		public void entityRemoved(Entity entity) {}

		@Override
		public void globalComponentAdded(IGlobalComponent component) {}

		@Override
		public void globalComponentRemoved(IGlobalComponent component) {}

		@Override
		public void systemAdded(ASystem system) {}

		@Override
		public void systemRemoved(ASystem system) {}

	}

}
