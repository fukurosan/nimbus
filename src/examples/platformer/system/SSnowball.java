package examples.platformer.system;

import java.util.List;
import java.util.stream.Collectors;

import com.nimbus.engine.Renderer;
import com.nimbus.engine.ecs.Datastore;
import com.nimbus.engine.ecs.Query;
import com.nimbus.engine.ecs.component.lib.CAABB;
import com.nimbus.engine.ecs.component.lib.CBody;
import com.nimbus.engine.ecs.component.lib.CParticle;
import com.nimbus.engine.ecs.component.lib.CTransform;
import com.nimbus.engine.ecs.entity.Entity;
import com.nimbus.engine.ecs.system.ASystem;
import com.nimbus.engine.graphics.Vector2f;
import com.nimbus.engine.graphics.light.Light;

import examples.platformer.component.CBlock;
import examples.platformer.component.CSnowball;
import examples.platformer.gameobjects.GOSnowball;

public class SSnowball extends ASystem {
	
	private Datastore datastore;
	
	public SSnowball() {
		super();
	}
	
	public SSnowball(int priority, boolean isParallel) {
		super(priority, isParallel);
	}

	public List<Entity> getEntitiesToProcess() {
		Query query = new Query();
		query
		.hasComponent(CSnowball.class)
		.hasComponent(CAABB.class);
		return datastore.executeQuery(query);
	}
	
	public List<Entity> getCollidedEntities() {
		return getEntitiesToProcess()
				.stream()
				.filter(entity -> ((CAABB) entity.getComponent(CAABB.class)).hasCollided())
				.collect(Collectors.toList());
	}
	
	@Override
	public void init(Datastore datastore) {
		this.datastore = datastore;
	}

	@Override
	public void update(float deltaTime) {
		for(Entity entity : getCollidedEntities()) {
			boolean blockCollided = false;
			CAABB aabb = (CAABB) entity.getComponent(CAABB.class);
			for(Entity collidedEntity : aabb.getCollidedEntities()) {
				if(collidedEntity.hasComponent(CBlock.class)) {
					blockCollided = true;
					break;
				}
			}
			if(blockCollided) {
				datastore.removeEntity(entity, false);
				CTransform transform = (CTransform) entity.getComponent(CTransform.class);
				createParticles(GOSnowball.EXPLOSION_NUMBER_OF_PARTICLES, transform.getPosition().getX(), transform.getPosition().getY());
			}
		}
	}

	@Override
	public void render(Renderer renderer) {
		for(Entity entity : getEntitiesToProcess()) {
			CTransform transform = (CTransform) entity.getComponent(CTransform.class);
			CBody body = (CBody) entity.getComponent(CBody.class);
			renderer.drawLight(new Light(GOSnowball.LIGHT_SIZE, GOSnowball.LIGHT_COLOUR), (int) (transform.getPosition().getX() + body.getWidth() / 2), (int) (transform.getPosition().getY() + body.getHeight() / 2));			
		}
	}
	
	public void createParticles(int numberOfParticles, float startX, float startY) {
		for(int i = 0; i < numberOfParticles; i++) {
			Entity particle = new Entity();
			particle.addComponent(new CTransform(new Vector2f(startX, startY), new Vector2f(1, 1), 0));
			particle.addComponent(new CParticle(CParticle.CENTER_TILT, CParticle.CENTER_TILT, GOSnowball.EXPLOSION_COOLDOWN, GOSnowball.EXPLOSION_FORCE, GOSnowball.EXPLOSION_COLOUR, GOSnowball.EXPLOSION_PARTICLE_LIGHT_SIZE, GOSnowball.EXPLOSION_LIGHT_COLOUR));
			datastore.addEntity(particle, false);
		}
	}

	
}
