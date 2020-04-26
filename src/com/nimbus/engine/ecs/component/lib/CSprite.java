package com.nimbus.engine.ecs.component.lib;

import com.nimbus.engine.ecs.component.IComponent;
import com.nimbus.engine.graphics.image.Image;

public class CSprite implements IComponent{

	Image sprite;
	String name;
	
	public CSprite(Image sprite, String name) {
		this.sprite = sprite;
		this.name = name;
	}
	
	public CSprite(Image sprite) {
		this.sprite = sprite;
		this.name = null;
	}

	public Image getSprite() {
		return sprite;
	}

	public void setSprite(Image sprite) {
		this.sprite = sprite;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
