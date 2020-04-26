package com.nimbus.engine.ecs.system.lib;

import java.util.List;

import com.nimbus.engine.Renderer;
import com.nimbus.engine.ecs.Datastore;
import com.nimbus.engine.ecs.Query;
import com.nimbus.engine.ecs.component.lib.CMass;
import com.nimbus.engine.ecs.component.lib.CVelocity;
import com.nimbus.engine.ecs.entity.Entity;
import com.nimbus.engine.ecs.system.ASystem;
import com.nimbus.engine.graphics.Vector2f;

public class SGravity extends ASystem {

	private Datastore datastore;
	
	public SGravity() {
		super();
	}
	
	public SGravity(int priority, boolean isParallel) {
		super(priority, isParallel);
	}
	
	@Override
	public void init(Datastore datastore) {
		this.datastore = datastore;
	}
	
	public List<Entity> getEntitiesToProcess() {
		Query query = new Query();
		query
		.hasComponent(CVelocity.class)
		.hasComponent(CMass.class);
		return datastore.executeQuery(query);
	}

	@Override
	public void update(float deltaTime) {
		for(Entity entity : getEntitiesToProcess()) {
			CVelocity velocity = (CVelocity) entity.getComponent(CVelocity.class);
			CMass mass = (CMass) entity.getComponent(CMass.class);
			Vector2f velocityVector = velocity.getVelocity();
			velocityVector.setY(velocityVector.getY() + deltaTime * mass.getWeight());
		}
	}

	@Override
	public void render(Renderer renderer) {}

}
