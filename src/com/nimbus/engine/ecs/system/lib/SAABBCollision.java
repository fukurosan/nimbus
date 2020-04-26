package com.nimbus.engine.ecs.system.lib;

import java.util.List;

import com.nimbus.engine.Renderer;
import com.nimbus.engine.ecs.Datastore;
import com.nimbus.engine.ecs.Query;
import com.nimbus.engine.ecs.component.lib.CAABB;
import com.nimbus.engine.ecs.component.lib.CBody;
import com.nimbus.engine.ecs.component.lib.CTransform;
import com.nimbus.engine.ecs.component.lib.GCQuadTree;
import com.nimbus.engine.ecs.entity.Entity;
import com.nimbus.engine.ecs.system.ASystem;

public class SAABBCollision extends ASystem {

	private Datastore datastore;
	private GCQuadTree quadtree;
	
	public SAABBCollision() {
		super();
	}
	
	public SAABBCollision(int priority, boolean isParallel) {
		super(priority, isParallel);
	}
		
	public List<Entity> getEntitiesToProcess() {
		Query query = new Query();
		query
		.hasComponent(CAABB.class)
		.hasComponent(CBody.class)
		.hasComponent(CTransform.class);
		return datastore.executeQuery(query);
	}

	@Override
	public void init(Datastore datastore) {
		this.datastore = datastore;
		quadtree = (GCQuadTree) datastore.getGlobalComponent(GCQuadTree.class);
	}

	@Override
	public void update(float deltaTime) {
		quadtree.clear();
		
		List<Entity> entitiesToProcess = getEntitiesToProcess();
		
		for(Entity entity : entitiesToProcess) {
			CTransform transform = (CTransform) entity.getComponent(CTransform.class);
			CBody body = (CBody) entity.getComponent(CBody.class);
			CAABB aabb = (CAABB) entity.getComponent(CAABB.class);
			aabb.getCollidedEntities().clear();
			aabb.setLastCenterX(aabb.getCurrentCenterX());
			aabb.setLastCenterY(aabb.getCurrentCenterY());
			aabb.setCurrentCenterX((int) (transform.getPosition().getX() + body.getWidth() / 2));
			aabb.setCurrentCenterY((int) (transform.getPosition().getY() + body.getHeight() / 2));
			quadtree.insert(entity, (int) transform.getPosition().getX(), (int) transform.getPosition().getY(), (int) body.getWidth(), (int) body.getHeight());
		}
		
		for(Entity entity : entitiesToProcess) {
			CTransform transform = (CTransform) entity.getComponent(CTransform.class);
			CBody body = (CBody) entity.getComponent(CBody.class);
			List<Entity> potentialCollisions = quadtree.findPotentialCollisions(entity, (int) transform.getPosition().getX(), (int) transform.getPosition().getY(), (int) body.getWidth(), (int) body.getHeight());
			
			for(Entity potentialCollidedEntity : potentialCollisions) {
				CTransform collidedTransform = (CTransform) potentialCollidedEntity.getComponent(CTransform.class);
				CBody collidedBody = (CBody) potentialCollidedEntity.getComponent(CBody.class);
				
				float entityHalfWidth = body.getWidth() / 2;
				float entityHalfHeight = body.getHeight() / 2;
				float collidedHalfWidth = collidedBody.getWidth() / 2;
				float collidedHalfHeight = collidedBody.getHeight() / 2;
				float entityCenterX = transform.getPosition().getX() + entityHalfWidth;
				float entityCenterY = transform.getPosition().getY() + entityHalfHeight;
				float collidedCenterX = collidedTransform.getPosition().getX() + collidedHalfWidth;
				float collidedCenterY = collidedTransform.getPosition().getY() + collidedHalfHeight;
				
				if(Math.abs(entityCenterX - collidedCenterX) <= entityHalfWidth + collidedHalfWidth) {
					if(Math.abs(entityCenterY - collidedCenterY) <= entityHalfHeight + collidedHalfHeight) {
						CAABB aabb = (CAABB) entity.getComponent(CAABB.class);
						aabb.getCollidedEntities().add(potentialCollidedEntity);
					}					
				}
				
			}
		}
	}

	@Override
	public void render(Renderer renderer) {}
	
}
