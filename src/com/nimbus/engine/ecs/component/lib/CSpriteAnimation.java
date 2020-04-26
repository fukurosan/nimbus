package com.nimbus.engine.ecs.component.lib;

import java.util.List;

import com.nimbus.engine.ecs.component.IComponent;
import com.nimbus.engine.graphics.image.Image;
import com.nimbus.engine.graphics.image.ImageTile;

public class CSpriteAnimation implements IComponent {

	private ImageTile spriteMap;
	private List<int[]> frames;
	private int currentFrame;
	private float animationTimer;
	private float speed;
	private String name;
	
	//The list is expected to contain integer tuples with a x and y tile value for each frame
	public CSpriteAnimation(ImageTile spriteMap, List<int[]> frames, float speed, String name) {
		this.spriteMap = spriteMap;
		this.frames = frames;
		this.currentFrame = 0;
		this.speed = speed;
		this.name = name;
		animationTimer = 0;
	}
	
	//The list is expected to contain integer tuples with a x and y tile value for each frame
	public CSpriteAnimation(ImageTile spriteMap, List<int[]> frames, float speed) {
		this.spriteMap = spriteMap;
		this.frames = frames;
		this.currentFrame = 0;
		this.speed = speed;
		this.name = null;
		animationTimer = 0;
	}
	
	public Image getSprite() {
		int tileY = frames.get(currentFrame)[0];
		int tileX = frames.get(currentFrame)[1];
		return spriteMap.getTileImage(tileX, tileY);
	}

	public void nextFrame() {
		if(currentFrame == frames.size() - 1) {
			currentFrame = 0;
			animationTimer = 0;
		}
		else {
			currentFrame++;
		}
	}
	
	public float getAnimationTimer() {
		return animationTimer;
	}

	public void setAnimationTimer(float animation) {
		this.animationTimer = animation;
	}

	public float getSpeed() {
		return speed;
	}
	
	public int getCurrentFrame() {
		return currentFrame;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
