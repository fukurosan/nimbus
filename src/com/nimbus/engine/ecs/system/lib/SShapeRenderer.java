package com.nimbus.engine.ecs.system.lib;

import java.util.List;

import com.nimbus.engine.Renderer;
import com.nimbus.engine.ecs.Datastore;
import com.nimbus.engine.ecs.Query;
import com.nimbus.engine.ecs.component.lib.CBody;
import com.nimbus.engine.ecs.component.lib.CRectangleShape;
import com.nimbus.engine.ecs.component.lib.CTransform;
import com.nimbus.engine.ecs.entity.Entity;
import com.nimbus.engine.ecs.system.ASystem;

//For now only works on rectangles
public class SShapeRenderer extends ASystem {
	
	private Datastore datastore;

	public SShapeRenderer() {
		super();
	}
	
	public SShapeRenderer(int priority, boolean isParallel) {
		super(priority, isParallel);
	}
	
	@Override
	public void init(Datastore datastore) {
		this.datastore = datastore;
	}
	
	public List<Entity> getEntitiesToProcess() {
		Query query = new Query();
		query
		.hasComponent(CRectangleShape.class)
		.hasComponent(CTransform.class)
		.hasComponent(CBody.class);
		return datastore.executeQuery(query);
	}

	@Override
	public void render(Renderer renderer) {
		for (Entity entity : getEntitiesToProcess()) {
			CRectangleShape rectangle = (CRectangleShape) entity.getComponent(CRectangleShape.class);
			CTransform transform = (CTransform) entity.getComponent(CTransform.class);
			CBody body = (CBody) entity.getComponent(CBody.class);
			if(rectangle.isFilled()) {
				renderer.drawFilledRectangle((int) transform.getPosition().getX(), (int) transform.getPosition().getY(), (int) body.getWidth(), (int) body.getHeight(), rectangle.getColour(), transform.getzDepth());
			}
			else {
				renderer.drawRectangle((int) transform.getPosition().getX(), (int) transform.getPosition().getY(), (int) body.getWidth(), (int) body.getHeight(), rectangle.getColour(), transform.getzDepth());
			}
		}
	}

	@Override
	public void update(float deltaTime) {}

}
