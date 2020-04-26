package com.nimbus.engine.ecs.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.nimbus.engine.Renderer;
import com.nimbus.engine.ecs.Datastore;
import com.nimbus.engine.ecs.event.ISystemListener;

public class SystemManager {

	private List<ASystem> systems;
	private Comparator<ASystem> compareSystemByPriority = (ASystem o1, ASystem o2) -> o1.getPriority() - o2.getPriority();
	private List<ISystemListener> listeners;
	private Datastore datastore;

	public SystemManager(Datastore datastore) {
		this.datastore = datastore;
		systems = new ArrayList<ASystem>();
		listeners = new ArrayList<ISystemListener>();
	}

	// Update all systems in order of priority, first sync and then async
	public void update(float deltaTime) {
		systems.forEach(system -> {
			if (!system.isParallel()) {
				system.update(deltaTime);
			}
		});
		//A bit crude, but for now..
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
		systems.forEach(system -> {
			if (system.isParallel()) {
				Runnable thread = () -> {
					system.update(deltaTime);
				};
				executor.execute(thread);
			}
		});
		executor.shutdown();
		try {
			executor.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Render all entity systems in order of priority
	public void render(Renderer renderer) {
		systems.forEach(system -> {
			system.render(renderer);
		});
	}

	// Sort systems by priority
	public void sortSystemsByPriority() {
		Collections.sort(systems, compareSystemByPriority);
	}

	// Add a given system
	public void addSystem(ASystem system) {
		systems.add(system);
		sortSystemsByPriority();
		system.init(datastore);
		for (ISystemListener listener : listeners) {
			listener.systemAdded(system);
		}
	}

	// Remove a given system
	public void removeSystem(ASystem system) {
		systems.remove(system);
		sortSystemsByPriority();
		for (ISystemListener listener : listeners) {
			listener.systemRemoved(system);
		}
	}

	// Add a listener
	public void addListener(ISystemListener listener) {
		listeners.add(listener);
	}

	// Remove a listener
	public void removeListener(ISystemListener listener) {
		listeners.remove(listener);
	}

}
