package examples.platformer.system;

import java.util.List;
import java.util.stream.Collectors;

import com.nimbus.engine.Renderer;
import com.nimbus.engine.ecs.Datastore;
import com.nimbus.engine.ecs.Query;
import com.nimbus.engine.ecs.component.lib.CAABB;
import com.nimbus.engine.ecs.component.lib.CBody;
import com.nimbus.engine.ecs.component.lib.CPlayer;
import com.nimbus.engine.ecs.component.lib.CTransform;
import com.nimbus.engine.ecs.component.lib.CVelocity;
import com.nimbus.engine.ecs.entity.Entity;
import com.nimbus.engine.ecs.system.ASystem;

import examples.platformer.component.CBlock;

public class SPlayerCollision extends ASystem{
	
	private Datastore datastore;
	
	public SPlayerCollision() {
		super();
	}

	public SPlayerCollision(int priority, boolean isParallel) {
		super(priority, isParallel);
	}

	@Override
	public void init(Datastore datastore) {
		this.datastore = datastore;
	}
	
	public List<Entity> getEntitiesToProcess() {
		Query query = new Query();
		query
		.hasComponent(CPlayer.class)
		.hasComponent(CAABB.class)
		.hasComponent(CBody.class)
		.hasComponent(CTransform.class);
		List<Entity> entitiesToProcess = datastore.executeQuery(query)
		.stream()
		.filter(entity -> ((CAABB) entity.getComponent(CAABB.class)).hasCollided())
		.collect(Collectors.toList());
		return entitiesToProcess;
	}


	@Override
	public void update(float deltaTime) {
		for(Entity entity : getEntitiesToProcess()) {
			CAABB aabb = (CAABB) entity.getComponent(CAABB.class);
			CTransform transform = (CTransform) entity.getComponent(CTransform.class);
			CBody body = (CBody) entity.getComponent(CBody.class);
			CVelocity velocity = (CVelocity) entity.getComponent(CVelocity.class);

			List<Entity> collidedBlockEntities = aabb.getCollidedEntities().stream().filter(collidedEntity -> collidedEntity.hasComponent(CBlock.class)).collect(Collectors.toList());
			for(Entity collidedEntity : collidedBlockEntities) {
				CTransform collidedTransform = (CTransform) collidedEntity.getComponent(CTransform.class);
				CBody collidedBody = (CBody) collidedEntity.getComponent(CBody.class);
				CAABB collidedAabb = (CAABB) collidedEntity.getComponent(CAABB.class);
				
				float entityHalfWidth = body.getWidth() / 2;
				float entityHalfHeight = body.getHeight() / 2;
				float collidedHalfWidth = collidedBody.getWidth() / 2;
				float collidedHalfHeight = collidedBody.getHeight() / 2;
				float entityCenterX = transform.getPosition().getX() + entityHalfWidth;
				float entityCenterY = transform.getPosition().getY() + entityHalfHeight;
				float collidedCenterX = collidedTransform.getPosition().getX() + collidedHalfWidth;
				float collidedCenterY = collidedTransform.getPosition().getY() + collidedHalfHeight;
				
				if (Math.abs(aabb.getLastCenterX() - collidedAabb.getLastCenterX()) < entityHalfWidth + collidedHalfWidth) {
					//We are dealing with a top/bottom collision
					if (entityCenterY < collidedCenterY) {
						// We are on top
						// How close are we and how close should we be?
						float correctionDistance = ((entityHalfHeight + collidedHalfHeight) - (collidedCenterY - entityCenterY));
						transform.getPosition().setY(transform.getPosition().getY() - correctionDistance);
						aabb.setCurrentCenterY((entityCenterY - correctionDistance));
					}	

					else if (entityCenterY > collidedCenterY) {
						// We are on the bottom
						// How close are we and how close should we be?
						float correctionDistance = ((entityHalfHeight + collidedHalfHeight) - (entityCenterY - collidedCenterY));
						transform.getPosition().setY(transform.getPosition().getY() + correctionDistance);
						aabb.setCurrentCenterY((entityCenterY + correctionDistance));
					}
					if(velocity != null) {
						//If we've hit something and there is velocity present then set it to 0
						velocity.getVelocity().setY(0);
					}
				}
				
				//Ignore left and right collision if we were last seen above or below the other object. This stops gravity and flight from creating an invisible wall
				else if (aabb.getLastCenterY() + entityHalfHeight > collidedAabb.getLastCenterY() - collidedHalfHeight && aabb.getLastCenterY() - entityHalfHeight < collidedAabb.getLastCenterY() + collidedHalfHeight) {
					//We are dealing with a left and right collision
					if (entityCenterX < collidedCenterX) {
						// We are on the left
						// How close are we and how close should we be?
						float correctionDistance = ((entityHalfWidth + collidedHalfWidth) - (collidedCenterX - entityCenterX));
						transform.getPosition().setX(transform.getPosition().getX() - correctionDistance);
						aabb.setCurrentCenterX((entityCenterX - correctionDistance));
					}
					else if (entityCenterX > collidedCenterX) {
						// We are on the right
						// How close are we and how close should we be?
						float correctionDistance = ((entityHalfWidth + collidedHalfWidth) - (entityCenterX - collidedCenterX));
						transform.getPosition().setX(transform.getPosition().getX() + correctionDistance);	
						aabb.setCurrentCenterX((entityCenterX + correctionDistance));		
					}
					if(velocity != null) {
						//If we've hit something and there is velocity present then set it to 0
						velocity.getVelocity().setX(0);
					}

				}
			}
		}
	}

	@Override
	public void render(Renderer renderer) {}

}
