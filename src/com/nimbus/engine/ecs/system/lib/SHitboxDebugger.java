package com.nimbus.engine.ecs.system.lib;

import java.util.List;

import com.nimbus.engine.Renderer;
import com.nimbus.engine.ecs.Datastore;
import com.nimbus.engine.ecs.Query;
import com.nimbus.engine.ecs.component.lib.CAABB;
import com.nimbus.engine.ecs.component.lib.CBody;
import com.nimbus.engine.ecs.component.lib.CTransform;
import com.nimbus.engine.ecs.entity.Entity;
import com.nimbus.engine.ecs.system.ASystem;

public class SHitboxDebugger extends ASystem {
	
	private Datastore datastore;
	
	public SHitboxDebugger() {
		super();
	}
	
	public SHitboxDebugger(int priority, boolean isParallel) {
		super(priority, isParallel);
	}
	
	public List<Entity> getEntitiesToProcess() {
		Query query = new Query();
		query
		.hasComponent(CAABB.class)
		.hasComponent(CTransform.class)
		.hasComponent(CBody.class);
		return datastore.executeQuery(query);
	}

	@Override
	public void init(Datastore datastore) {
		this.datastore = datastore;
	}

	@Override
	public void update(float deltaTime) {}

	@Override
	public void render(Renderer renderer) {
		for (Entity entity : getEntitiesToProcess()) {
			CTransform transform = (CTransform) entity.getComponent(CTransform.class);
			CBody body = (CBody) entity.getComponent(CBody.class);
			renderer.drawRectangle((int) transform.getPosition().getX(), (int) transform.getPosition().getY(), (int) body.getWidth(), (int) body.getHeight(), 0xffffffff, transform.getzDepth());			
		}
	}

}
