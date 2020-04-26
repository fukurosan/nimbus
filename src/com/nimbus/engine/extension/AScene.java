package com.nimbus.engine.extension;

import com.nimbus.engine.Nimbus;
import com.nimbus.engine.Renderer;
import com.nimbus.engine.ecs.Datastore;

public abstract class AScene {

	private Datastore datastore;

	// Instantiate the data store
	protected AScene() {
		datastore = new Datastore();
	}

	// Life cycle method: The Scene has been added to the game stack
	public abstract void entered();

	// Life cycle method: The Scene has been removed from the game stack
	public abstract void exiting();

	// Life cycle method: Another Scene is blocking in the stack
	public abstract void obscuring();

	// Life cycle method: A blocking Scene in the stack has been removed and this is now the live Scene
	public abstract void revealed();
	
	// Life cycle method: Triggered on update tick
	public void update(Nimbus nimbus, float deltaTime) {
		datastore.update(nimbus, deltaTime);
	}
	
	// Life cycle method: Triggered on render tick
	public void render(Nimbus nimbus, Renderer renderer) {
		datastore.render(nimbus, renderer);
	}
	
	// Life cycle method: Triggered after render tick post processing has finished
	public abstract void postProcessingFinished(Nimbus nimbus, Renderer renderer);

	protected Datastore getDatastore() {
		return datastore;
	}
		
}
