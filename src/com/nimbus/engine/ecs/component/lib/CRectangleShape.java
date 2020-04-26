package com.nimbus.engine.ecs.component.lib;

import com.nimbus.engine.ecs.component.IComponent;

public class CRectangleShape implements IComponent {
	
	private boolean filled;
	private int colour;
	
	public CRectangleShape(boolean filled, int colour) {
		this.filled = filled;
		this.colour = colour;
	}

	public boolean isFilled() {
		return filled;
	}

	public void setFilled(boolean filled) {
		this.filled = filled;
	}

	public int getColour() {
		return colour;
	}

	public void setColour(int colour) {
		this.colour = colour;
	}

}
