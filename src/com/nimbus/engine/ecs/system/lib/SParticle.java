package com.nimbus.engine.ecs.system.lib;

import java.util.List;

import com.nimbus.engine.Renderer;
import com.nimbus.engine.ecs.Datastore;
import com.nimbus.engine.ecs.Query;
import com.nimbus.engine.ecs.component.lib.CParticle;
import com.nimbus.engine.ecs.component.lib.CTransform;
import com.nimbus.engine.ecs.entity.Entity;
import com.nimbus.engine.ecs.system.ASystem;
import com.nimbus.engine.graphics.light.Light;

public class SParticle extends ASystem {
	
	private Datastore datastore;
	
	public SParticle() {
		super();
	}
	
	public SParticle(int priority, boolean isParallel) {
		super(priority, isParallel);
	}
	
	public List<Entity> getEntitiesToProcess() {
		Query query = new Query();
		query
		.hasComponent(CParticle.class)
		.hasComponent(CTransform.class);
		return datastore.executeQuery(query);
	}

	@Override
	public void init(Datastore datastore) {
		this.datastore = datastore;
	}

	@Override
	public void update(float deltaTime) {
		for(Entity entity : getEntitiesToProcess()) {
			CParticle particle = (CParticle) entity.getComponent(CParticle.class);
			CTransform transform = (CTransform) entity.getComponent(CTransform.class);
			transform.getPosition().setX(transform.getPosition().getX() + deltaTime * particle.getDirectionX());
			transform.getPosition().setY(transform.getPosition().getY() + deltaTime * particle.getDirectionY());
			particle.setCooldown(particle.getCooldown() - deltaTime);
			if(particle.getCooldown() <= 0) {
				datastore.removeEntity(entity, false);
			}
		}
	}

	@Override
	public void render(Renderer renderer) {
		for(Entity entity : getEntitiesToProcess()) {
			CParticle particle = (CParticle) entity.getComponent(CParticle.class);
			CTransform transform = (CTransform) entity.getComponent(CTransform.class);
			
			int x = (int) transform.getPosition().getX() - renderer.getCameraX();
			int y = (int) transform.getPosition().getY() - renderer.getCameraY();
			int colour = particle.getColour();

			renderer.setzDepth(Integer.MAX_VALUE);
			renderer.setPixel(x, y, colour);
			renderer.setPixel(x + 1, y, colour);
			renderer.setPixel(x, y + 1, colour);
			renderer.setPixel(x + 1, y + 1, colour);
			renderer.setPixel(x + 2, y, colour);
			renderer.setPixel(x, y + 2, colour);
			renderer.setPixel(x + 2, y + 2, colour);
			renderer.drawLight(new Light(particle.getLightSize(), particle.getLightColour()), x + renderer.getCameraX() + 1, y + renderer.getCameraY() + 1);
		}
	}

	
	
}
