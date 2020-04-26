package com.nimbus.engine.ecs.component.lib;

import com.nimbus.engine.ecs.component.IComponent;

public class CMass implements IComponent {
	int weight;

	public CMass(int weight) {
		this.weight = weight;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
}
