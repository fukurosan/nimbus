package examples.platformer.system;

import java.util.List;

import com.nimbus.engine.Renderer;
import com.nimbus.engine.ecs.Datastore;
import com.nimbus.engine.ecs.Query;
import com.nimbus.engine.ecs.component.lib.CBody;
import com.nimbus.engine.ecs.component.lib.CPlayer;
import com.nimbus.engine.ecs.component.lib.CTransform;
import com.nimbus.engine.ecs.entity.Entity;
import com.nimbus.engine.ecs.system.ASystem;

import examples.platformer.gameobjects.GOPlayer;

public class SPlayerLantern extends ASystem {

	private Datastore datastore;
	
	public SPlayerLantern() {
		super();
	}
	
	public SPlayerLantern(int priority, boolean isParallel) {
		super(priority, isParallel);
	}

	public List<Entity> getEntitiesToProcess() {
		Query query = new Query();
		query
		.hasComponent(CPlayer.class)
		.hasComponent(CBody.class)
		.hasComponent(CTransform.class);
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
		for(Entity entity : getEntitiesToProcess()) {
			CTransform transform = (CTransform) entity.getComponent(CTransform.class);
			CBody body = (CBody) entity.getComponent(CBody.class);
			renderer.drawLight(GOPlayer.light, (int) (transform.getPosition().getX() + body.getWidth() / 2), (int) (transform.getPosition().getY() + body.getHeight() / 2));			
		}
	}

	
	
	
}
