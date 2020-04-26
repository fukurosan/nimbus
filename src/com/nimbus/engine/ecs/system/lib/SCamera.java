package com.nimbus.engine.ecs.system.lib;

import java.util.List;

import com.nimbus.engine.Renderer;
import com.nimbus.engine.ecs.Datastore;
import com.nimbus.engine.ecs.Query;
import com.nimbus.engine.ecs.component.lib.CBody;
import com.nimbus.engine.ecs.component.lib.CCamera;
import com.nimbus.engine.ecs.component.lib.CPlayer;
import com.nimbus.engine.ecs.component.lib.CTransform;
import com.nimbus.engine.ecs.entity.Entity;
import com.nimbus.engine.ecs.system.ASystem;

public class SCamera extends ASystem {

	private Datastore datastore;

	public SCamera() {
		super();
	}
	
	public SCamera(int priority, boolean isParallel) {
		super(priority, isParallel);
	}

	@Override
	public void init(Datastore datastore) {
		this.datastore = datastore;
	}

	public List<Entity> getEntitiesToProcess() {
		Query query = new Query();
		query
		.hasComponent(CCamera.class)
		.hasComponent(CTransform.class)
		.hasComponent(CBody.class);
		return datastore.executeQuery(query);
	}

	public List<Entity> getEntitiesToRender() {
		Query query = new Query();
		query
		.hasComponent(CPlayer.class)
		.hasComponent(CCamera.class)
		.hasComponent(CTransform.class)
		.hasComponent(CBody.class);
		return datastore.executeQuery(query);
	}

	@Override
	public void update(float deltaTime) {
		for (Entity entity : getEntitiesToProcess()) {
			CCamera camera = (CCamera) entity.getComponent(CCamera.class);
			CTransform transform = (CTransform) entity.getComponent(CTransform.class);
			CBody body = (CBody) entity.getComponent(CBody.class);

			if (camera != null && transform != null && body != null) {
				float targetX = transform.getPosition().getX() + body.getWidth() - camera.getWidth() / 2;
				float targetY = transform.getPosition().getY() + body.getHeight() - camera.getHeight() / 2;

				// Smoothly move the camera
				float alpha = deltaTime * camera.getCameraSpeed();

				int newOffsetX = (int) lerp(camera.getPosition().getX(), targetX, alpha);
				int newOffsetY = (int) lerp(camera.getPosition().getY(), targetY, alpha);

				if (newOffsetX < 0) {
					newOffsetX = 0;
				}
				if (newOffsetY < 0) {
					newOffsetY = 0;
				}
				if (newOffsetX + camera.getWidth() > camera.getBoundOffsetX()) {
					newOffsetX = camera.getBoundOffsetX() - camera.getWidth();
				}
				if (newOffsetY + camera.getHeight() > camera.getBoundOffsetY()) {
					newOffsetY = camera.getBoundOffsetY() - camera.getHeight();
				}

				camera.getPosition().setX(newOffsetX);
				camera.getPosition().setY(newOffsetY);
			}
		}
	}

	@Override
	public void render(Renderer renderer) {
		for (Entity entity : getEntitiesToRender()) {
			CCamera camera = (CCamera) entity.getComponent(CCamera.class);
			renderer.setCameraX((int) camera.getPosition().getX());
			renderer.setCameraY((int) camera.getPosition().getY());
		}
	}

	float lerp(float point1, float point2, float alpha) {
		return point1 + alpha * (point2 - point1);
	}

}
