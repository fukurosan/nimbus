package com.nimbus.engine.ecs.system.lib;

import java.util.List;

import com.nimbus.engine.Renderer;
import com.nimbus.engine.ecs.Datastore;
import com.nimbus.engine.ecs.Query;
import com.nimbus.engine.ecs.component.lib.CSprite;
import com.nimbus.engine.ecs.component.lib.CSpriteAnimation;
import com.nimbus.engine.ecs.component.lib.CTransform;
import com.nimbus.engine.ecs.entity.Entity;
import com.nimbus.engine.ecs.system.ASystem;

public class SSpriteRenderer extends ASystem {
	
	private Datastore datastore;

	public SSpriteRenderer() {
		super();
	}
	
	public SSpriteRenderer(int priority, boolean isParallel) {
		super(priority, isParallel);
	}
	
	@Override
	public void init(Datastore datastore) {
		this.datastore = datastore;
	}
	
	public List<Entity> getEntitiesToProcess() {
		Query spriteQuery = new Query();
		Query animationQuery = new Query();
		
		spriteQuery
		.hasComponent(CSprite.class)
		.hasComponent(CTransform.class);
		
		animationQuery
		.hasComponent(CSpriteAnimation.class)
		.hasComponent(CTransform.class);
		
		return datastore.executeQuery(spriteQuery.outerJoin(animationQuery));
	}

	@Override
	public void update(float deltaTime) {
		for(Entity entity : getEntitiesToProcess()) {
			CSpriteAnimation animation = (CSpriteAnimation) entity.getComponent(CSpriteAnimation.class);
			if(animation != null) {
				float animationTimer = animation.getAnimationTimer() + deltaTime * animation.getSpeed();
				animation.setAnimationTimer(animationTimer);
				if((int) animationTimer > animation.getCurrentFrame()) {
					animation.nextFrame();
				}
			}
		}
	}
	
	@Override
	public void render(Renderer renderer) {
		for(Entity entity : getEntitiesToProcess()) {
			CSprite sprite = (CSprite) entity.getComponent(CSprite.class);
			CTransform transform = (CTransform) entity.getComponent(CTransform.class);
			CSpriteAnimation animation = (CSpriteAnimation) entity.getComponent(CSpriteAnimation.class);
			if(animation != null) {
				renderer.drawImage(animation.getSprite(), (int) transform.getPosition().getX(), (int) transform.getPosition().getY(), transform.getzDepth());
			}
			else {
				renderer.drawImage(sprite.getSprite(), (int) transform.getPosition().getX(), (int) transform.getPosition().getY(), transform.getzDepth());
			}
		}
	}
	
}
