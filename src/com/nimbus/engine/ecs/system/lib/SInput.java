package com.nimbus.engine.ecs.system.lib;

import java.util.Map;

import com.nimbus.engine.InputHandler;
import com.nimbus.engine.Renderer;
import com.nimbus.engine.ecs.Datastore;
import com.nimbus.engine.ecs.component.lib.GCInput;
import com.nimbus.engine.ecs.system.ASystem;

public class SInput extends ASystem {

	private InputHandler inputHandler;
	private GCInput inputComponent;

	public SInput() {
		super();
		inputHandler = InputHandler.INSTANCE;
	}
	
	public SInput(int priority, boolean isParallel) {
		super(priority, isParallel);
		inputHandler = InputHandler.INSTANCE;
	}

	@Override
	public void init(Datastore datastore) {
		inputComponent = (GCInput) datastore.getGlobalComponent(GCInput.class);
	}

	@Override
	public void update(float deltaTime) {
		inputComponent.nextTick();
		inputComponent.setMousePosition(inputHandler.getMouseX(), inputHandler.getMouseY());
		inputComponent.setScrollPosition(inputHandler.getScroll());
		Map<String, Integer> keyMap = inputComponent.getKeyMap();
		for (String key : keyMap.keySet()) {
			if(inputHandler.isKey(keyMap.get(key))) {
				inputComponent.setKeyDown(key);
			}
		}
	}

	@Override
	public void render(Renderer renderer) {}

}
