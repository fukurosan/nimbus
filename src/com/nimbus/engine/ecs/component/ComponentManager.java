package com.nimbus.engine.ecs.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nimbus.engine.ecs.event.IComponentListener;

public class ComponentManager {

	private Map<Class<? extends IGlobalComponent>, IGlobalComponent> globalComponents;
	private List<IComponentListener> listeners;
	
	public ComponentManager() {
		globalComponents = new HashMap<Class<? extends IGlobalComponent>, IGlobalComponent>();
		listeners = new ArrayList<IComponentListener>();
	}
	
	public void addGlobalComponent(IGlobalComponent component) {
		globalComponents.put(component.getClass(), component);
		for(IComponentListener listener : listeners) {
			listener.globalComponentAdded(component);
		}
	}
	
	public void removeGlobalComponent(IGlobalComponent component) {
		globalComponents.remove(component.getClass());
		for(IComponentListener listener : listeners) {
			listener.globalComponentRemoved(component);
		}
	}

	public Map<Class<? extends IGlobalComponent>, IGlobalComponent> getGlobalComponents() {
		return globalComponents;
	}
	
	public boolean hasComponent(Class<? extends IGlobalComponent> clazz) {
		return globalComponents.containsKey(clazz);
	}
	
	// Add a listener
	public void addListener(IComponentListener listener) {
		listeners.add(listener);
	}
	
	// Remove a listener
	public void removeListener(IComponentListener listener) {
		listeners.remove(listener);
	}
	
}
