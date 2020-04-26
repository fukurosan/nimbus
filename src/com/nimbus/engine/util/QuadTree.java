package com.nimbus.engine.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.nimbus.engine.ecs.entity.Entity;

//This quad tree helps with making collision detection way more efficient
public class QuadTree {

	private int MAX_OBJECTS = 6;
	private int MAX_LEVELS = 4;

	private int level;

	private List<EntityHitbox> entities;

	private int positionX;
	private int positionY;
	private int width;
	private int height;

	private QuadTree[] nodes; // 0 = top left, 1 = top right, 2 = bottom left, 3 = bottom right

	public QuadTree(int level, int positionX, int positionY, int treeWidth, int treeHeight) {
		this.level = level;
		this.positionX = positionX;
		this.positionY = positionY;
		this.width = treeWidth;
		this.height = treeHeight;
		entities = new ArrayList<EntityHitbox>();
		nodes = new QuadTree[4];
	}

	// Clear the current quadtree and all linked ones
	public void clear() {
		entities.clear();
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] != null) {
				nodes[i].clear();
				nodes[i] = null;
			}
		}
	}

	// Split the quadtree into four new quadtrees
	private void split() {
		int x = positionX;
		int y = positionY;
		int halfWidth = width / 2;
		int halfHeight = height / 2;

		nodes[0] = new QuadTree(level + 1, x, y, halfWidth, halfHeight);
		nodes[1] = new QuadTree(level + 1, x + halfWidth, y, halfWidth, halfHeight);
		nodes[2] = new QuadTree(level + 1, x, y + halfHeight, halfWidth, halfHeight);
		nodes[3] = new QuadTree(level + 1, x + halfWidth, y + halfHeight, halfWidth, halfHeight);
	}

	// Find what zone in the current quadtree a given object (basically a square)
	// fits into
	private int getIndex(EntityHitbox entity) {
		int index = -1;

		double horizontalCenter = positionX + width / 2;
		double verticalCenter = positionY + height / 2;

		boolean isFitsInTopQuadrant = entity.getY() + entity.getHeight() < verticalCenter;
		boolean isFitsInBottomQuadrant = entity.getY() > verticalCenter;
		boolean isFitsInLeftQuadrant = entity.getX() + entity.getWidth() < horizontalCenter;
		boolean isFitsInRightQuadrant = entity.getX() > horizontalCenter;

		if (isFitsInTopQuadrant) {
			if (isFitsInLeftQuadrant) {
				index = 0;
			} else if (isFitsInRightQuadrant) {
				index = 1;
			}
		}

		else if (isFitsInBottomQuadrant) {
			if (isFitsInLeftQuadrant) {
				index = 2;
			} else if (isFitsInRightQuadrant) {
				index = 3;
			}
		}

		return index;
	}

	// Insert a new entity into the tree
	public void insert(Entity entity, int positionX, int positionY, int width, int height) {
		EntityHitbox entityHitbox = new EntityHitbox(entity, positionX, positionY, width, height);
		insert(entityHitbox);
	}

	// Find the appropriate quadrant and place the entity into it
	private void insert(EntityHitbox entity) {
		if (nodes[0] != null) {
			int index = getIndex(entity);
			if (index != -1) {
				nodes[index].insert(entity);
				return;
			}
		}
		entities.add(entity);
		if (entities.size() > MAX_OBJECTS && level < MAX_LEVELS) {
			if (nodes[0] == null) {
				split();
			}
			for (int i = 0; i < entities.size(); i++) {
				int index = getIndex(entities.get(i));
				if (index != -1) {
					nodes[index].insert(entities.get(i));
					entities.remove(i);
					i--;
				}
			}
		}
	}

	// Find potential collision entities
	public List<Entity> findPotentialCollisions(Entity entity, int positionX, int positionY, int width, int height) {
		EntityHitbox entityHitbox = new EntityHitbox(entity, positionX, positionY, width, height);
		List<Entity> resultEntities = findPotentialCollisions(new ArrayList<Entity>(), entityHitbox);
		resultEntities.remove(entity);
		return resultEntities;
	}

	// Finds all objects that could potentially collide with another given object
	private List<Entity> findPotentialCollisions(List<Entity> returnObjects, EntityHitbox entity) {
		int index = getIndex(entity);
		if (nodes[0] != null) {
			if (index != -1) {
				nodes[index].findPotentialCollisions(returnObjects, entity);
			} else {
				nodes[0].findPotentialCollisions(returnObjects, entity);
				nodes[1].findPotentialCollisions(returnObjects, entity);
				nodes[2].findPotentialCollisions(returnObjects, entity);
				nodes[3].findPotentialCollisions(returnObjects, entity);
			}
		}
		List<Entity> containedEntities = entities.stream().map(hitbox -> hitbox.getEntity())
				.collect(Collectors.toList());
		returnObjects.addAll(containedEntities);
		return returnObjects;
	}

	// Private class to hold hitbox objects
	private class EntityHitbox {
		private Entity entity;
		private int width;
		private int height;
		private int x;
		private int y;

		public EntityHitbox(Entity entity, int x, int y, int width, int height) {
			this.entity = entity;
			this.width = width;
			this.height = height;
			this.x = x;
			this.y = y;
		}

		public Entity getEntity() {
			return entity;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}
	}

}
