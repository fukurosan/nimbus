package com.nimbus.engine.ecs.system.lib;

import java.util.List;

import com.nimbus.engine.Renderer;
import com.nimbus.engine.ecs.Datastore;
import com.nimbus.engine.ecs.Query;
import com.nimbus.engine.ecs.component.lib.CTransform;
import com.nimbus.engine.ecs.component.lib.CVelocity;
import com.nimbus.engine.ecs.entity.Entity;
import com.nimbus.engine.ecs.system.ASystem;

public class SMovement extends ASystem {
	
	private Datastore datastore;

	public SMovement() {
		super();
	}
	
	public SMovement(int priority, boolean isParallel) {
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
		.hasComponent(CTransform.class);
		return datastore.executeQuery(query);
	}
	
	@Override
	public void update(float deltaTime) {
		for(Entity entity : getEntitiesToProcess()) {
			CVelocity velocity = (CVelocity) entity.getComponent(CVelocity.class);
			CTransform transform = (CTransform) entity.getComponent(CTransform.class);
			float positionX = transform.getPosition().getX() + velocity.getVelocity().getX() * deltaTime;
			float positionY = transform.getPosition().getY() + velocity.getVelocity().getY() * deltaTime;
			transform.getPosition().setX(positionX);
			transform.getPosition().setY(positionY);
		}
	}

	@Override
	public void render(Renderer renderer) {}

}
