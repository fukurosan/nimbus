package com.nimbus.engine.ecs.component.lib;

import java.util.List;

import com.nimbus.engine.ecs.component.IGlobalComponent;
import com.nimbus.engine.ecs.entity.Entity;
import com.nimbus.engine.util.QuadTree;

public class GCQuadTree implements IGlobalComponent {
	
	private QuadTree quadTree;
	
	public GCQuadTree(int levelWidth, int levelHeight) {
		quadTree = new QuadTree(0, 0, 0, levelWidth, levelHeight);
	}

	public void clear() {
		quadTree.clear();
	}
	
	public void insert(Entity entity, int positionX, int positionY, int width, int height) {
		quadTree.insert(entity, positionX, positionY, width, height);
	}
	
	public List<Entity> findPotentialCollisions(Entity entity, int positionX, int positionY, int width, int height) {
		return quadTree.findPotentialCollisions(entity, positionX, positionY, width, height);
	}
	
	public QuadTree getQuadTree() {
		return quadTree;
	}

}
